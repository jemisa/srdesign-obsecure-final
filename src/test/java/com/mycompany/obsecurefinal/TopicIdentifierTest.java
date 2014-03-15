package com.mycompany.obsecurefinal;

/*
 * @author Mike
 */

import cc.mallet.topics.*;
import cs492.obsecurefinal.algorithm.TopicIdentifier;
import cs492.obsecurefinal.common.Topic;
import cs492.obsecurefinal.builder.TopicBuilder;
import java.io.*;
import static junit.framework.Assert.*;
import org.junit.Test;

public class TopicIdentifierTest {
    private int numTopics = 10;
    private ParallelTopicModel model = null;
    private String testDirectory = "testdocs";
    private String malletFile = "\\dump.mallet";
    
    @Test
    public void buildTopicModel(){
        TopicBuilder tb = new TopicBuilder(numTopics);
        tb.setIterations(10);
        tb.loadRaw(testDirectory);
        
        tb.saveDatabase(testDirectory + malletFile);
        assertTrue(new File(testDirectory + malletFile).exists());
        
        model = tb.loadDatabase(testDirectory + malletFile);
        //model = tb.getModel();
        
        junit.framework.Assert.assertTrue(model.getData().size() > 0);
        assertEquals(model.getNumTopics(), 10);
        assertNotNull(model);
    }
    
    @Test 
    public void getInferencedTopicsFromString(){         
        TopicBuilder tb = new TopicBuilder(numTopics);
        model = tb.loadDatabase(testDirectory + malletFile);
        
        TopicIdentifier ti = new TopicIdentifier(model);
        String[] s = new String[] {
            "It is located in the Northeastern United States at the confluence of the Delaware and Schuylkill rivers, and it is the only consolidated city-county in Pennsylvania.",
            "Popular nicknames for Philadelphia are Philly and The City of Brotherly Love, the latter of which comes from the literal meaning of the city's name in ",
            "Philadelphia is known for its arts and culture. The cheesesteak and soft pretzel are emblematic of Philadelphia cuisine, which is shaped by the city's ethnic mix. "
        };
        
        Topic[] topicArray = ti.readFromStrings(s);
               
        assertNotNull(topicArray);
        assertEquals(numTopics, topicArray.length);
        
        confirmInference(topicArray);
    }
    
    @Test
    public void getInferenceTopicsFromFile(){
        TopicBuilder tb = new TopicBuilder(numTopics);
        model = tb.loadDatabase(testDirectory + malletFile);
        
        TopicIdentifier ti = new TopicIdentifier(model);
        Topic[] topicArray = null;
        
        try {
            topicArray = ti.readFromFile(testDirectory + "\\ap.txt");
        }
        catch (IOException e){         
        }
        
        assertNotNull(topicArray);
        assertEquals(numTopics, topicArray.length);
        
        confirmInference(topicArray);
    }
    
    public void confirmInference(Topic[] topicArray){
        double count = 0;
        for (Topic t : topicArray){
            count += t.getProbability();
        }
        
        count = Math.round(count);
        assertEquals(count, 1.0);
    }
}
