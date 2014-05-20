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

import cs492.obsecurefinal.metaintelligence.XmlLoader;
import cs492.obsecurefinal.metaintelligence.parsetree.RuleTreeComponent;
import cs492.obsecurefinal.metaintelligence.parsetree.RuleTreeComposite;
import cs492.obsecurefinal.metaintelligence.parsetree.RuleTreeLeaf;
import cs492.obsecurefinal.metaintelligence.parsetree.XmlDocument;
import cs492.obsecurefinal.metaintelligence.parsetree.XmlTag;
import cs492.obsecurefinal.spring.domain.common.SpringAgent;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaAction;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCategory;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCondition;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCriteria;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaFilter;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaMetric;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRule;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleSet;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaWeight;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import static junit.framework.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Benjamin Arnold
 */
public class XmlRuleTest {
    private static final String document = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ruleSet name=\"opencyc\"><category name=\"LOCATION\"><rule name=\"namedLocation\" type=\"GENERALITY\"><filter type=\"adaptive\" value=\"3\">LocationFilter</filter><action type=\"isa\" value=\"1\">AUTO</action><metric><weight value=\"1\">City</weight><weight value=\"2\">State-UnitedStates</weight><weight value=\"3\">IndependentCountry</weight><weight value=\"4\">Country</weight><weight value=\"5\">Continent</weight></metric><criteria><condition type=\"isa\">ANY</condition></criteria></rule><rule name=\"historic\" type=\"GENERALITY\"><filter type=\"adaptive\" value=\"3\">LocationFilter</filter><action type=\"isa\" value=\"1\">AUTO</action><metric><weight value=\"1\">Historic</weight><weight value=\"2\">City</weight><weight value=\"3\">State-UnitedStates</weight><weight value=\"4\">IndependentCountry</weight><weight value=\"5\">Country</weight><weight value=\"6\">Continent</weight></metric><criteria><condition type=\"comment\">DATE</condition></criteria></rule></category></ruleSet>";
    
    @Test
    public void parseFilter() throws Exception {
	final String fString = "<filter type=\"adaptive\" value=\"3\">LocationFilter</filter>";
	RuleTreeComponent filter = XmlTag.FILTER.parse(fString);
	assertEquals("adaptive", filter.getType());
	assertEquals("3", filter.getValue());
	assertEquals("LocationFilter", filter.getName());
    }
    
    @Test
    public void parseAction() throws Exception {
	final String aString = "<action type=\"isa\" value=\"1\">AUTO</action>";
	RuleTreeComponent filter = XmlTag.ACTION.parse(aString);
	assertEquals("isa", filter.getType());
	assertEquals("1", filter.getValue());
	assertEquals("AUTO", filter.getName());
    }
    
    @Test
    public void parseCondition() throws Exception {
	final String fString = "<condition type=\"isa\">ANY</condition>";
	RuleTreeComponent filter = XmlTag.CONDITION.parse(fString);
	assertEquals("isa", filter.getType());
	assertEquals("ANY", filter.getName());
    }
    
