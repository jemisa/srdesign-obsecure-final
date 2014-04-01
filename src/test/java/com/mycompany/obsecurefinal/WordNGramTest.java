/*
 * Copyright (C) 2014 Rysn
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

package com.mycompany.obsecurefinal;

import cs492.obsecurefinal.wordngrams.WordNGramExtractor;
import java.util.HashMap;
import static junit.framework.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Ryan Hersh
 */
public class WordNGramTest 
{
    String sentance = "This is my Sentance";
    int size = 3;
    
    @Ignore
    @Test
    public void NGramTest()
    {
        String[] words = sentance.split(sentance);
        
        WordNGramExtractor gram = new WordNGramExtractor(sentance);
        HashMap<String, Integer> dist = gram.getAllNGramDistributions(size);
        assertTrue(dist.isEmpty());
        
        for(int i=0; i< words.length; i++)
            assertEquals(dist.containsKey(words[i]), words[i]);
    }
}
