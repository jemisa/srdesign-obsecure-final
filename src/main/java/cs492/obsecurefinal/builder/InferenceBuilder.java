/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.builder;

import cs492.obsecurefinal.algorithm.TopicIdentifier;
import cs492.obsecurefinal.common.Topic;
import java.io.File;


/**
 *
 * @author Ryan (Skeleton)
 */
public class InferenceBuilder 
{
    public InferenceBuilder()
    {   }
    
    // s is string to build the inference from, 
    // name is the name of the saved inference
    public void saveInference(String s, String name)
    {
        TopicIdentifier ident = new TopicIdentifier();
        Topic[] topics = ident.readFromStrings(new String[] {s});
    }
    
    // Loads an inference with the given name
    public Topic[] loadInference(String name)
    {
        return null;
    }
}
