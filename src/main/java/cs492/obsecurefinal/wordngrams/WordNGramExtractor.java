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
public class WordNGramExtractor 
{
    String sentence;
    NGramStrategy[] strats;
    
    // create a new n-gram extractor
    public WordNGramExtractor(String sentence)
    {
        this.sentence = sentence;
        strats = new NGramStrategy[] {new UnigramStrategy(), new BigramStrategy(), new TrigramStrategy()};
        // index must correspond to the number of words in the n-gram
    }
    
    // returns all n-grams of a certain size
    public HashMap<String, Integer> getAllNGramDistributions(int size)
    {
       int sizeIndex = size - 1;
       
       if(sizeIndex > 0 && sizeIndex < strats.length)
           return strats[sizeIndex].getNGramDistribution(sentence);
       else
           return new HashMap<String, Integer>();
        
    }
}
