package cs492.obsecurefinal.algorithm;

import cs492.obsecurefinal.common.Topic;

/**
 *
 * @author Ryan Hersh
 * Takes two topics and compares them.
 * 
 * First it sorts the topicLists by the highest, then compares the the ordered probabilities
 * to each other.  Finally, it returns the best probability.
 */
public class TopicMatch 
{
    private Topic[] topicListA, topicListB;
    
    public double TopicMatch(Topic[] a, Topic[] b)
    {
        topicListA = a.clone();
        topicListB = b.clone();
        return getBestProbability();
    }

    /**
     * Finds how close the match is.
     * @return Double 
     */
    private double getBestProbability() 
    {
        double bestProbability = 0;
        sort(topicListA);
        sort(topicListB);
        for(int i = 0; i < topicListA.length; i++)
        {
            double probability = topicListA[i].getProbability() + topicListB[i].getProbability();
            probability = probability/2;
            
            if(probability > bestProbability)
                bestProbability = probability;
        }
        
        return bestProbability;
    }
    
    /**
     * Sorts the List by it highest probabilities
     */
    private void sort(Topic[] list) 
    {
        boolean swapped = true;
        int j = 0;
        Topic tmp;
        while(swapped)
        {
            swapped = false;
            j++;
            for(int i = 0; i < list.length - j; i++)
            {
                if(list[i].getProbability() < list[i++].getProbability())
                {
                    tmp = list[i];
                    list[i] = list[i+1];
                    list[i+1] = tmp;
                    swapped = true;
                }
            }
        }
        
    }
    
    
}
