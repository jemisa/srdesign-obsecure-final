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
package cs492.obsecurefinal.wordngrams;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author JOEL
 */
public class TrigramStrategy extends NGramStrategy
{
    private List<String> stopwords;
    
    public TrigramStrategy(List<String> stopwords)
    {
        this.stopwords = stopwords;
    }
    
    // Extract all sets of 3 words from a sentence
    @Override
    public HashMap<String, Integer> getNGramDistribution(String sentence) 
    {
       String[] words = sentence.split(" ");
       
       if(words.length > 3)
       {
           for(int i = 0; i < words.length-2; i++)
           {
               String trigram = words[i] + " " + words[i+1] + " " + words[i+2];
               
               if(distribution.containsKey(trigram))
                    distribution.put(trigram, distribution.get(trigram) + 1);
               else
                   distribution.put(trigram, 1);
           }
       }
       
       return distribution;
    }
    
}
