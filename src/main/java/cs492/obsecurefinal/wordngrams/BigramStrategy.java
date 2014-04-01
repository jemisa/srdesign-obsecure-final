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

/**
 *
 * @author JOEL
 */
public class BigramStrategy extends NGramStrategy
{

    @Override
    public HashMap<String, Integer> getNGramDistribution(String sentence) 
    {
       String[] words = sentence.split(" ");
       
       if(words.length > 2)
       {
           for(int i = 0; i < words.length-1; i++)
           {
               String bigram = words[i] + " " + words[i+1];
               
               if(distribution.containsKey(bigram))
                    distribution.put(bigram, distribution.get(bigram) + 1);
               else
                   distribution.put(bigram, 1);
           }
       }
       
       return distribution;
    }
    
}
