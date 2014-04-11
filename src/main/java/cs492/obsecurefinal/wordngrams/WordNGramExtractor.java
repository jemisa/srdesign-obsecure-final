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

import cs492.obsecurefinal.common.DataSourceNames;
import cs492.obsecurefinal.common.Debug;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author JOEL
 */
public class WordNGramExtractor 
{
    String sentence;
    NGramStrategy[] strats;
    List<String> stopwords;
    
    // create a new n-gram extractor
    public WordNGramExtractor(String sentence)
    {
        stopwords = new ArrayList<>();
        
        try
        {
            File stoplistFile = new File(DataSourceNames.TOPICS_STOPWORDS);
            if(stoplistFile.exists() && stoplistFile.canRead())
            {
                BufferedReader reader = new BufferedReader(new FileReader(stoplistFile));

                String word = reader.readLine();
                while(word != null)
                {
                    stopwords.add(word);
                    
                    word = reader.readLine();
                }
                
                reader.close();
            }
        }
        catch(Exception ex)
        {
            Debug.println("Cannot find stoplist file, common words will be included");
        }
        
        this.sentence = sentence;
        strats = new NGramStrategy[] {null, new BigramStrategy(stopwords), new TrigramStrategy(stopwords)};
        // index must correspond to the number of words in the n-gram
    }
    
    // returns all n-grams of a certain size
    public HashMap<String, Integer> getAllNGramDistributions(int size)
    {
       int sizeIndex = size - 1;
       
       if(sizeIndex > 0 && sizeIndex < strats.length && strats[sizeIndex] != null)
           return strats[sizeIndex].getNGramDistribution(sentence);
       else
           return new HashMap<>();
        
    }
}
