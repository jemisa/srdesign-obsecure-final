package com.mycompany.obsecurefinal;

/*
 * @author Mike
 */

import cc.mallet.topics.*;
import cs492.obsecurefinal.algorithm.TopicIdentifier;
import cs492.obsecurefinal.common.Topic;
import java.io.IOException;
import static junit.framework.Assert.*;
import org.junit.Test;

public class TopicIdentifierTest {
    private int numTopics = 10;
    
    @Test 
    public void getInferencedTopicsFromString(){ 
        // I will revisit later
        /*      
        TopicIdentifier ti = new TopicIdentifier();
        String[] s = new String[] {
            "It is located in the Northeastern United States at the confluence of the Delaware and Schuylkill rivers, and it is the only consolidated city-county in Pennsylvania.",
            "Popular nicknames for Philadelphia are Philly and The City of Brotherly Love, the latter of which comes from the literal meaning of the city's name in ",
            "Philadelphia is known for its arts and culture. The cheesesteak and soft pretzel are emblematic of Philadelphia cuisine, which is shaped by the city's ethnic mix. "
        };
        
        Topic[] topicArray = ti.readFromStrings(s);
               
        assertNotNull(topicArray);
        assertEquals(numTopics, topicArray.length);
        
        confirmInference(topicArray);
        */
    }
    
    @Test
    public void getInferenceTopicsFromFile(){
       /* TopicIdentifier ti = new TopicIdentifier();
        Topic[] topicArray = null;
        
        try {
            topicArray = ti.readFromFile("");
        }
        catch (IOException e){         
        }
        
        assertNotNull(topicArray);
        assertEquals(numTopics, topicArray.length);
        
        confirmInference(topicArray);
        */
    }
    
    public void confirmInference(Topic[] topicArray){
        int count = 0;
        for (Topic t : topicArray){
            count += t.getProbability();
        }
        
        count = Math.round(count);
        assertEquals(count, 1.00);
    }
}
