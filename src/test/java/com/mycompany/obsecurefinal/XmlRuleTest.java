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
import cs492.obsecurefinal.metaintelligence.bean.MetaCategory;
import cs492.obsecurefinal.metaintelligence.bean.MetaCondition;
import cs492.obsecurefinal.metaintelligence.bean.MetaCriteria;
import cs492.obsecurefinal.metaintelligence.bean.MetaFilter;
import cs492.obsecurefinal.metaintelligence.bean.MetaMetric;
import cs492.obsecurefinal.metaintelligence.bean.MetaRule;
import cs492.obsecurefinal.metaintelligence.bean.MetaRuleSet;
import cs492.obsecurefinal.metaintelligence.bean.MetaWeight;
import cs492.obsecurefinal.metaintelligence.parsetree.RuleTreeComponent;
import cs492.obsecurefinal.metaintelligence.parsetree.RuleTreeComposite;
import cs492.obsecurefinal.metaintelligence.parsetree.RuleTreeLeaf;
import cs492.obsecurefinal.metaintelligence.parsetree.XmlDocument;
import cs492.obsecurefinal.metaintelligence.parsetree.XmlTag;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import static junit.framework.Assert.*;
import org.junit.Test;

/**
 *
 * @author Benjamin Arnold
 */
public class XmlRuleTest {
    private static final String document = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ruleSet name=\"opencyc\"><category name=\"LOCATION\"><rule name=\"namedLocation\" type=\"GENERALITY\"><filter type=\"adaptive\" value=\"3\">LocationFilter</filter><metric><weight value=\"1\">City</weight><weight value=\"2\">State-UnitedStates</weight><weight value=\"3\">IndependentCountry</weight><weight value=\"4\">Country</weight><weight value=\"5\">Continent</weight></metric><criteria><condition type=\"isa\">ANY</condition></criteria></rule><rule name=\"historic\" type=\"GENERALITY\"><filter type=\"adaptive\" value=\"3\">LocationFilter</filter><metric><weight value=\"1\">Historic</weight><weight value=\"2\">City</weight><weight value=\"3\">State-UnitedStates</weight><weight value=\"4\">IndependentCountry</weight><weight value=\"5\">Country</weight><weight value=\"6\">Continent</weight></metric><criteria><condition type=\"comment\">DATE</condition></criteria></rule></category></ruleSet>";
    
