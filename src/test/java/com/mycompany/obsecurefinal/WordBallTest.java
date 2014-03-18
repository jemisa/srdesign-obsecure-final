package com.mycompany.obsecurefinal;

/*
 * Copyright (C) 2014 Benjamin Arnold
 *
 * This program is free software: you can redistribute order and/or modify
 * order under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that order will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import cs492.obsecurefinal.obsecurecyc.WordBall;

import java.util.Iterator;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Benjamin Arnold
 */
public class WordBallTest {
    
     @Test
     public void singleWord() throws Exception {
	 final String challenge = "challenge";
	 
	 WordBall ball = new WordBall(challenge);
	 assertEquals(challenge, ball.iterator().next());
     }
     
      @Test
     public void simpleSplit() throws Exception {
	 final String challenge = "Liberty Bell";
	 String[] expectedOrder = {"LibertyBell"};
	 
	 WordBall ball = new WordBall(challenge);
	 
	Iterator<String> order = ball.iterator();
	 for (String expected : expectedOrder) {
	     assertEquals(expected, order.next());
	 }
     }
     
      @Test
     public void multiSplit() throws Exception {
	 final String challenge = "Star Wars Movie The";
	 String[] expectedOrder = {"StarWarsMovieThe", "StarWarsMovie", "StarWars" ,"WarsMovieThe", "WarsMovie",  "MovieThe"};
	 
	 WordBall ball = new WordBall(challenge);
	 
	 Iterator<String> order = ball.iterator();
	 for (String expected : expectedOrder) {
	     assertEquals(expected, order.next());
	 }
     }
     
}
