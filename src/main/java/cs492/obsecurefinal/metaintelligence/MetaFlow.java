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

import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.spring.controller.metaintelligence.MetaRuleFacade;
import cs492.obsecurefinal.spring.controller.metaintelligence.MetaRuleGraphFacade;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCondition;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCriteria;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRule;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleGraph;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Benjamin Arnold
 */
public class MetaFlow {
    private final NamedEntity entity;
    private final Map<String, Object> cache = new HashMap<>();
    
    public MetaFlow(NamedEntity entity) {
	this.entity = entity;
    }
    
    
    public void start(Iterable<MetaRuleGraph> categoryGraphs) {
	
	Long txnId = IntelligenceGraph.getInstance().startTransaction();
	String[] args = new String[]{};
	
	Iterable<MetaRuleGraph> bestGraphs = getBestGraphs(categoryGraphs);
	
	for (MetaRuleGraph graph : bestGraphs) {
	     MetaPredicate metaPredicate = getMetaPredicate(graph);
	     PredicateHandler predicateHandler = metaPredicate.getHandler();
	     if (predicateHandler.applies(this)) {
		 
	     }
	    
	} 
    }
    
    private Iterable<MetaRuleGraph> getBestGraphs(Iterable<MetaRuleGraph> categoryGraphs) {
	Set<MetaRuleGraph> vettedGraphs = vetGraphsByCriteria(categoryGraphs);
	Iterable<MetaRuleGraph> bestGraphs = null;
	
	Long aRuleId = vettedGraphs.iterator().next().getMetaRuleId();
	MetaRule aRule = MetaRuleFacade.getInstance().read(aRuleId);
	int firstEpsilon = MetaRuleFacade.getInstance().getEpisolon(aRule);
	
	for (int epsilon = firstEpsilon; bestGraphs == null || bestGraphs.iterator().hasNext(); epsilon++) {
	    bestGraphs = MetaRuleGraphFacade.getInstance().findHighestScoring(vettedGraphs, epsilon);
	}
	return bestGraphs;
    }
    
    private MetaPredicate getMetaPredicate(MetaRuleGraph graph) {
	Long ruleId = graph.getMetaRuleId();
	MetaRule rule = MetaRuleFacade.getInstance().read(ruleId);

	MetaCriteria criteria = rule.getCriteria();
	Set<MetaCondition> conditions = criteria.getMetaConditions();
	MetaCondition primaryCondition = conditions.iterator().next();

	return new MetaPredicate(rule, primaryCondition);
    }
    
    private Set<MetaRuleGraph> vetGraphsByCriteria(Iterable<MetaRuleGraph> categoryGraphs) {
	Set<MetaRuleGraph> vettedGraphs = new HashSet<>();
	
	for (MetaRuleGraph graph : categoryGraphs) {
	    MetaPredicate metaPredicate = getMetaPredicate(graph);
	    PredicateHandler predicateHandler = metaPredicate.getHandler();
	    
	    if (predicateHandler.applies(this)) {
		vettedGraphs.add(graph);
	    }
	}
	return vettedGraphs;
    }
    
    public NamedEntity getEntity() {
	return entity;
    }
    
    public void cache(String key, Object o) {
	cache.put(key, o);
    }
    
    public Object retrieveFromCache(String key) {
	return cache.get(key);
    }    
    
}
