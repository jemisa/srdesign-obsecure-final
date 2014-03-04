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

import cs492.obsecurefinal.common.Topic;

/*
 * @author MIKE
 */


// DON'T CREATE MODEL FROM DOC TO BE SANITIZED
// TAKE MASTER MODEL (FROM TOPIC BUILDER) and RUN INFERENCES FROM THE DOCUMENT AGAINST IT
// TWO DIFFERENT DOCUMENTS WILL GO IN (DOCUMENT AND VALUE)

// MATCHER WILL TAKE TWO TOPIC[] 
public class TopicIdentifier {
    Pipe pipe;
    int numTopics = 5;
    int numTopWords = 5;

        
    public TopicIdentifier() {
    }
       
    public InstanceList readFromStrings(String[] s){
        InstanceList instances = new InstanceList (buildPipe());        
        instances.addThruPipe(new StringArrayIterator(s));
        
        return instances;        
        //return instanceToTopicArray(instances);
    }
    
    public InstanceList readFromFile(String file) throws IOException{   	
        InstanceList instances = new InstanceList(buildPipe());
        
        Reader fileReader = new InputStreamReader(new FileInputStream(new File(file)), "UTF-8");
        instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // dataGroup, label, name fields
        
        return instances;
        //return instanceToTopicArray(instances);
    }
    
    public Topic[] instanceToTopicArray(InstanceList instances){
    	ParallelTopicModel model = modelTopics(instances);
    	Topic[] topicArray = getTopicDetails(model, instances);
    	
    	return topicArray;
    }
    
    private Pipe buildPipe() {
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pattern for tokens: {L}etters, {N}umbers, {P}unctuation
        Pattern tokenPattern = Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}");
        
        pipeList.add(new CharSequence2TokenSequence(tokenPattern));									// Tokenize strings
        pipeList.add(new TokenSequenceLowercase());													// Covert to lowercase to unify
        pipeList.add(new TokenSequenceRemoveStopwords(new File("stoplist.txt"), "UTF-8", false, false, false));// Common words to be ignored
        pipeList.add(new TokenSequence2FeatureSequence());											// Convert to int
        
        // FOR TESTING
        //pipeList.add(new PrintInputAndTarget());

        return new SerialPipes(pipeList);
    }
    
    // Returns Topic Model for an instance
    private ParallelTopicModel modelTopics(InstanceList instances){
        int numThreads = 2;
        int numIterations = 10;  		// 1000 to 2000 recommended for production
        double alpha = 1.00;
        double beta = 0.01;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, alpha, beta);

        model.addInstances(instances);
        model.setNumThreads(numThreads);
        model.setNumIterations(numIterations);
        try {
                      model.estimate();
        } catch (IOException e) {
                      e.printStackTrace();
        }

        return model;
    }
  	
    // Returns array of Topics with IDs, probabilities, and top word list
    private Topic[] getTopicDetails(ParallelTopicModel model, InstanceList instances) {
        Topic[] topicArray = new Topic[numTopics];
        String[] topWords = new String[numTopWords];

        Alphabet dataAlphabet = instances.getDataAlphabet();

        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;

        double[] probability = model.getTopicProbabilities(0);

        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

        for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

            int rank = 0;
            while (iterator.hasNext() && rank < numTopWords) {
                IDSorter idCountPair = iterator.next();
                topWords[rank] = dataAlphabet.lookupObject(idCountPair.getID()).toString();
                rank++;
            }

            topicArray[topic] = new Topic(topic, probability[topic], topWords);
            topWords = new String[numTopWords];
        }

        return topicArray;
    }
}
