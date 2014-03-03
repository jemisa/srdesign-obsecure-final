/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.algorithm;

import cc.mallet.types.InstanceList;
import cs492.obsecurefinal.common.Document;
import cs492.obsecurefinal.common.Agent;
import cs492.obsecurefinal.common.GeneralizationResult;
import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.common.SanitizationHint;
import cs492.obsecurefinal.common.SanitizationResult;
import cs492.obsecurefinal.common.Topic;
import cs492.obsecurefinal.obsecurecyc.ObSecureCycFacade;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
            
            for (int i = 0; i < sentences.length; i++)
            {
                // Get this sentence and the sentences surrounding it
                String sentence = sentences[i];
                String nextSentence = "";
                String prevSentence = "";
                
                if(i < sentences.length)
                    nextSentence = sentences[i+1];
                
                if(i > 0)
                    prevSentence = sentences[i-1];
                
                // extract entities from the current sentence
                EntityExtractor extractor = new EntityExtractor(sentence);
                
                List<NamedEntity> allEntities = extractor.extractAll();
                
                if(allEntities.size() > 0)
                {
                    // send sentences to topic modeller to see if a match is found against the privacy profile

                    TopicIdentifier ident = new TopicIdentifier();                                   
                    InstanceList documentInference = ident.readFromStrings(new String[] {prevSentence, sentence, nextSentence});
                    Topic[] topicList = ident.instanceToTopicArray(documentInference);
                    
                    boolean anyMatch = false;

                    // load instance lists for profile
                    List<Topic[]> profileInferences = new Vector<Topic[]>();
                    for(Topic[] inf : profileInferences)
                    {
                        TopicMatcher matcher = new TopicMatcher(inf, topicList);
                        if (matcher.getMatchValue() > 0.5) // TODO: adjust threshold value
                        {
                            anyMatch = true;
                            break;
                        }
                    }

                    //if topic modeller identifies private information, return lists of generalized entities
                    if(anyMatch)
                    {  
                        try
                        {
                            ObSecureCycFacade generalizer = ObSecureCycFacade.getInstance();

                            Map<NamedEntity, GeneralizationResult> generalizedResults = generalizer.generalize(allEntities);

                            for(NamedEntity ent: generalizedResults.keySet())
                            {
                                SanitizationHint hint = new SanitizationHint(ent, generalizedResults.get(ent));
                                finalResult.addHint(hint);
                            }
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace(System.out);
                        }
                    }
                }
            }
            
            return finalResult;
        }
        else
            return null;
    }
}
