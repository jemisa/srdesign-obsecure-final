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
import cs492.obsecurefinal.builder.TopicBuilder;
import cs492.obsecurefinal.common.DataSourceNames;

/*
 * @author MIKE
 */

public class TopicIdentifier {
    private ParallelTopicModel model;
    private int numTopics = 5;
    private int numTopWords = 5;
    private String DEFAULT_DOCUMENT_DIRECTORY = "\\";
    
    public TopicIdentifier(){
        TopicBuilder tb = new TopicBuilder(numTopics, DEFAULT_DOCUMENT_DIRECTORY);
        model = tb.getModel();
    }
    
    public TopicIdentifier(ParallelTopicModel ptm) {
        model = ptm;
        numTopics = model.getNumTopics();
    }
    
    public TopicIdentifier(String location){
        TopicBuilder tb = new TopicBuilder(numTopics, location);
        model = tb.getModel();
    }
    
    public Topic[] readFromStrings(String[] s){
        String text = "";
        for(int i = 0; i < s.length; i++)
        {
            if(i > 0)
                text += " ";
            text += s[i];
        }
        
        String[] textArray = new String[] {text};
        
        InstanceList instances = new InstanceList (buildPipe());        
        instances.addThruPipe(new StringArrayIterator(textArray));
        
        Topic[] topicArray = getInference(instances);
        
        return topicArray;        
    }
    
    public Topic[] readFromFile(String file) throws IOException{   	
        InstanceList instances = new InstanceList(buildPipe());
        
        Reader fileReader = new InputStreamReader(new FileInputStream(new File(file)), "UTF-8");
        instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // dataGroup, label, name fields
        
        Topic[] topicArray = getInference(instances);
        
        return topicArray;
    }
    
    // Returns Topic[] for inferences of instances on model
    private Topic[] getInference(InstanceList instances){
        Topic[] topicArray = new Topic[numTopics];
        
        double[] dist = inferTopics(instances.get(0));
        
        for (int i=0; i < numTopics; i++){
            topicArray[i] = new Topic(i, dist[i], null);
        }
        
        return topicArray;
    }
    
    private double[] inferTopics(Instance instance){
        int numIterations = 1000;
        int numThinning = 20;     // The number of iterations between saved samples
        int numBurnin = 20;       // The number of iterations before the first saved sample
        
        TopicInferencer topicinfer = model.getInferencer();        
        double [] distribution = topicinfer.getSampledDistribution(instance, numIterations, numThinning, numBurnin);
        return distribution;
    }
    
    private Pipe buildPipe() {
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pattern for tokens: {L}etters, {N}umbers, {P}unctuation
        Pattern tokenPattern = Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}");
        
        pipeList.add(new CharSequence2TokenSequence(tokenPattern));
        pipeList.add(new TokenSequenceLowercase());
        pipeList.add(new TokenSequenceRemoveStopwords(new File(DataSourceNames.TOPICS_STOPWORDS), "UTF-8", false, false, false));
        pipeList.add(new TokenSequence2FeatureSequence());

        return new SerialPipes(pipeList);
    }
    
    /** Functions below may no longer be required.  Dependent on TopicBuilder functionality. **/
    
    // Will probably be removed
    private Topic[] instanceToTopicArray(InstanceList instances){
    	model = modelTopics(instances);
    	Topic[] topicArray = getTopicDetails(instances);
    	
    	return topicArray;
    }
    
    // Returns Topic Model for an instance
    private ParallelTopicModel modelTopics(InstanceList instances){
        int numThreads = 2;
        int numIterations = 10;  		// 1000 to 2000 recommended for production
        double alpha = 1.00;
        double beta = 0.01;
        ParallelTopicModel ptm = new ParallelTopicModel(numTopics, alpha, beta);

        ptm.addInstances(instances);
        ptm.setNumThreads(numThreads);
        ptm.setNumIterations(numIterations);
        try {
        	ptm.estimate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ptm;
    }
  	
    // Returns array of Topics with IDs, probabilities, and top word list
    // No longer used
    private Topic[] getTopicDetails(InstanceList instances) {
        Topic[] topicArray = new Topic[numTopics];
        String[] topWords = new String[numTopWords];
        //double[] probability = model.getTopicProbabilities(0);  // Don't think this is required anymore

        Alphabet dataAlphabet = instances.getDataAlphabet();
        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

        for (int topic = 0; topic < numTopics; topic++) {
            int rank = 0;
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

            while (iterator.hasNext() && rank < numTopWords) {
                IDSorter idCountPair = iterator.next();
                topWords[rank] = dataAlphabet.lookupObject(idCountPair.getID()).toString();
                rank++;
            }

            //topicArray[topic] = new Topic(topic, probability[topic], topWords);
            topicArray[topic] = new Topic(topic, 0, topWords);
            
            topWords = new String[numTopWords];
        }

        return topicArray;
    }
}
