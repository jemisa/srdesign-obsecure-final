/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.algorithm;

import cs492.obsecurefinal.common.Topic;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Joel Marcinik
 * Compares two topic lists to determine if they refer to the same context
 */
public class TopicMatcher 
{
    public static final double THRESHOLD_MULTIPLIER = 0.1;
    
    private  Topic[] topicListA, topicListB;
    
    public TopicMatcher(Topic[] a, Topic[] b)
    {
        topicListA = a.clone();
        topicListB = b.clone();       
    }
    
    // Return how close the match is.
    public double getMatchValue()
    {
        if(topicListA.length > 0 && topicListB.length > 0)
        {
            ArrayList<Integer> uniqueTopics = new ArrayList<>();        
            double countCloseMatch = 0.0; 
            
            // Sort to have a greater probability of matching right away.
            Arrays.sort(topicListA);
            Arrays.sort(topicListB);

            for(int i = 0; i < topicListA.length; i++)
            {                     
                for(int j = 0; j < topicListB.length; j++)
                {                 
                    // Track the number of unique topic id's uncovered
                    if(!uniqueTopics.contains(topicListA[i].getId()))
                        uniqueTopics.add(topicListA[i].getId());

                    if(!uniqueTopics.contains(topicListB[j].getId()))
                        uniqueTopics.add(topicListB[j].getId());

                    if(topicListA[i].getId() == topicListB[j].getId())
                    {
                        // For matching topics, see if the two probabilities are close.
                        
                        double probA = topicListA[i].getProbability();
                        double probB = topicListB[j].getProbability();

                        double fractionA = THRESHOLD_MULTIPLIER * probA;
                        double fractionB = THRESHOLD_MULTIPLIER * probB;

                        if(probA > probB - fractionB && probA < probB + fractionB)
                            countCloseMatch += 1.0 * ((probA + probB) / 2.0); // use average prob to weight it
                            //countCloseMatch++; // unweighted version
                    }
                }
            }

            return countCloseMatch/(double)(uniqueTopics.size());
        }
        else
            return 0.0;
    }
}
