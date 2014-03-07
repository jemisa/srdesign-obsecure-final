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
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import opennlp.tools.sentdetect.*;
import cs492.obsecurefinal.common.DataSourceNames;
import cs492.obsecurefinal.common.Sentence;
import cs492.obsecurefinal.generalization.GeneralizationManager;
import opennlp.tools.util.Span;

/**
 *
 * @author JOEL
 */
public class SanitizationSimple extends Sanitization
{
    public static final double EQUALITY_THRESHOLD = 0.75;
    
    Document doc;
    Agent profile;
    String text;
    
    // TODO: Take in document object instead of string
    public SanitizationSimple(Document d, Agent agent)
    {
        this.doc = d;
        this.profile = agent;
    }
    
    @Override
    public SanitizationResult sanitize()
    {
//        text = doc.getText();
//                
//        //split into sentences using natural language processing
//        FileInputStream modelInput = null;
//        SentenceModel sm = null;
//        
//        try
//        { 
//            modelInput  = new FileInputStream(DataSourceNames.SENT_MODEL_FILE);
//            
//            sm = new SentenceModel(modelInput);
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace(System.out);
//        }
//        finally
//        {
//            try
//            {
//                if(modelInput != null)            
//                    modelInput.close();
//            }
//            catch(Exception ex)
//            {                
//                ex.printStackTrace();
//            }
//        }
        
        // doc has been properly split into sentences
        if(doc.isValid())
        {
            SanitizationResult finalResult = new SanitizationResult();
            
            //SentenceDetectorME detector = new SentenceDetectorME(sm);
        
            //String[] sentenceStrings = detector.sentDetect(text);
            
            Sentence[] sentences = doc.getSentences(); //Sentence.convertStringArray(sentenceStrings);
            
            for (int i = 0; i < sentences.length; i++)
            {
                // Get this sentence and the sentences surrounding it
                String sentence = sentences[i].getText();
                String nextSentence = "";
                String prevSentence = "";
                
                if(i < sentences.length)
                    nextSentence = sentences[i+1].getText();
                
                if(i > 0)
                    prevSentence = sentences[i-1].getText();
                
                // extract entities from the current sentence
                EntityExtractor extractor = new EntityExtractor(sentences[i]);
                
                List<NamedEntity> allEntities = extractor.extractAll();
                
                if(allEntities.size() > 0)
                {
                    // send sentences to topic modeller to see if a match is found against the privacy profile

                   TopicIdentifier ident = new TopicIdentifier();
                    
                   HashMap<NamedEntity, Boolean> privateEntities = new HashMap<NamedEntity, Boolean>();
                                        
                    for(NamedEntity ent: allEntities)
                    {
                        privateEntities.put(ent, Boolean.FALSE);
                        
                        // Run inference on sentence and context
                         Topic[] topicList =  ident.readFromStrings(new String[] {prevSentence, sentence, nextSentence});
                                           
                        // Remove entity from the sentence
                        Span entitySpan = ent.getSpan();
                        String s1 = sentence.substring(0, entitySpan.getStart());
                        String s2 = sentence.substring(entitySpan.getEnd(), sentence.length() - 1);
                        String sentenceNoEntity = s1 + s2;                        
                                            
                        // Run the inference on the entity-less sentence and context
                        Topic[] topicListNoEntities = ident.readFromStrings(new String[] {prevSentence, sentenceNoEntity, nextSentence});
                                             
                        List<Topic[]> profileInferences = new Vector<Topic[]>(); // TODO: Load from builder
                        
                        for(Topic[] inf : profileInferences)
                        {
                            TopicMatcher matcher = new TopicMatcher(inf, topicList);
                            TopicMatcher matcherNoEntities = new TopicMatcher(inf, topicListNoEntities);
                            
                            double matchWithEntities = matcher.getMatchValue();
                            double matchWithNoEntities = matcherNoEntities.getMatchValue();
                            
                            if (matchWithEntities > EQUALITY_THRESHOLD && matchWithNoEntities < matchWithEntities) // TODO: adjust threshold value
                            {
                                privateEntities.put(ent, Boolean.TRUE);     
                                break;
                            }
                        }
                    }
                    
                    //if topic modeller identifies private information, return lists of generalized entities
                    
                    try
                    {
                        Map<NamedEntity, GeneralizationResult> generalizedResults = GeneralizationManager.generalize(allEntities);

                        for(NamedEntity ent: generalizedResults.keySet())
                        {
                            if(privateEntities.get(ent) == Boolean.TRUE)
                            {
                                SanitizationHint hint = new SanitizationHint(ent, generalizedResults.get(ent));
                                finalResult.addHint(hint);
                            }
                        }
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace(System.out);
                    }                    
                }
            }
            
            return finalResult;
        }
        else
            return null;
    }
}