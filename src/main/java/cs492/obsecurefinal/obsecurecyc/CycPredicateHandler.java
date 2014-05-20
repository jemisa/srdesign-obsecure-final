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

import cs492.obsecurefinal.common.EntityTypes;
import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.metaintelligence.MetaFlow;
import cs492.obsecurefinal.metaintelligence.MetaPredicate;
import cs492.obsecurefinal.metaintelligence.PredicateHandler;
import cs492.obsecurefinal.metaintelligence.StringClassifierUtil;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCondition;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaMetric;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRule;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaWeight;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycFort;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycObject;

/**
 *
 * @author Benjamin Arnold
 */
public class CycPredicateHandler implements PredicateHandler {
    private static final String CYC_OBJECT_KEY = "cycObjectKey";
    private MetaPredicate metaPredicate;
    private CycPredicateType type;
    
    @Override
    public void setMetaPredicate(MetaPredicate metaPredicate) {
	this.metaPredicate = metaPredicate;
	String name = metaPredicate.getName();
	type = CycPredicateType.valueOf(name);
    }

    @Override
    public boolean apply(CycObject object, CycQuery context, String[] args) throws IOException {
	return type.apply(object, metaPredicate, context, args);
    }

    @Override
    public PredicateHandler newInstance() {
	return new CycPredicateHandler();
    }

    @Override
    public boolean applies(MetaFlow flow) {
	NamedEntity namedEntity = flow.getEntity();
	EntityTypes type = namedEntity.getType();
	CycQueryStrategy strategy = ObSecureCycStrategyFactory.lookupStrategy(type);
	boolean applies = false;
	try {
	    CycAccess cycAccess = ObSecureCycFacade.getInstance().getCycAccess();
	    CycQuery context = new CycQuery(strategy,cycAccess);
	    
	    Object o = flow.retrieveFromCache(CYC_OBJECT_KEY);
	    
	    if (o == null) {
		WordBall wordBall = new WordBall(namedEntity.getText());  //encapsulate word permutations
		CycList constants = strategy.getConstants(cycAccess, wordBall.iterator());
		for (Iterator it = constants.iterator(); it.hasNext();) {
		    CycObject co = (CycObject) it.next();
		    boolean coApplies = apply(co, context, new String[]{});
		    if (coApplies) {
			flow.cache(CYC_OBJECT_KEY, co);
			o = co;
			break;
		    }
		}
	    }
	    
	    applies = apply((CycObject) o, context, new String[]{});
	    
	} catch (Exception ex) {
	    Logger.getLogger(ObSecureCycFacade.class.getName()).log(Level.SEVERE, null, ex); //TODO: we need to implement a logging solution
	} 
	return applies;
    }
    
  
    private static enum CycPredicateType {
	ISA {
	    @Override
	    public boolean apply(CycObject object, MetaPredicate metaPredicate, CycQuery context, String[] args) throws IOException {
		boolean condition = false;
		CycAccess cycAccess = context.getCycAccess();
		CycQueryStrategy strategy = context.getStrategy();
		String conditional = metaPredicate.getCondition().getName();
		
		if ("ANY".equals(conditional)) {
		    MetaRule metaRule = metaPredicate.getRule();
		    MetaMetric metric = metaRule.getMetric();
		    Set<MetaWeight> weights = metric.getMetaWeights();
		    for (MetaWeight weight : weights) {
			MetaCondition nthCondition = new MetaCondition();
			nthCondition.setType(name());
			nthCondition.setName(weight.getName());
			MetaPredicate nthPredicate = new MetaPredicate(metaRule, nthCondition);
			condition = nthPredicate.getHandler().apply(object, context, args);
			if (condition) {
			    return true;
			}
		    }
		}
		
		CycList list = cycAccess.getAllIsa(object);
		CycList constants = strategy.cycConstantAutoCompleteExact(cycAccess, conditional);
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
