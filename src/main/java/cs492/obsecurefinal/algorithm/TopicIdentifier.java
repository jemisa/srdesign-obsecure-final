/* 
 * Populates database with topic model information from source document
 * Based off of UMass Mallet documentation
 *
 * MALLET INFORMATION:
 * McCallum, Andrew Kachites.  "MALLET: A Machine Learning for Language Toolkit."
 * http://mallet.cs.umass.edu. 2002.
 */

package cs492.obsecurefinal.algorithm;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.regex.*;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.types.*;
import cc.mallet.topics.*;

public class TopicIdentifier {
	Pipe pipe;

    public TopicIdentifier() {
    }
    
    public enum PipeType {
        CORPUS, MODEL
    }
    
    public Pipe buildPipe(PipeType type) {
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pattern for tokens: {L}etters, {N}umbers, {P}unctuation
        Pattern tokenPattern = Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}");  //("[\\p{L}\\p{N}_]+");
        
        if (type.equals(PipeType.CORPUS)) pipeList.add(new Input2CharSequence("UTF-8"));	// File format
        pipeList.add(new CharSequence2TokenSequence(tokenPattern));				// Tokenize strings
        pipeList.add(new TokenSequenceLowercase());						// Covert to lowercase to unify
        pipeList.add( new TokenSequenceRemoveStopwords(new File("stoplist.txt"), "UTF-8", false, false, false) );// Common words to be ignored
        pipeList.add(new TokenSequence2FeatureSequence());					// Convert to int
        if (type.equals(PipeType.CORPUS)) pipeList.add(new Target2Label());			// Convert labels
        if (type.equals(PipeType.CORPUS)) pipeList.add(new FeatureSequence2FeatureVector());	// ID sequences to vector
        
        // FOR TESTING
        pipeList.add(new PrintInputAndTarget());

        return new SerialPipes(pipeList);
    }
   	
    // Builds corpus from directory of files
    public InstanceList buildCorpus(String dir){
    	TopicIdentifier importer = new TopicIdentifier();
        InstanceList instances = importer.readDirectory(new File(dir));
        
        instances.save(new File("corpusOutput.txt"));
        return instances;
    }
    
    public InstanceList readFromStrings(String[] s){
        InstanceList instances = new InstanceList (buildPipe(PipeType.MODEL));
        instances.addThruPipe(new StringArrayIterator(s));
        
        return instances;
    }
    
    public InstanceList readFromFile(String file) throws IOException{   	
        InstanceList instances = new InstanceList(buildPipe(PipeType.MODEL));
        
        Reader fileReader = new InputStreamReader(new FileInputStream(new File(file)), "UTF-8");
        instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // data, label, name fields
        
        return instances;
    }
    
    // TODO: return topics instead of printing
    public void modelTopics(InstanceList instances) throws IOException {
        int numTopics = 10;		// Results are inconsistant > 10 in testing
        int numThreads = 4;
        int numIterations = 50;  	// 1000 to 2000 recommended for production
        double alpha = 1.00;
        double beta = 0.01;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, alpha, beta);

        // Model attributes
        model.addInstances(instances);
        model.setNumThreads(numThreads);
        model.setNumIterations(numIterations);
        model.estimate();

        System.out.println("in");
        
        Alphabet dataAlphabet = instances.getDataAlphabet();
        
        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;
        
        Formatter out = new Formatter(new StringBuilder(), Locale.US);
        for (int position = 0; position < tokens.getLength(); position++) {
            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
        }

        // Topic probabilities
        double[] topicDistribution = model.getTopicProbabilities(0);

        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
        
        // Show number of topics with proportions for the first document
        for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
            
            out = new Formatter(new StringBuilder(), Locale.US);
            out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
            int rank = 0;
            while (iterator.hasNext() && rank < numTopics) {
                IDSorter idCountPair = iterator.next();
                out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
                rank++;
            }
        }
        
        // Create a new instance with high probability of topic 0
        StringBuilder topicZeroText = new StringBuilder();
        Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

        int rank = 0;
        while (iterator.hasNext() && rank < numTopics) {
            IDSorter idCountPair = iterator.next();
            topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
            rank++;
        }

        // Create a new instance named "test instance" with empty target and source fields.
        InstanceList testing = new InstanceList(instances.getPipe());
        testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

        TopicInferencer inferencer = model.getInferencer();
        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
        System.out.println("0\t" + testProbabilities[0]);
	}
		
    // FROM EXAMPLE:
    // Used to build corpus.  Can be removed when moving to corpus database tables
    public InstanceList readDirectory(File directory) {
        return readDirectories(new File[] {directory});
    }

    public InstanceList readDirectories(File[] directories) {
        FileIterator iterator =
            new FileIterator(directories,
                             new TxtFilter(),
                             FileIterator.LAST_DIRECTORY);

        InstanceList instances = new InstanceList(buildPipe(PipeType.CORPUS));
        
        instances.addThruPipe(iterator);

        return instances;
    }

    class TxtFilter implements FileFilter {
        public boolean accept(File file) {
            return file.toString().endsWith(".txt");
        }
    }
}
