/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.algorithm;

import cc.mallet.topics.ParallelTopicModel;
import cs492.obsecurefinal.builder.InferenceBuilder;
import cs492.obsecurefinal.builder.NGramBuilder;
import cs492.obsecurefinal.builder.TopicBuilder;
import cs492.obsecurefinal.cluster.BrownClusters;
import cs492.obsecurefinal.common.Agent;
import cs492.obsecurefinal.common.DataSourceNames;
import cs492.obsecurefinal.common.Debug;
import cs492.obsecurefinal.common.Document;
import cs492.obsecurefinal.common.EntityTypes;
import cs492.obsecurefinal.common.GeneralizationResult;
import cs492.obsecurefinal.common.HintNoReplacements;
import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.common.HintWithReplacements;
import cs492.obsecurefinal.common.NGramHintWithReplacement;
import cs492.obsecurefinal.common.PrivacyStatus;
import cs492.obsecurefinal.common.SanitizationHint;
import cs492.obsecurefinal.common.SanitizationResult;
import cs492.obsecurefinal.common.Sentence;
import cs492.obsecurefinal.common.Topic;
import cs492.obsecurefinal.generalization.GeneralizationManager;
import cs492.obsecurefinal.wordngrams.NGram;
import cs492.obsecurefinal.wordngrams.NGramGeneralizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author JOEL
 */
public class SanitizationSimple extends Sanitization
{
    public static final int MIN_LENGTH_FOR_MODELLING = 100;
    public static final int SS_MIN_LENGTH_FOR_MODELLING = 100;
    
    Document doc;
    Agent profile;
    //String documenttext;
    
    // TODO: Take in document object instead of string
    public SanitizationSimple(Document d, Agent agent)
    {
        this.doc = d;
        this.profile = agent;
    }
    
