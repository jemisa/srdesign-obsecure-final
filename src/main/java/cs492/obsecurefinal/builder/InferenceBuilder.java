/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.builder;

import cc.mallet.topics.ParallelTopicModel;
import cs492.obsecurefinal.algorithm.TopicIdentifier;
import cs492.obsecurefinal.common.Topic;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


/**
 *
 * @author Ryan (Skeleton)
 */
public class InferenceBuilder 
{
    ParallelTopicModel model;
    
    public InferenceBuilder()
    {
        model = null;
    }
    
    public InferenceBuilder(ParallelTopicModel model)
    {
        this.model = model;
    }
    
    // s is string to build the inference from, 
    // name is the name of the saved inference
    public void saveInference(String s, String name)
    {
        TopicIdentifier ident;
        
        if(model == null)
            ident = new TopicIdentifier();
        else
            ident = new TopicIdentifier(model);
        
        Topic[] topics = ident.readFromStrings(new String[] {s});
        
        try
        {
            File f = new File(name);
            if(f.createNewFile() || f.canWrite())
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter(f));
                for(int i = 0; i < topics.length; i++)
                {
                    if(i > 0)
                        writer.write(":");
                    
                    writer.write(topics[i].getId() + "," + topics[i].getProbability());
                }
                
                writer.close();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.out);
        }
    }
    
    // Loads an inference with the given name
    public Topic[] loadInference(String name)
    {
        try
        {
            File f = new File(name);
            if(f.exists() && f.canRead())
            {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String data = reader.readLine();
                
                String[] topicPairs = data.split(":");
                
                Topic[] result = new Topic[topicPairs.length];
                
                for(int i = 0; i < topicPairs.length; i++)
                {
                    String[] topic = topicPairs[i].split(",");
                    
                    int id=Integer.parseInt(topic[0]);
                    double prob=Double.parseDouble(topic[1]);
                    
                    Topic t = new Topic();
                    t.setId(id);
                    t.setProbability(prob);
                    result[i] = t;
                }
                
                reader.close();
                
                return result;
            }
            else
            {
                return new Topic[] {};
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.out);
            return new Topic[] {};
        }
    }
}
