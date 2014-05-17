/*
 * Copyright (C) 2014 JOEL
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cs492.obsecurefinal.algorithm;

import cs492.obsecurefinal.common.Topic;
import java.util.Map;

/**
 *
 * @author JOEL
 */
public class TopicModelingContextStrategy extends PrivateContextIdentStrategy
{
    public static final double EQUALITY_THRESHOLD = 0.2;
    
    String sentences;
    Map<String, Topic[]> profileInferences;
    TopicIdentifier ident;
    
    public TopicModelingContextStrategy(String sentences, Map<String, Topic[]> profileInferences, TopicIdentifier ident)
    {
        this.sentences = sentences;
        this.profileInferences = profileInferences;
        this.ident = ident;
    }
    
    @Override
    public PrivateContextMatch getMatchValue() 
    {
        // Run inference on sentence and context
        Topic[] topicList =  ident.readFromStrings(new String[] {sentences});

       // For each topic inference related to the agent,
       // check if the sentence matches
       for(String topicName : profileInferences.keySet())
       {
           Topic[] inf = profileInferences.get(topicName);
           
           TopicMatcher matcher = new TopicMatcher(inf, topicList);
           //TopicMatcher matcherNoEntities = new TopicMatcher(inf, topicListNoEntities);                            
           //TopicMatcher matcherInfoType = new TopicMatcher(inf, infPrivacyType);

           double matchWithEntities = matcher.getMatchValue();
          // double matchWithNoEntities = matcherNoEntities.getMatchValue();
           //double matchType = matcherInfoType.getMatchValue();

           //if (matchWithEntities > EQUALITY_THRESHOLD && matchWithNoEntities < matchWithEntities) // TODO: adjust threshold value
           //if(matchWithEntities > EQUALITY_THRESHOLD && matchType > EQUALITY_THRESHOLD)
           if(matchWithEntities > EQUALITY_THRESHOLD)
           {
               // mark entity as private
               return new PrivateContextMatch(matchWithEntities, topicName);
           }
       }
       
       return null;
    }
    
}
