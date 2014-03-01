/* 
 * Takes input string or document and returns the related topics and probabilities.
 * 
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
    
    // Used to set values in buildPipe
    public enum PipeType {
        CORPUS, MODEL
    }
    
    public Pipe buildPipe(PipeType type) {
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pattern for tokens: {L}etters, {N}umbers, {P}unctuation
        Pattern tokenPattern = Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}");
        
        if (type.equals(PipeType.CORPUS)) pipeList.add(new Input2CharSequence("UTF-8"));			// File format
        pipeList.add(new CharSequence2TokenSequence(tokenPattern));						// Tokenize strings
        pipeList.add(new TokenSequenceLowercase());								// Covert to lowercase to unify
        pipeList.add( new TokenSequenceRemoveStopwords(new File("stoplist.txt"), "UTF-8", false, false, false) );// Common words to be ignored
        pipeList.add(new TokenSequence2FeatureSequence());							// Convert to int
        if (type.equals(PipeType.CORPUS)) pipeList.add(new Target2Label());					// Convert labels
        if (type.equals(PipeType.CORPUS)) pipeList.add(new FeatureSequence2FeatureVector());                    // ID sequences to vector
        
        // FOR TESTING
        //if (type.equals(PipeType.MODEL)) pipeList.add(new PrintInputAndTarget());

        return new SerialPipes(pipeList);
    }
   	
    public InstanceList readFromStrings(String[] s){
        InstanceList instances = new InstanceList (buildPipe(PipeType.MODEL));        
        instances.addThruPipe(new StringArrayIterator(s));
        
        return instances;
    }
    
    public InstanceList readFromFile(String file) throws IOException{   	
        InstanceList instances = new InstanceList(buildPipe(PipeType.MODEL));
        
        Reader fileReader = new InputStreamReader(new FileInputStream(new File(file)), "UTF-8");
        instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // dataGroup, label, name fields
        
        return instances;
    }
    
    // Assuming that I am going to pass an instance list to the DAO
    public void getTopics(InstanceList instances){
    	//String[] topics = DAO.getTopicsFromInstanceList(instances);
    	//return topics;
    	
    	//or for each instace.getData()
    	//return topics;
    }
    
    // Returns largest possibility of distribution
    public double getProbability(InstanceList instances) throws IOException{
        int numTopics = 5;			// Results are inconsistant > 10 in testing
        int numThreads = 2;
        int numIterations = 50;  		// 1000 to 2000 recommended for production
        int numProbIterations = 50;
        double alpha = 1.00;
        double beta = 0.01;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, alpha, beta);

        model.addInstances(instances);
        model.setNumThreads(numThreads);
        model.setNumIterations(numIterations);
        model.estimate();
    	
        //System.out.println(model.displayTopWords(5, false));
        
        TopicInferencer inferencer = model.getInferencer();
        double[] probabilities = inferencer.getSampledDistribution(instances.get(0), numProbIterations, 10, 10);
        return probabilities[0];
    }
    
    
    

    
    // TO BE COVERED IN DB IMPORT?
    public void modelTopics(InstanceList instances) throws IOException {
        int numTopics = 5;			// Results are inconsistant > 10 in testing
        int numThreads = 2;
        int numIterations = 100;  	// 1000 to 2000 recommended for production
        double alpha = 1.00;
        double beta = 0.01;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, alpha, beta);

        // Model attributes
        model.addInstances(instances);
        model.setNumThreads(numThreads);
        model.setNumIterations(numIterations);
        model.estimate();
        
        // int to word mappings
        Alphabet dataAlphabet = instances.getDataAlphabet();
        
        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;
        
        Formatter out = new Formatter(new StringBuilder(), Locale.US);
        for (int position = 0; position < tokens.getLength(); position++) {
            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
        }
        out.close();

        // Topic probabilities
        double[] probability = model.getTopicProbabilities(0);

        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
   
        // Get pair ID and weight for topics
        for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
            
            out = new Formatter(new StringBuilder(), Locale.US);
            out.format("%d\t%.3f\t", topic, probability[topic]);
            int rank = 0;
            while (iterator.hasNext() && rank < 5) {
                IDSorter idCountPair = iterator.next();
                out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
                rank++;
            }
        }
        
        
        // Create a new instance with high probability of topic 0
        StringBuilder topicZeroText = new StringBuilder();
        Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

        int rank = 0;
        while (iterator.hasNext() && rank < 5) {
            IDSorter idCountPair = iterator.next();
            topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
            rank++;
        }

        
        InstanceList instanceList = new InstanceList(instances.getPipe());
        instanceList.addThruPipe(new Instance(topicZeroText.toString(), null, "topics", null));
        
        
        // Print largest possibility of distribution
        TopicInferencer inferencer = model.getInferencer();
        double[] probabilities = inferencer.getSampledDistribution(instanceList.get(0), 100, 10, 10);
        System.out.println("Probability: " + probabilities[0]);
	}
	
    
    // TO BE MOVED TO CORPUS BUILDER CLASS
    // Builds corpus from directory of files
    public InstanceList buildCorpus(String dir){
    	TopicIdentifier importer = new TopicIdentifier();
        InstanceList instances = importer.readDirectory(new File(dir));
        
        instances.save(new File("corpusOutput.txt"));
        return instances;
    }
    	
    // FROM EXAMPLE:
    // Used to build corpus.  Can be remvoed when moving to corpus database tables
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