    @Test
    public void parseTwoRuleSet() throws Exception {
	XmlLoader loader = new XmlLoader() {
	    @Override
	    public String getDocumentContents(String path) {
		return document;
	    }
	};
	
	XmlDocument xmlDoc = loader.getDocument();
	
	RuleTreeComposite root = (RuleTreeComposite) xmlDoc.createParseTree();
	assertEquals("opencyc", root.getName());
	List<RuleTreeComponent> children = root.getChildren();
	assertEquals(1, children.size());
	
	RuleTreeComposite category = (RuleTreeComposite) children.get(0);
	assertEquals("LOCATION", category.getName());
	final List<RuleTreeComponent> categoryChildren = category.getChildren();
	assertEquals(2, categoryChildren.size());
	
	final Map<Integer, String> expectedWeights = new HashMap<>();
	expectedWeights.put(1, "City");
	expectedWeights.put(2,"State-UnitedStates");
	expectedWeights.put(3,"IndependentCountry");
	expectedWeights.put(4,"Country");
	expectedWeights.put(5,"Continent");
	
	final Map<Integer, String> expectedWeightsB = new HashMap<>();
	expectedWeightsB.put(1, "Historic");
	expectedWeightsB.put(2, "City");
	expectedWeightsB.put(3,"State-UnitedStates");
	expectedWeightsB.put(4,"IndependentCountry");
	expectedWeightsB.put(5,"Country");
	expectedWeightsB.put(6,"Continent");
	
	Object[][] expectedAgents = new Object[][] {
	    {new Object[]{"namedLocation","4"},	new Object[]{"LocationFilter","3"},new Object[]{"AUTO","1","isa"},"5",expectedWeights,"1",new Object[]{"ANY","isa"}},
	    {new Object[]{"historic","4"},	new Object[]{"LocationFilter","3"},new Object[]{"AUTO","1","isa"},"6",expectedWeightsB,"1",new Object[]{"DATE","comment"}}
	};
	
	int score = 0;
	int predicateSize = 0;
	MatchScore matchScore = new MatchScore(expectedAgents);
	for (int i = 0; i < categoryChildren.size(); i++) {
	    RuleTreeComposite rule = (RuleTreeComposite) categoryChildren.get(i);
	    List<RuleTreeComponent> ruleChildren = rule.getChildren();
	    RuleTreeComposite metric = (RuleTreeComposite) ruleChildren.get(2);
	    List<RuleTreeComponent> metricChildren = metric.getChildren();
	    RuleTreeComposite criteria = (RuleTreeComposite) ruleChildren.get(3);
	    List<RuleTreeComponent> criteriaChildren = criteria.getChildren();

	    List<Predicate> predicates = generatePredicates(rule,ruleChildren, metricChildren,criteriaChildren); 
	    score += matchScore.score(predicates);
	    predicateSize = predicates.size();
	}
	assertEquals(2*predicateSize,score);

    }


private List<Predicate> generatePredicates(final RuleTreeComposite rule,final List<RuleTreeComponent> ruleChildren,final List<RuleTreeComponent> metricChildren,final List<RuleTreeComponent> criteriaChildren) {
	Predicate rulePredicate = new Predicate() {
	    @Override
	    public boolean apply(Object expectedValue) {
		Object[] expected = (Object[]) expectedValue;
		boolean matched = expected[0].equals(rule.getName());
		if (matched) {
		    matched = Integer.parseInt((String)expected[1]) == ruleChildren.size();
		}
		
		return matched;
	    }
	};

	Predicate filterPredicate = new Predicate() {
	    @Override
	     public boolean apply(Object expectedValue) {
		Object[] expected = (Object[]) expectedValue;
		RuleTreeLeaf filter = (RuleTreeLeaf) ruleChildren.get(0);
		boolean matched = expected[0].equals(filter.getName());
		if (matched) {
		    matched = expected[1].equals(filter.getValue());
		}
		return matched;
	    }
	};

	Predicate actionPredicate = new Predicate() {
	    @Override
	     public boolean apply(Object expectedValue) {
		Object[] expected = (Object[]) expectedValue;
		RuleTreeLeaf action = (RuleTreeLeaf) ruleChildren.get(1);
		boolean matched = expected[0].equals(action.getName());
		if (matched) {
		    matched = expected[1].equals(action.getValue());
		}
		if (matched) {
		    matched = expected[2].equals(action.getType());
		}
		return matched;
	    }
	};


	Predicate metricPredicate = new Predicate() {
	    @Override
	     public boolean apply(Object expectedValue) {
		boolean matched = Integer.parseInt((String)expectedValue) == metricChildren.size();
		return matched;
	     }
	};

	Predicate weightPredicate = new Predicate() {
	    @Override
	     public boolean apply(Object expectedValue) {
		Map<Integer, String> expectedWeights = (Map<Integer, String>) expectedValue;
		Iterator<RuleTreeComponent> weightIterator = metricChildren.iterator();
		boolean matched = true;
		while (weightIterator.hasNext() && matched) {
		    RuleTreeComponent weight = weightIterator.next();
		    String wtVal = weight.getValue();
		    String expectedName = expectedWeights.get(Integer.valueOf(wtVal));
		    matched = expectedName.equals(weight.getName());
		}
		return matched;
	    }
	};

	Predicate criteriaPredicates = new Predicate() {
	    @Override
	    public boolean apply(Object expectedValue) {
		boolean matched = Integer.parseInt((String) expectedValue) == criteriaChildren.size();
		return matched;
	    }
	};

	Predicate conditionPredicates = new Predicate() {
	    @Override
	    public boolean apply(Object expectedValue) {
		Object[] expected = (Object[]) expectedValue;
		RuleTreeLeaf condition = (RuleTreeLeaf) criteriaChildren.get(0);
		boolean matched = expected[0].equals(condition.getName());
		if (matched) {
		    matched = expected[1].equals(condition.getType());
		}
		return matched;
	    }
	};

	return Arrays.asList(new Predicate[]{rulePredicate,filterPredicate,actionPredicate,metricPredicate,weightPredicate,criteriaPredicates,conditionPredicates});
    }
    
