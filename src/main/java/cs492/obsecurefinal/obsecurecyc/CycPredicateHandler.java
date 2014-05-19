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

package cs492.obsecurefinal.obsecurecyc;

import cs492.obsecurefinal.metaintelligence.MetaPredicate;
import cs492.obsecurefinal.metaintelligence.PredicateHandler;
import cs492.obsecurefinal.metaintelligence.StringClassifierUtil;
import java.io.IOException;
import java.util.Iterator;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycFort;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycObject;

/**
 *
 * @author Benjamin Arnold
 */
public class CycPredicateHandler implements PredicateHandler {
    private final MetaPredicate metaPredicate;
    private final CycPredicateType type;
    
    public CycPredicateHandler(MetaPredicate metaPredicate) {
	this.metaPredicate = metaPredicate;
	String name = metaPredicate.getName();
	type = CycPredicateType.valueOf(name);
    }

    @Override
    public boolean apply(CycObject object, CycQuery context, String[] args) throws IOException {
	return type.apply(object, metaPredicate, context, args);
    }
    
  
    
    private static enum CycPredicateType {
	ISA {
	    @Override
	    public boolean apply(CycObject object, MetaPredicate metaPredicate, CycQuery context, String[] args) throws IOException {
		boolean condition = false;
		CycAccess cycAccess = context.getCycAccess();
		CycQueryStrategy strategy = context.getStrategy();
		
		CycList list = cycAccess.getAllIsa(object);
		CycList constants = strategy.cycConstantAutoCompleteExact(cycAccess, metaPredicate.getCondition().getName());
		for (Iterator it = constants.iterator(); it.hasNext();) {
		    CycFort fort = (CycFort) it.next();
		    for (Iterator isIt = list.iterator(); isIt.hasNext();) {
			CycFort isa = (CycFort) isIt.next();
			   if (fort.compareTo(isa) == 0) {
			       condition = true;
			       break;
			   }
		    }
		}
		
		return condition;
	    }
	    
	}, COMMENT {
	    @Override
	    public boolean apply(CycObject object, MetaPredicate metaPredicate, CycQuery context, String[] args) throws IOException {
		CycAccess cycAccess = context.getCycAccess();
		
		String comment = cycAccess.getComment(object);
		String searchType = metaPredicate.getCondition().getName();
		
		return StringClassifierUtil.getInstance().matches(searchType, comment);
	    }
	};
	
	abstract public boolean apply(CycObject object, MetaPredicate metaPredicate, CycQuery context, String[] args) throws IOException;
	
    }
    
}
