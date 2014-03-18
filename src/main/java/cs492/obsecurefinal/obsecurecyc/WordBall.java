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

package cs492.obsecurefinal.obsecurecyc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Benjamin Arnold
 */
public class WordBall {
    private String base;
    private final List<String> orderedWords = new ArrayList<>();
    
    public WordBall(String text) {
	base = text;
	List<String> basis = Arrays.asList(text.split(" ")); 
	if (basis.size()==1) {
	    orderedWords.add(text);
	} else {
	    for (int i = 0; i < basis.size(); i++) { //
		List<String> tBasis = new ArrayList<>();
		String tBase = StringUtils.EMPTY;
		for (int j = i; j < basis.size(); j++) {
		    tBase += basis.get(j);
		    if (j != i ) {   //don't add single words
			tBasis.add(tBase);  
		    }
		}
		Collections.reverse(tBasis);
		orderedWords.addAll(tBasis);
	    }
	}
    }
    
    public Iterator<String> iterator() {
	return orderedWords.iterator();
    }
    
    public String original() {
	return base;
    }
}
