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

package com.mycompany.obsecurefinal;

import cs492.obsecurefinal.cluster.BrownClusters;
import static junit.framework.Assert.assertEquals;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Benjamin Arnold
 */
public class BrownClusterTest {
    
    @Test
    public void simpleCluster() throws Exception {
	BrownClusters cluster = BrownClusters.getInstance();
	String word = "rathr";
	String expectedCluster = "rather";
	
	assertEquals(expectedCluster, cluster.cluster(word));
    }
    
    @Test
    public void wordClusterWithoutSpecialCharOnTarget() throws Exception {
	BrownClusters cluster = BrownClusters.getInstance();
	final String format = "I would %s die!";
	final String badWord = "rathr";
	final String expectedGoodWord = "rather";
	
	String sentence = String.format(format, badWord);
	final String expected = String.format(format, expectedGoodWord);
	
	String clusteredSentence = cluster.clusterSentence(sentence);
	assertEquals(expected, clusteredSentence);
    }
    
    @Test
    public void wordClusterWithSpecialCharOnTarget() throws Exception {
	BrownClusters cluster = BrownClusters.getInstance();
	final String format = "The things I would do %s!";
	final String badWord = "rathr";
	final String expectedGoodWord = "rather";
	
	String sentence = String.format(format, badWord);
	final String expected = String.format(format, expectedGoodWord);
	
	String clusteredSentence = cluster.clusterSentence(sentence);
	assertEquals(expected, clusteredSentence);
    }
    
    @Test
    public void nonSynonymClusterTest() throws Exception {
	BrownClusters cluster = BrownClusters.getInstance();
	final String initial = "I visited the Liberty Bell this weekend";
	
	String clusteredSentence = cluster.clusterSentence(initial);
	assertEquals(initial, clusteredSentence);
    }
    
    @Test
    public void coPoolSameCaseTest() throws Exception {
	BrownClusters cluster = BrownClusters.getInstance();
	final String initial = "location";
	final String replacement = "place";
	
	boolean coPooled = cluster.coPooled(initial, replacement);
	assertTrue(initial, coPooled);
    }
    
    @Test
    public void coPoolDiffCaseTest() throws Exception {
	BrownClusters cluster = BrownClusters.getInstance();
	final String initial = "location";
	final String replacement = "Place";
	
	boolean coPooled = cluster.coPooled(initial, replacement);
	assertTrue(initial, coPooled);
    }
    
    @Test
    public void coPoolTest() throws Exception {
	BrownClusters cluster = BrownClusters.getInstance();
	final String initial = "location";
	final String replacement = "site";
	
	boolean coPooled = cluster.coPooled(initial, replacement);
	assertTrue(initial, coPooled);
    }
    
}