    @Test
    public void loadMetaRules() throws Exception {
	XmlLoader loader = new XmlLoader() {
	    @Override
	    public String getDocumentContents(String path) {
		return document;
	    }
	};
	
	MetaRuleSet ruleSet = loader.loadRules();
	assertEquals("opencyc", ruleSet.getName());
	Set<MetaCategory> children = ruleSet.getMetaCategories();
	assertEquals(1, children.size());
	
	MetaCategory category = children.iterator().next();
	assertEquals("LOCATION", category.getName());
	Set<MetaRule> categoryChildren = category.getMetaRules();
	assertEquals(2, categoryChildren.size());
	Iterator<MetaRule> metaRuleIterator = categoryChildren.iterator();
	
	final Map<Integer, String> expectedWeights = new HashMap<>();
	expectedWeights.put(1, "City");
	expectedWeights.put(2,"State-UnitedStates");
	expectedWeights.put(3,"IndependentCountry");
	expectedWeights.put(4,"Country");
	expectedWeights.put(5,"Continent");
	
	final Map<Integer, String> expectedWeightsB = new HashMap<>();
	expectedWeightsB.put(1, "Historic");
	expectedWeightsB.put(2, "City");
	expectedWeightsB.put(3,"State-UnitedStates");
	expectedWeightsB.put(4,"IndependentCountry");
	expectedWeightsB.put(5,"Country");
	expectedWeightsB.put(6,"Continent");
	
	Object[][] expectedAgents = new Object[][] {
	    {new Object[]{"namedLocation","4"},	new Object[]{"LocationFilter","3"},new Object[]{"AUTO","1","isa"},"5",expectedWeights,"1",new Object[]{"ANY","isa"}},
	    {new Object[]{"historic","4"},	new Object[]{"LocationFilter","3"},new Object[]{"AUTO","1","isa"},"6",expectedWeightsB,"1",new Object[]{"DATE","comment"}}
	};
	
	int score = 0;
	int predicateSize = 0;
	MatchScore matchScore = new MatchScore(expectedAgents);
	
	while (metaRuleIterator.hasNext()) {
	    MetaRule rule = metaRuleIterator.next();
	    MetaFilter filter = rule.getFilter();
	    MetaAction action = rule.getAction();
	    MetaMetric metric = rule.getMetric();
	    Set<MetaWeight> weights = metric.getMetaWeights();

	    MetaCriteria criteria = rule.getCriteria();
	    Set<MetaCondition> conditions = criteria.getMetaConditions();
	    MetaCondition condition = conditions.iterator().next();
	    List<Predicate> predicates = generateMetaPredicates(rule, filter, action, metric, weights, criteria, conditions, condition);
	    score += matchScore.score(predicates);
	    predicateSize = predicates.size();
	}
	assertEquals(2*predicateSize,score);
	
    }
    
    private List<Predicate> generateMetaPredicates(final MetaRule rule, final MetaFilter filter, final MetaAction action, final MetaMetric metric, final Set<MetaWeight> weights, final MetaCriteria criteria, final Set<MetaCondition> conditions, final MetaCondition condition) {
	Predicate rulePredicate = new Predicate() {
	    @Override
	    public boolean apply(Object expectedValue) {
		Object[] expected = (Object[]) expectedValue;
		boolean matched = expected[0].equals(rule.getName());
		
		return matched;
	    }
	};

	Predicate filterPredicate = new Predicate() {
	    @Override
	     public boolean apply(Object expectedValue) {
		Object[] expected = (Object[]) expectedValue;
		boolean matched = expected[0].equals(filter.getName());
		if (matched) {
		    matched = Integer.valueOf((String)expected[1]).equals(filter.getValue());
		}
		return matched;
	    }
	};

	Predicate actionPredicate = new Predicate() {
	    @Override
	     public boolean apply(Object expectedValue) {
		Object[] expected = (Object[]) expectedValue;
		boolean matched = expected[0].equals(action.getName());
		if (matched) {
		    matched = Integer.valueOf((String)expected[1]).equals(action.getValue());
		}
		if (matched) {
		    matched = expected[2].equals(action.getType());
		}
		return matched;
	    }
	};


	Predicate metricPredicate = new Predicate() {
	    @Override
	     public boolean apply(Object expectedValue) {
		return true;
	     }
	};

	Predicate weightPredicate = new Predicate() {
	    @Override
	     public boolean apply(Object expectedValue) {
		Map<Integer, String> expectedWeights = (Map<Integer, String>) expectedValue;
		Iterator<MetaWeight> weightIterator = weights.iterator();
		boolean matched = true;
		while (weightIterator.hasNext() && matched) {
		    MetaWeight weight = weightIterator.next();
		    Integer wtVal = weight.getValue();
		    matched = wtVal <= expectedWeights.size();
		    if (matched) {
			 String expectedName = expectedWeights.get(wtVal);
			matched = expectedName.equals(weight.getName());
		    }
		   
		}
		return matched;
	    }
	};

	Predicate criteriaPredicates = new Predicate() {
	    @Override
	    public boolean apply(Object expectedValue) {
		return true;
	    }
	};

	Predicate conditionPredicates = new Predicate() {
	    @Override
	    public boolean apply(Object expectedValue) {
		Object[] expected = (Object[]) expectedValue;
		boolean matched = expected[0].equals(condition.getName());
		if (matched) {
		    matched = expected[1].equals(condition.getType());
		}
		return matched;
	    }
	};

	return Arrays.asList(new Predicate[]{rulePredicate,filterPredicate,actionPredicate,metricPredicate,weightPredicate,criteriaPredicates,conditionPredicates});
    }
    
    @Test
    public void testNoOverrides() throws Exception {
	XmlLoader loader = new XmlLoader();
	XmlDocument document = loader.getDocument();
	assertFalse("A document should be loaded even if there are no overrides", document.isEmpty());
    }
}
