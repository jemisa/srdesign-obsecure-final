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

import cs492.obsecurefinal.metaintelligence.FunctionHandler;
import cs492.obsecurefinal.metaintelligence.MetaFunction;
import cs492.obsecurefinal.metaintelligence.MetaPredicate;
import cs492.obsecurefinal.metaintelligence.PredicateHandler;
import cs492.obsecurefinal.obsecurecyc.CycQueryBuilder.QueryType;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCondition;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaMetric;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRule;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaWeight;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycConstant;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycObject;
import org.opencyc.cycobject.CycVariable;

/**
 *
 * @author Benjamin Arnold
 */
public class CycFunctionHandler implements FunctionHandler {
    private final MetaFunction metaFunction;
    private final Mode mode;
    private QueryType queryType;
    
    public CycFunctionHandler(MetaFunction metaFunction) {
	this.metaFunction = metaFunction;
	String modeName = metaFunction.getAction().getName();
	this.mode = Mode.valueOf(modeName);
    }
    
    protected QueryType getQueryType() {
	return queryType;
    }
    
    protected MetaFunction getMetaFunction() {
	return metaFunction;
    }

    @Override
    public CycList invoke(CycObject object, CycQuery context, String[] args) throws IOException {
	String type = metaFunction.getName().toUpperCase();
	queryType = QueryType.valueOf(type);
	return mode.applyMode(object, this, context, args);
    }

    private CycList invokeQuery(CycObject object, final QueryType queryType, CycQuery context) throws IOException {
	final CycAccess cycAccess = context.getCycAccess();
	final CycQueryStrategy strategy = context.getStrategy();
	final String microtheory = strategy.getMicroTheory();

	CycQueryExecutor executor = new CycQueryExecutor() {
	    @Override
	    public CycList loop(CycObject cycObject) throws UnknownHostException, IOException {
		CycList query = CycQueryBuilder.makeQuery(cycAccess, cycObject, queryType);
		CycVariable variable = CycQueryBuilder.makeVariable();
		CycConstant mtGeo = cycAccess.getConstantByName(microtheory);
		return cycAccess.queryVariable(query, variable, mtGeo);
	    }

	    @Override
	    public CycList filter(CycList input) throws IOException {
		return CycFilter.filter(cycAccess, input, strategy.getFilters());
	    }
	};

	CycList result = executor.execute(object);
	return result;
    }
    
    private static enum Mode {
	AUTO {
	    @Override
	    protected CycList applyMode(CycObject cycObject, CycFunctionHandler handler, CycQuery context, String[] args) throws IOException {
		CycAccess cycAccess = context.getCycAccess();
		CycQueryStrategy strategy = context.getStrategy();
		
		MetaFunction metaFunction = handler.getMetaFunction();
		Integer distance = metaFunction.getAction().getValue();
		MetaWeight targetWeight = getTargetWeight(cycObject, metaFunction, context, distance, args);
		
		CycList results = new CycList();
		String name = targetWeight.getName();
		CycList constants = strategy.cycConstantAutoCompleteExact(cycAccess, name);
		CycObject constant0 = (CycObject) constants.get(0);
		CycList queryResults = handler.invokeQuery(cycObject, handler.getQueryType(), context);
		for (Iterator it = queryResults.iterator(); it.hasNext();) {
		    CycObject co = (CycObject) it.next();   
		    CycList isas = cycAccess.getIsas(co, context.getStrategy().getMicrotheory(cycAccess));
		    for (Iterator isIt = isas.iterator(); isIt.hasNext();) {
			CycObject co2 = (CycObject) isIt.next();
			if (constant0.compareTo(co2) == 0) {
			    results.add(co);
			}
		    }
		}
		return results;
	    }
	    
	};
	
	protected MetaWeight getTargetWeight(CycObject cycObject, MetaFunction metaFunction, CycQuery context, Integer distance, String[] args) throws IOException {
	    MetaWeight currentWeight = getCurrentWeight(cycObject, metaFunction, context, args);
	    
	    Integer targetWeight = distance + currentWeight.getValue();
	    Integer fallbackWeight = 1 + currentWeight.getValue();

	    MetaRule rule = metaFunction.getRule();
	    MetaMetric metric = rule.getMetric();
	    Set<MetaWeight> weights = metric.getMetaWeights();
	    
	    MetaWeight newWeight = null;
	    for (MetaWeight weight : weights) {
		if (targetWeight.equals(weight.getValue())) {
		    newWeight = weight;
		    break;
		} else if (fallbackWeight.equals(weight.getValue())) {
		    newWeight = weight;
		    //and don't break
		}
	    }
	    return newWeight;
	}
	
	protected MetaWeight getCurrentWeight(CycObject cycObject, MetaFunction metaFunction, CycQuery context, String[] args) throws IOException {
	    MetaRule rule = metaFunction.getRule();
	    MetaMetric metric = rule.getMetric();
	    Set<MetaWeight> weights = metric.getMetaWeights();
	    MetaWeight thisWeight = weights.iterator().next();
		
	    for (MetaWeight weight : weights) {
		MetaCondition nthCondition = new MetaCondition();
		nthCondition.setType(name());
		nthCondition.setName(weight.getName());

		//one weight at a time so we can measure it
		MetaRule newRule = new MetaRule();
		MetaMetric newMetric = new MetaMetric();
		newMetric.addWeight(weight);
		MetaPredicate nthPredicate = new MetaPredicate(newRule, nthCondition);
		PredicateHandler handler = nthPredicate.getHandler();
		boolean matched = handler.apply(cycObject, context, args);
		if (matched) {
		    thisWeight = weight;
		    break;
		}
	    }
	    return thisWeight;
	}
	
	abstract protected CycList applyMode(CycObject cycObject, CycFunctionHandler handler, CycQuery context, String[] args) throws IOException;
    }
    
}
