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

import cs492.obsecurefinal.common.EntityTypes;
import cs492.obsecurefinal.common.Sentence;
import java.util.Map;

/**
 *
 * @author JOEL
 */
public class NGramContextStrategy extends PrivateContextIdentStrategy
{
    Sentence sentence;
    Map<EntityTypes, Map<String, Integer>> ngrams;
    
    public NGramContextStrategy(Sentence sentence, Map<EntityTypes, Map<String, Integer>> ngrams)
    {
        this.sentence = sentence;
        this.ngrams = ngrams;
    }
    
    @Override
    public PrivateContextMatch getMatchValue() 
    {      
        String text = sentence.getText().toUpperCase();
        text = text.replaceAll("\\p{Punct}", "");
        
        for(EntityTypes type: ngrams.keySet())
        {
            Map<String, Integer> topicalNGram = ngrams.get(type);
            
            for(String keyTerm: topicalNGram.keySet())
            {
                if(text.contains(keyTerm.toUpperCase()))
                    return new PrivateContextMatch(1.0, keyTerm, type.toString());
            }
        }
        
        return null;
    }
    
}
