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

package cs492.obsecurefinal.metaintelligence;

import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRule;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleSet;
import cs492.obsecurefinal.common.EntityTypes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Benjamin Arnold
 */
public class IntelligenceGraph {
    private final Map<EntityTypes, NavigableMap<Integer, HashSet<Long>>> graph = new HashMap<>();   //EntityType => score => {RuleIds}
    
    private static final Long START_ID = 100000L;
    private Long counter = START_ID;
    private final NavigableMap<Long, String> txns = new TreeMap<>();
    private final NavigableMap<Long, List<String>> txnsResults = new TreeMap<>();
    private final NavigableMap<Long, EntityTypes> txnTypes = new TreeMap<>();
    private final NavigableMap<Long, MetaRule> txnRules = new TreeMap<>();
    
    private static final IntelligenceGraph instance = new IntelligenceGraph();
    
    private IntelligenceGraph() {
	//singleton
    }
    
    public static IntelligenceGraph getInstance() {
	return instance;
    }
    
    public synchronized IntelligenceGraph load() {
	//FIXME
	return this;
    }
    
    public synchronized IntelligenceGraph set(MetaRuleSet ruleSet) {
	graph.clear();  //Hibernate will remove the entities
	//TODO: load new
	return this;
    }
    
    public List<MetaRule> getTopNRules(EntityTypes category, int n) {
	List<MetaRule> topRules = new ArrayList<>();
//	MetaRuleFacade facade = MetaServiceDelagateLookup.getMetaService(MetaRule.class);
//	
//	NavigableMap<Integer, HashSet<Long>> categoryRules = graph.get(category);
//	Iterator<Integer> descendingSetIterator = categoryRules.descendingKeySet().descendingIterator();
//	while (topRules.size() < n && descendingSetIterator.hasNext()) {
//	    Integer next = descendingSetIterator.next();
//	    HashSet<Long> ids = categoryRules.get(next);
//	    Iterator<Long> idIterator = ids.iterator();
//	    
//	    while (topRules.size() < n && idIterator.hasNext()) {
//		MetaRule metaRule = facade.read(MetaRule.class, idIterator.next());
//		topRules.add(metaRule);
//	    }
//	}
	return topRules;
    }
    
    public void resolve(String original, String result) {
	Set<Long> txnIds = findMatchingTxns(original);
	for (Long txnId : txnIds) {
	    List<String> results = txnsResults.get(txnId);
	    EntityTypes types = txnTypes.get(txnId);
	    MetaRule ruleUsed = txnRules.get(txnId);
	    if (results.contains(result)) {
		
	    }
	    
	    
	}
    }
    
    private Set<Long> findMatchingTxns(String target) {
	Set<Long> txnIds = new HashSet<>();
	for (Long id : txns.navigableKeySet()) {
	    String set = txns.get(id);
	    if (target.equals(set)) {
		txnIds.add(id);
	    }
	}
	return txnIds;
    }
    
    public Long mark(String originalText, EntityTypes type, List<String> results, MetaRule ruleUsed ) {
	Long txnId;
	synchronized(this) {
	    txnId =counter;
	    counter += 1L;
	}
	txns.put(txnId, originalText);
	txnsResults.put(txnId, results);
	txnTypes.put(txnId, type);
	txnRules.put(txnId, ruleUsed);
	return txnId;
    }

    public Long getCounter() {
	return counter;
    }
    
}
