/* 
 * Populates database with topic model information from source document
 * Based off of UMass Mallet documentation
 *
 * For information on Mallet api calls:
 * http://mallet.cs.umass.edu/api/
 */
package cs492.obsecurefinal.common;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import cc.mallet.util.*;
import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.types.InstanceList;

/*
 * @author Mike
 */
public class TopicModeller {
   Pipe pipe;
   
   public TopicModeller(){
       pipe = buildPipe();
       InstanceList instance = this.readFromEditor();
       
       // TODO: save instance to database instead of file
       // instance.save(); 
       // May need to iterate through to save
   }
   
   public Pipe buildPipe(){
        ArrayList pipeList = new ArrayList();
        
        // Tokenizes using letters{L} and numbers{N}
        Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
        pipeList.add(new CharSequence2TokenSequence(tokenPattern));        
        
        // Left out : remove stop words
        // Left out : to lower case
        // Left out : convert strings to int
        
        // Convert target string to label
        pipeList.add(new Target2Label());
        
        // Convert feature sequences to feature vector for mapping IDs
        pipeList.add(new FeatureSequence2FeatureVector());
        
        // Left out : print input and target
        
        return new SerialPipes(pipeList);
   }
   
   public InstanceList readFromEditor(){
        InstanceList editorIns = new InstanceList(pipe);

        // TODO: Read from Editor
        // editorIns.addThruPipe();

        return editorIns;
   }
}
