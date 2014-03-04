/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.algorithm;

import cc.mallet.types.InstanceList;
import cs492.obsecurefinal.common.Topic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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

            Arrays.sort(topicListA);
            Arrays.sort(topicListB);

            for(int i = 0; i < topicListA.length; i++)
            {                     
                for(int j = 0; j < topicListB.length; j++)
                {                 
                    if(!uniqueTopics.contains(topicListA[i].getId()))
                        uniqueTopics.add(topicListA[i].getId());

                    if(!uniqueTopics.contains(topicListB[j].getId()))
                        uniqueTopics.add(topicListB[j].getId());

                    if(topicListA[i].getId() == topicListB[j].getId())
                    {
                        double probA = topicListA[i].getProbability();
                        double probB = topicListB[j].getProbability();

                        double fractionA = THRESHOLD_MULTIPLIER * probA;
                        double fractionB = THRESHOLD_MULTIPLIER * probB;

                        if(probA > probB - fractionB && probA < probB + fractionB)
                            countCloseMatch += 1.0 * ((probA + probB) / 2); // use average prob to weight it
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
