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
import cs492.obsecurefinal.common.PrivacyStatus;
import cs492.obsecurefinal.common.SanitizationHint;
import cs492.obsecurefinal.common.SanitizationResult;
import cs492.obsecurefinal.common.Sentence;
import cs492.obsecurefinal.common.Topic;
import cs492.obsecurefinal.generalization.GeneralizationManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author JOEL
 */
public class SanitizationSimple extends Sanitization
{
    public static final double EQUALITY_THRESHOLD = 0.5;
    public static final int MAX_WINDOW = 3;
    public static final int MIN_WINDOW = 2;
    
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
                        
                        if(prevSentence.length() + sentence.length() + nextSentence.length() > 100)
                        {                        
                            // Run inference on sentence and context
                             Topic[] topicList =  ident.readFromStrings(new String[] {prevSentence, sentence, nextSentence});

                            // Remove entity from the sentence
                            //Span entitySpan = ent.getSpan();
                            //String s1 = sentence.substring(0, entitySpan.getStart());
                            //String s2 = sentence.substring(entitySpan.getEnd(), sentence.length() - 1);
                            String sentenceNoEntity = ent.getSentenceNoEntity().getText();

                            // Run the inference on the entity-less sentence and context
                            Topic[] topicListNoEntities = ident.readFromStrings(new String[] {prevSentence, sentenceNoEntity, nextSentence});

                            List<Topic[]> profileInferences = new ArrayList<>();

                            // Get all private information about the agent, and topic distribution
                            // associated with characteristic of the agent
                            for(EntityTypes type: EntityTypes.values())
                            {
                                String profileEntity = profile.getCharacteristic(type).toUpperCase();
                                Topic[] infTopics = infBuilder.loadInference(profileEntity);
                                if(infTopics.length > 0)
                                    profileInferences.add(infTopics);
                            }



                            // Load topic distributions for the type of privacy data to which the entity
                            // belongs
                            Topic[] infPrivacyType = infBuilder.loadInference(ent.getType().toString());

                            // For each topic inference related to the agent,
                            // check if the sentence matches
                            for(Topic[] inf : profileInferences)
                            {
                                TopicMatcher matcher = new TopicMatcher(inf, topicList);
                                TopicMatcher matcherNoEntities = new TopicMatcher(inf, topicListNoEntities);                            
                                TopicMatcher matcherInfoType = new TopicMatcher(inf, infPrivacyType);

                                double matchWithEntities = matcher.getMatchValue();
                                double matchWithNoEntities = matcherNoEntities.getMatchValue();
                                double matchType = matcherInfoType.getMatchValue();

                                //if (matchWithEntities > EQUALITY_THRESHOLD && matchWithNoEntities < matchWithEntities) // TODO: adjust threshold value
                                //if(matchWithEntities > EQUALITY_THRESHOLD && matchType > EQUALITY_THRESHOLD)
                                if(matchWithEntities > EQUALITY_THRESHOLD)
                                {
                                    // mark entity as private
                                    privateEntities.put(ent, Boolean.TRUE);     
                                    break;
                                }
                            }
                        }
                        else // if not enough text, use ngrams to check for privacy contents
                        {
                            // Get ngrams associated with each topic value
                            List<Map<String, Integer>> storedNgrams = new ArrayList<>();
                            for(EntityTypes type: EntityTypes.values())
                            {
                                Map<String, Integer> ngramset = ngramBuilder.LoadNGrams(text.toUpperCase() + "_NGRAM");
                                storedNgrams.add(ngramset);
                            }
                            
                            String text = sentence.toUpperCase();
        
                            for(Map<String, Integer> topicalNGram: storedNgrams)
                            {
                                for(String keyTerm: topicalNGram.keySet())
                                {
                                    if(text.contains(keyTerm.toUpperCase()))
                                    {
                                        privateEntities.put(ent, Boolean.TRUE);
                                        break;
                                    }
                                }
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
            
            // Get all private information about the user, and topics associated
            // with each piece of private information
            List<Topic[]> profileInferences = new ArrayList<>();
            for(EntityTypes type: EntityTypes.values())
            {
                String profileEntity = profile.getCharacteristic(type);
                Topic[] infTopics = infBuilder.loadInference(profileEntity.toUpperCase());
                if(infTopics.length > 0)
                    profileInferences.add(infTopics);
            }
            
            List<Map<String, Integer>> storedNgrams = new ArrayList<>();
            for(EntityTypes type: EntityTypes.values())
            {
                Map<String, Integer> ngramset = ngramBuilder.LoadNGrams(text.toUpperCase() + "_NGRAM");
                storedNgrams.add(ngramset);
            }
            
            // Check sentences which contained no entities, and see if they contain private information
            // that can't be manually taken out.
            for(int i = 0; i < sentences.length; i++)
            {
                if(sentencePrivacyValue.get(sentences[i]) == PrivacyStatus.UNKNOWN)
                {
                    if(checkSentenceNgramMatch(sentences[i], storedNgrams))
                    {
                        SanitizationHint hint = new HintNoReplacements(sentences[i], 1);
                        finalResult.addHint(hint);
                    }
                    //else if(checkSentenceTopicMatch(sentences[i], profileInferences, ident))
                    //{
                    //    SanitizationHint hint = new HintNoReplacements(sentences[i], 1);
                    //    finalResult.addHint(hint);
                    //}
                }
            }
            
            return finalResult;
        }
        else
            return null;
    }
    
