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
    public static final double MIN_VALUE = 0.05;
    
    private  Topic[] topicListA, topicListB;
    
    public TopicMatcher(Topic[] a, Topic[] b)
    {
        topicListA = a.clone();
        topicListB = b.clone();       
    }
    
    // Return how close the match is.
    public double getMatchValue()
    {
        if(topicListA.length == topicListB.length)
        {     
            double countCloseMatch = 0.0; 
            
            // Sort to have a greater probability of matching right away.
            // Not needed because topic ids should correspond to array index
            //Arrays.sort(topicListA);
            //Arrays.sort(topicListB);

            //for(int i = 0; i < topicListA.length; i++)
            //{                     
                //for(int j = 0; j < topicListB.length; j++)
                //{                 
                    // Track the number of unique topic id's uncovered
                    //if(!uniqueTopics.contains(topicListA[i].getId()))
                     //   uniqueTopics.add(topicListA[i].getId());

                    //if(!uniqueTopics.contains(topicListB[j].getId()))
                    //    uniqueTopics.add(topicListB[j].getId());
            for(int i=0; i < topicListA.length; i++)
            {
                if(topicListA[i].getId() == topicListB[i].getId())
                {
                    // For matching topics, see if the two probabilities are close.

                    double probA = topicListA[i].getProbability();
                    double probB = topicListB[i].getProbability();
                    
                    if(probA >= MIN_VALUE && probB >= MIN_VALUE)
                    {
                        double fractionA = THRESHOLD_MULTIPLIER * probA;
                        double fractionB = THRESHOLD_MULTIPLIER * probB;

                        if(probA > probB - fractionB && probA < probB + fractionB)
                            countCloseMatch += 1.0 * ((probA + probB) / 2.0); // use average prob to weight it
                            //countCloseMatch++; // unweighted version
                
                    }
                }
            }
                //}
            //}

            return countCloseMatch;//(double)(uniqueTopics.size());
        }
        else
            return 0.0;
    }
}
