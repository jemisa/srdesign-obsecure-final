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

package cs492.obsecurefinal.cluster;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Benjamin Arnold
 */
public enum Filter {
    TAG("<",">") {
	@Override
	String apply(String string, String mask) {
	    return string.contains(mask) ? StringUtils.EMPTY : string;
	}
    },APOSTRAPHE("'") {
	@Override
	String apply(String string, String mask) {
	    String result = string;
	    Pattern p = Pattern.compile(mask);
	    Matcher m = p.matcher(result);
	    if (m.find()) {
		if (!string.startsWith(mask)) {
		    String not = result.substring(m.start()-1);
		    result = ("n't".equals(not)) ? "not" : result.substring(0,m.start());
		} else {
		    result = m.replaceAll(StringUtils.EMPTY);
		}
		
	    }

	    return result;
	}
    },UNDERSCORE("_") {
	@Override
	String apply(String string, String mask) {
	    String result = StringUtils.EMPTY;
	    String results[] = string.split(mask);
	    for (String res : results) {
		if (res.length() > result.length()) {  //choose longest word
		    result = res;
		}
	    }
	    
	    return result;
	}
    };
    
    private String [] indicators;
    
    private Filter(String...masks) {
	indicators = masks;
    }
    
    abstract String apply(String string, String mask);
    
    private String scrub(String string) {
	String base = string;
	if (indicators != null) {
	    for (String mask: indicators) {
		base = apply(base, mask);
	    }
	}
	return base;
    }
    
    public static String scrub(String string, Filter...filters) {
	String base = string;
	if (filters != null) {
	    for (Filter filter : filters) {
		base = filter.scrub(base);
	    }
	}
	return base;
    }
}