    private boolean checkSentenceTopicMatch(Sentence sentence, List<Topic[]> profileInferences, TopicIdentifier ident)
    {
        // only operate on sentences we haven't confirmed to be privacy violations
                
        // get inference on full sentence
         Topic[] topicList = ident.readFromStrings(new String[] {sentence.getText()});

         String[] words = sentence.getText().split(" ");

         // Start with a window size of 2 and work up to max window size, increment each time
         int windowSize = MIN_WINDOW;
         while(windowSize < MAX_WINDOW)
         {
             // iterate through words, taking <window size> words at a time.
             int windowStart = 0;
             while(windowStart <= words.length - windowSize)
             {
                 // create a new array containing words which are not inside the window

                 int index = 0;
                 String[] wordsMinusWindow = new String[words.length - windowSize];
                 for(int wordIndex=0; wordIndex<words.length; wordIndex++)
                 {
                     if(index < wordsMinusWindow.length && (wordIndex < windowStart || wordIndex >= windowStart + windowSize))
                     {
                         wordsMinusWindow[index] = words[wordIndex];
                         index++;
                     }
                 }

                 // Run inference on sentence without window
                 Topic[] topicListNoWindow = ident.readFromStrings(wordsMinusWindow);

                 // Compare inferences to each inference based on profile items
                 for(Topic[] inf : profileInferences)
                {
                    TopicMatcher matcherWithWindow = new TopicMatcher(inf, topicList);   
                    TopicMatcher matcherWithoutWindow = new TopicMatcher(inf, topicListNoWindow); 
                    double match = matcherWithWindow.getMatchValue();
                    double matchNoWindow = matcherWithoutWindow.getMatchValue();

                    if(match > EQUALITY_THRESHOLD && match > matchNoWindow)
                    {
                        return true;
                       // break;
                    }   
                }

                 windowStart++;
             }

             windowSize++;
         }

         return false;
    }

    private boolean checkSentenceNgramMatch(Sentence sentence, List<Map<String, Integer>> ngrams)
    {
        String text = sentence.getText().toUpperCase();
        
        for(Map<String, Integer> topicalNGram: ngrams)
        {
            for(String keyTerm: topicalNGram.keySet())
            {
                if(text.contains(keyTerm.toUpperCase()))
                    return true;
            }
        }
        
        return false;
    }
}