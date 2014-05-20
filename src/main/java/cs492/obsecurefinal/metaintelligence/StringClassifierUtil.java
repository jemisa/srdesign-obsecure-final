/*
 * Copyright (C) 2014 Drexel University
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

package cs492.obsecurefinal.metaintelligence;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Benjamin Arnold
 */
public class StringClassifierUtil implements ClassificationExpressions {
    private static final StringClassifierUtil instance = new StringClassifierUtil();
    
    private StringClassifierUtil() {
	//singleton
    }
    
    public static final StringClassifierUtil getInstance() {
	return instance;
    }
    
    public boolean matches(String type, String searchable) {
	Type searchType = Type.valueOf(type);
	return searchType.matches(searchable);
    }
    
    private static enum Type {
	DATE {
	    @Override
	    boolean matches(String searchable) {
		Pattern pYear = Pattern.compile(FOUR_DIGIT_NUMBER);
		Pattern pShortYear = Pattern.compile(TWO_DIGIT_YEAR);
		
		Matcher mYear = pYear.matcher(searchable);
		Matcher mShortYear = pShortYear.matcher(searchable);
		return mYear.find() || mShortYear.find();
	    }
	    
	};
	
	abstract boolean matches(String searchable);
    }
}
