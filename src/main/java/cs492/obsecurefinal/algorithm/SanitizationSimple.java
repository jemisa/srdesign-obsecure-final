/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.algorithm;

import cs492.obsecurefinal.common.Document;
import cs492.obsecurefinal.common.Agent;
import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.common.SanitizationResult;
import java.io.FileInputStream;
import java.util.List;
import opennlp.tools.sentdetect.*;

/**
 *
 * @author JOEL
 */
public class SanitizationSimple extends Sanitization
{
    Document doc;
    Agent profile;
    String text;
    
    // TODO: Take in document object instead of string
    public SanitizationSimple(Document d, Agent agent)
    {
        this.doc = d;
        this.profile = agent;
    }
    
    public SanitizationResult sanitize()
    {
        text = doc.getText();
                
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
            ex.printStackTrace(System.out);
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
            SanitizationResult finalResult = new SanitizationResult();
            
            SentenceDetectorME detector = new SentenceDetectorME(sm);
        
            String[] sentences = detector.sentDetect(text);
            
            for (String sentence: sentences)
            {
                // extract entities from each
                EntityExtractor extractor = new EntityExtractor(sentence);
                
                List<NamedEntity> allEntities = extractor.extractAll();
                
                // send entities to topic modeller to see if a match is found against the privacy profile
                
                TopicIdentifier ident = new TopicIdentifier();
                
                //if topic modeller identifies private information, return lists of generalized entities
            
                for(NamedEntity ent: allEntities)
                {
                    
                }
                
                
            }
            
            return finalResult;
        }
        else
            return null;
    }
}
