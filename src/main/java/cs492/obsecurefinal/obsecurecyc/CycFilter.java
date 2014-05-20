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

import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycConstant;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycNart;
import org.opencyc.inference.InferenceResultSet;

/**
 *
 * @author Benjamin Arnold
 */
public enum CycFilter {
    INDIVIDUAL,HOMO_SAPIENS,HUMAN_INFANT;
    
    private final String fThing;
    
    private CycFilter() {
	String construct = StringUtils.EMPTY;
	String[] namespace = name().toLowerCase().split("_");
	for (String name : namespace) {
	    construct += StringUtils.capitalize(name);
	}
	fThing = construct;
    }
    
    protected CycList apply(CycAccess cycAccess, CycList input) throws IOException {
	CycConstant filter = cycAccess.getKnownConstantByName(fThing);
	return filter(input, filter);
    }
    
    protected CycList filter(CycList rawResults, CycConstant filter) {
	CycList cycList = new CycList();
	for (Object co : rawResults) {
	    if (co instanceof CycNart) {
		cycList.addAll(((CycNart)co).toDeepCycList());
	    } else if (co instanceof CycList) {
		cycList.addAll((CycList)co);
	    } else {
		cycList.add(co);
	    }
	}
	CycList result = cycList.flatten().removeDuplicates();
	result.remove(filter);
	
	return result;
    }
    
    protected static CycList filter(CycAccess cycAccess, CycList input, List<CycFilter> filters) throws IOException {
	CycList result = input;
	if (filters != null) {
	    for (CycFilter filter : filters) {
		result = filter.apply(cycAccess, result);    
	    }
	}
	return result;
    }	
    
//    protected static CycList filter(CycAccess cycAccess, InferenceResultSet input, CycFilter...filters) throws IOException {
//	List<List<Object>> rs = input.getRS();
//	CycList result = input;
//	if (filters != null) {
//	    for (CycFilter filter : filters) {
//		result = filter.apply(cycAccess, result);    
//	    }
//	}
//	return result;
//    }	
}
