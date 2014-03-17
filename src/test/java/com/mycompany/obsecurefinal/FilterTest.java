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

import cs492.obsecurefinal.cluster.Filter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Benjamin Arnold
 */
public class FilterTest {
  
     @Test
     public void filterTag() {
	 final String s = "<URL-hlm.bz>";
	 final String expected = "";
	 Filter filter = Filter.TAG;
	 
	 assertEquals(expected, Filter.scrub(s, filter));   
     }     
     
     @Test
     public void dontFilterNonRoot() {
	 final String s = "bought";
	 final String expected = "bought";
	 Filter filter = Filter.TAG;
	 
	 assertEquals(expected, Filter.scrub(s, filter));
     }
     
     @Test
     public void notFilter() {
	 final String s = "can't";
	 final String expected = "not";
	 Filter filter = Filter.APOSTRAPHE;
	 
	 assertEquals(expected, Filter.scrub(s, filter));
     }
     
     @Test
     public void amFilter() {
	 final String s = "I'm";
	 final String expected = "I";
	 Filter filter = Filter.APOSTRAPHE;
	 
	 assertEquals(expected, Filter.scrub(s, filter));
     }
     
     @Test
     public void underscoreSplit() {
	 final String s = "the_thing";
	 final String expected = "thing";
	 Filter filter = Filter.UNDERSCORE;
	 
	 assertEquals(expected, Filter.scrub(s, filter));
     }
     
     @Test
     public void boundaryTruncate() {
	 final String s = "_thing_";
	 final String expected = "thing";
	 Filter filter = Filter.UNDERSCORE;
	 
	 assertEquals(expected, Filter.scrub(s, filter));
     }
}