    @Test
    public void parseFilter() throws Exception {
	final String fString = "<filter type=\"adaptive\" value=\"3\">LocationFilter</filter>";
	RuleTreeComponent filter = XmlTag.FILTER.parse(fString);
	assertEquals("adaptive", filter.getType());
	assertEquals("3", filter.getValue());
	assertEquals("LocationFilter", filter.getName());
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
	    public String getDocumentContents() {
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
	List<RuleTreeComponent> categoryCildren = category.getChildren();
	assertEquals(2, categoryCildren.size());
	
	{
	RuleTreeComposite rule = (RuleTreeComposite) categoryCildren.get(0);
	assertEquals("namedLocation", rule.getName());
	List<RuleTreeComponent> ruleChildren = rule.getChildren();
	assertEquals(3, ruleChildren.size());
	
	RuleTreeLeaf filter = (RuleTreeLeaf) ruleChildren.get(0);
	assertEquals("LocationFilter", filter.getName());
	assertEquals("3", filter.getValue());
	
	RuleTreeComposite metric = (RuleTreeComposite) ruleChildren.get(1);
	List<RuleTreeComponent> metricChildren = metric.getChildren();
	assertEquals(5, metricChildren.size());
	
	Iterator<RuleTreeComponent> weightIterator = metricChildren.iterator();
	
	final Map<Integer, String> expectedWeights = new HashMap<>();
	expectedWeights.put(1, "City");
	expectedWeights.put(2,"State-UnitedStates");
	expectedWeights.put(3,"IndependentCountry");
	expectedWeights.put(4,"Country");
	expectedWeights.put(5,"Continent");
	
	while (weightIterator.hasNext()) {
	    RuleTreeComponent weight = weightIterator.next();
	    String wtVal = weight.getValue();
	    String expectedName = expectedWeights.get(Integer.valueOf(wtVal));
	    assertEquals(expectedName, weight.getName());
	}
	
	RuleTreeComposite criteria = (RuleTreeComposite) ruleChildren.get(2);
	List<RuleTreeComponent> criteriaChildren = criteria.getChildren();
	assertEquals(1, criteriaChildren.size());
	
	RuleTreeLeaf condition = (RuleTreeLeaf) criteriaChildren.get(0);
	assertEquals("ANY", condition.getName());
	assertEquals("isa", condition.getType());
	}
	
	{
	RuleTreeComposite rule = (RuleTreeComposite) categoryCildren.get(1);
	assertEquals("historic", rule.getName());
	List<RuleTreeComponent> ruleChildren = rule.getChildren();
	assertEquals(3, ruleChildren.size());
	
	RuleTreeLeaf filter = (RuleTreeLeaf) ruleChildren.get(0);
	assertEquals("LocationFilter", filter.getName());
	assertEquals("3", filter.getValue());
	
	RuleTreeComposite metric = (RuleTreeComposite) ruleChildren.get(1);
	List<RuleTreeComponent> metricChildren = metric.getChildren();
	assertEquals(6, metricChildren.size());
	
	Iterator<RuleTreeComponent> weightIterator = metricChildren.iterator();
	
	final Map<Integer, String> expectedWeights = new HashMap<>();
	expectedWeights.put(1, "Historic");
	expectedWeights.put(2, "City");
	expectedWeights.put(3,"State-UnitedStates");
	expectedWeights.put(4,"IndependentCountry");
	expectedWeights.put(5,"Country");
	expectedWeights.put(6,"Continent");
	
	while (weightIterator.hasNext()) {
	    RuleTreeComponent weight = weightIterator.next();
	    String wtVal = weight.getValue();
	    String expectedName = expectedWeights.get(Integer.valueOf(wtVal));
	    assertEquals(expectedName, weight.getName());
	}
	
	RuleTreeComposite criteria = (RuleTreeComposite) ruleChildren.get(2);
	List<RuleTreeComponent> criteriaChildren = criteria.getChildren();
	assertEquals(1, criteriaChildren.size());
	
	RuleTreeLeaf condition = (RuleTreeLeaf) criteriaChildren.get(0);
	assertEquals("DATE", condition.getName());
	assertEquals("comment", condition.getType());
	}
    }
    
   
    
    @Test
    public void loadMetaRules() throws Exception {
	XmlLoader loader = new XmlLoader() {
	    @Override
	    public String getDocumentContents() {
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
	
	{
	MetaRule rule = metaRuleIterator.next();
	assertEquals("namedLocation", rule.getName());
	
	MetaFilter filter = rule.getFilter();
	assertEquals("LocationFilter", filter.getName());
	assertEquals(Integer.valueOf(3), filter.getValue());
	
	MetaMetric metric = rule.getMetric();
	Set<MetaWeight> weights = metric.getMetaWeights();
	assertEquals(5, weights.size());
	
	Iterator<MetaWeight> weightIterator = weights.iterator();
	
	final Map<Integer, String> expectedWeights = new HashMap<>();
	expectedWeights.put(1, "City");
	expectedWeights.put(2,"State-UnitedStates");
	expectedWeights.put(3,"IndependentCountry");
	expectedWeights.put(4,"Country");
	expectedWeights.put(5,"Continent");
	
	while (weightIterator.hasNext()) {
	    MetaWeight weight = weightIterator.next();
	    Integer wtVal = weight.getValue();
	    String expectedName = expectedWeights.get(wtVal);
	    assertEquals(expectedName, weight.getName());
	}
	
	MetaCriteria criteria = rule.getCriteria();
	Set<MetaCondition> conditions = criteria.getMetaConditions();
	assertEquals(1, conditions.size());
	
	MetaCondition condition = conditions.iterator().next();
	assertEquals("ANY", condition.getName());
	assertEquals("isa", condition.getType());
	}
	
	{
	MetaRule rule = metaRuleIterator.next();
	assertEquals("historic", rule.getName());
	
	MetaFilter filter = rule.getFilter();
	assertEquals("LocationFilter", filter.getName());
	assertEquals(Integer.valueOf(3), filter.getValue());
	
	MetaMetric metric = rule.getMetric();
	Set<MetaWeight> weights = metric.getMetaWeights();
	assertEquals(6, weights.size());
	
	Iterator<MetaWeight> weightIterator = weights.iterator();
	
	final Map<Integer, String> expectedWeights = new HashMap<>();
	expectedWeights.put(1, "Historic");
	expectedWeights.put(2, "City");
	expectedWeights.put(3,"State-UnitedStates");
	expectedWeights.put(4,"IndependentCountry");
	expectedWeights.put(5,"Country");
	expectedWeights.put(6,"Continent");
	
	while (weightIterator.hasNext()) {
	    MetaWeight weight = weightIterator.next();
	    Integer wtVal = weight.getValue();
	    String expectedName = expectedWeights.get(wtVal);
	    assertEquals(expectedName, weight.getName());
	}
	
	MetaCriteria criteria = rule.getCriteria();
	Set<MetaCondition> conditions = criteria.getMetaConditions();
	assertEquals(1, conditions.size());
	
	MetaCondition condition = conditions.iterator().next();
	assertEquals("DATE", condition.getName());
	assertEquals("comment", condition.getType());
	}
    }
}
