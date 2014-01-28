/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal;

import java.io.FileInputStream;
import opennlp.tools.sentdetect.*;

/**
 *
 * @author JOEL
 */
public class SimpleSanitization extends Sanitization
{
    String text;
    
    // TODO: Take in document object instead of string
    public SimpleSanitization(String s)
    {
        this.text = s;
    }
    
    public void sanitize()
    {
        //TODO: preprocessing goes here
        
        //split into sentences using natural language processing
        FileInputStream modelInput = null;
        SentenceModel sm = null;
        
        try
        { 
            modelInput  = new FileInputStream("en-sent.bin");
            
            sm = new SentenceModel(modelInput);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                if(modelInput != null)            
                    modelInput.close();
            }
            catch(Exception ex)
            {                
                ex.printStackTrace();
            }
        }
        
        // sentence model is initialized, split into sentences
        if(sm != null)
        {
            SentenceDetectorME detector = new SentenceDetectorME(sm);
        
            String[] sentences = detector.sentDetect(text);
            
            for (String sentence: sentences)
            {
                // extract entities from each
                EntityExtractor extractor = new EntityExtractorSimple(sentence);
                
                // send entities to topic modeller to see if a match is found against the privacy profile
                
                //if topic modeller identifies private information, return lists of generalized entities
            }
        }
    }
}
