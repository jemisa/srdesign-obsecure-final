package com.mycompany.obsecurefinal;

/*
 * Copyright (C) 2014 Benjamin Arnold
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

import cs492.obsecurefinal.cluster.WordNetDictionary;
import static org.junit.Assert.*;
import org.junit.Test;


/**
 * 
 * @author Benjamin Arnold <benjamin.arnold@drexel.edu>
 */
public class DictionaryTest  {
    
    @Test
    public void detectsWord() throws Exception {
	WordNetDictionary dictionary = WordNetDictionary.getInstance();
        final String visited = "visited";
	assertTrue(dictionary.isWord(visited));
	
	final String bought = "bought";
	assertTrue(dictionary.isWord(bought));
    }
    
    @Test
    public void notSynonym() throws Exception {
	WordNetDictionary dictionary = WordNetDictionary.getInstance();
        final String word = "visited";
	final String nonSynonym = "bought"; 
	assertFalse(dictionary.areRelated(word, nonSynonym));
    }
    
    @Test
    public void synonym() throws Exception {
	WordNetDictionary dictionary = WordNetDictionary.getInstance();
        final String word = "visited";
	final String nonSynonym = "travelled"; 
	assertTrue(dictionary.areRelated(word, nonSynonym));
    }
}