    @Override
    public SanitizationResult sanitize()
    {
        Debug.println("Begin sanitization");
        
        // check that doc has been properly split into sentences
        if(doc.isValid())
        {
            TopicBuilder builder = new TopicBuilder(DataSourceNames.MASTER_MODEL);
            ParallelTopicModel master = builder.getModel();
            
            if(master == null)
            {
                System.out.println("No topic model loaded!");
                return null;
            }
            
            TopicIdentifier ident = new TopicIdentifier(master); //new TopicIdentifier();
            InferenceBuilder infBuilder = new InferenceBuilder(master); //new InferenceBuilder();
                      
            NGramBuilder ngramBuilder = new NGramBuilder();
            
            SanitizationResult finalResult = new SanitizationResult();
            
            Sentence[] sentences = doc.getSentences();
            
            HashMap<Sentence, PrivacyStatus> sentencePrivacyValue = new HashMap<>();
            
            // Get all private information about the agent, and topic distribution
            // associated with characteristic of the agent
            Map<String, Topic[]> profileInferences = new HashMap<>();
            for(EntityTypes type: EntityTypes.values())
            {
                //String profileEntity = profile.getCharacteristic(type).toUpperCase();
                //Topic[] infTopics = infBuilder.loadInference(profileEntity);
                Topic[] infTopics = infBuilder.loadInference(type.toString());
                if(infTopics.length > 0)
                    profileInferences.put(type.toString(), infTopics);
                
                // TODO: Add stuff for each individual item in the user's privacy profile
            }
            
            // Get ngrams associated with each topic value
            Map<EntityTypes, Map<String, Integer>> storedNgrams = new HashMap<>();
            for(EntityTypes type: EntityTypes.values())
            {
                Map<String, Integer> ngramset = ngramBuilder.loadNGrams(type.toString().toUpperCase() + "_NGRAMS");
                storedNgrams.put(type, ngramset);
                
                // TODO: Add ngrams for each individual item in the user's privacy profile
            }            
            
            for (int i = 0; i < sentences.length; i++)
            {
                sentencePrivacyValue.put(sentences[i], PrivacyStatus.UNKNOWN);
                
                // Get this sentence and the sentences surrounding it
                String sentence = BrownClusters.getInstance().clusterSentence(sentences[i].getText());
                String nextSentence = "";
                String prevSentence = "";
                
                if(i + 1 < sentences.length)
                    nextSentence = sentences[i+1].getText();
                else
                    nextSentence = sentences[i].getText();
                nextSentence = BrownClusters.getInstance().clusterSentence(nextSentence);
		
                if(i > 0)
                    prevSentence = sentences[i-1].getText();
                else
                    prevSentence = sentences[i].getText();
		prevSentence = BrownClusters.getInstance().clusterSentence(prevSentence);
                
                            
                PrivateContextIdentStrategy ngramChecker = new NGramContextStrategy(sentences[i], storedNgrams);
                PrivateContextIdentStrategy tmChecker = new TopicModelingContextStrategy(prevSentence + " " + sentence + " " + nextSentence, 
                                                                                         profileInferences, ident); 
                
                // extract entities from the current sentence
                EntityExtractor extractor = new EntityExtractor(sentences[i]);                
                List<NamedEntity> allEntities = extractor.extractAll();
                
                if(allEntities.size() > 0)
                {
                    sentencePrivacyValue.put(sentences[i], PrivacyStatus.YES);
                    
                    // Debug code                  
                    Debug.println("All located entities:");
                    for(NamedEntity testEnt: allEntities)
                        Debug.println(testEnt.getText());                   
                    
                    // send sentences to topic modeller to see if a match is found against the privacy profile
                                    
                   HashMap<NamedEntity, Boolean> privateEntities = new HashMap<>();
                                        
                    for(NamedEntity ent: allEntities)
                    {
                        privateEntities.put(ent, Boolean.FALSE);
                        
                        if(prevSentence.length() + sentence.length() + nextSentence.length() >= MIN_LENGTH_FOR_MODELLING)
                        {                        
                            Debug.println("sentences long enough for topic modelling");
                            
                            PrivateContextMatch match = tmChecker.getMatchValue();
                          
                            if(match != null)
                            {
                                privateEntities.put(ent, Boolean.TRUE);
                                Debug.println("Located private information with topic modeling");
                            }
                        }
                        else // if not enough text, use ngrams to check for privacy contents
                        {
                            PrivateContextMatch match = ngramChecker.getMatchValue();
                            
                            if(match != null)
                            {
                                privateEntities.put(ent, Boolean.TRUE);
                                Debug.println("Ngram found: " + match.getSensitiveText());
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
                                Debug.println("Sanitizing entity: " + ent.getText());
                                SanitizationHint hint = new HintWithReplacements(ent, generalizedResults.get(ent));
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
            
            
            NGramGeneralizer ngramgen = new NGramGeneralizer();
            ngramgen.loadGeneralizations();
            
            // Check sentences which contained no entities, and see if they contain private information
            // that can't be manually taken out.
            for(int i = 0; i < sentences.length; i++)
            {
                NGramContextStrategy ngramChecker = new NGramContextStrategy(sentences[i], storedNgrams);
                 PrivateContextIdentStrategy tmChecker = new TopicModelingContextStrategy(sentences[i].getText(), profileInferences, ident); 
                
                // only operate on sentences we haven't confirmed to be privacy violations
                if(sentencePrivacyValue.get(sentences[i]) == PrivacyStatus.UNKNOWN)
                {
                    Debug.println("Manual check of sentence \"" + sentences[i].getText() + "\"");
                                                        
                    if(sentences[i].getText().length() >= SS_MIN_LENGTH_FOR_MODELLING)
                    {
                        PrivateContextMatch match = tmChecker.getMatchValue();
                        
                        if(match != null)
                        {
                            Debug.println("Found private data via topic matching");
                            SanitizationHint hint = new HintNoReplacements(sentences[i], match.getMatchValue(), match.getDescriptor());
                            finalResult.addHint(hint);
                        }
                    }
                    else if(sentences[i].getText().length() < SS_MIN_LENGTH_FOR_MODELLING)        
                    {
                       PrivateContextMatch match = ngramChecker.getMatchValue();
                       if(match != null)
                       {
                           Debug.println("Found private data via ngram matching: " + match.getSensitiveText());
                           
                           SanitizationHint hint;
                           
                           String genresult = ngramgen.getGeneralizedNgram(match.getSensitiveText());
                           if(!genresult.equals(""))
                           {
                               NGram n = new NGram(match.getSensitiveText(), match.getDescriptor(), sentences[i]);
                               hint = new NGramHintWithReplacement(n, genresult, match.getMatchValue());
                           }
                           else
                           {
                                hint  = new HintNoReplacements(sentences[i], match.getMatchValue(), match.getDescriptor());
                           }
                           
                           finalResult.addHint(hint);
                       }
                    }                    
                    else
                        Debug.println("Nothing found in sentence");
                }
            }
            
            return finalResult;
        }
        else
            return null;
    }
}
  