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

package com.mycompany.obsecurefinal;

import cs492.obsecurefinal.common.EntityTypes;
import cs492.obsecurefinal.spring.controller.metaintelligence.MetaCategoryFacade;
import cs492.obsecurefinal.spring.controller.metaintelligence.MetaIntelligenceFacade;
import cs492.obsecurefinal.spring.controller.metaintelligence.MetaRuleGraphFacade;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCategory;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleGraph;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleSet;
import java.util.Iterator;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * 
 * @author Benjamin Arnold
 */
public class MetaRuleGraphTest {
    private static final MetaIntelligenceFacade facade = MetaIntelligenceFacade.getInstance();
    private static int expectedRuleCount;
    
    @BeforeClass
    public static void setup() throws Exception {
	MetaRuleSet metaRuleSet = facade.loadRules();
	expectedRuleCount = 0;
	for (MetaCategory category : metaRuleSet.getMetaCategories()) {
	    expectedRuleCount += category.getMetaRules().size();
	}
    }
    
    @Test
    public void initialization() throws Exception {
	int actualCount = 0;
	MetaRuleGraphFacade metaRuleGraphFacade = MetaRuleGraphFacade.getInstance();
	Iterable<MetaRuleGraph> ruleGraphScores = metaRuleGraphFacade.findAll();
	MetaRuleGraph graph;
	
	for (Iterator<MetaRuleGraph> it = ruleGraphScores.iterator(); it.hasNext(); graph = it.next()) {
	    actualCount++;
	}
	
	assertEquals(expectedRuleCount, actualCount);
    }
    
    @Test
    public void update() throws Exception {
	final int updatedScore = 12;
	MetaRuleGraphFacade metaRuleGraphFacade = MetaRuleGraphFacade.getInstance();
	Iterable<MetaRuleGraph> ruleGraphScores = metaRuleGraphFacade.findAll();
	MetaRuleGraph graph;
	
	for (Iterator<MetaRuleGraph> it = ruleGraphScores.iterator(); it.hasNext();) {
	    graph = it.next();
	    graph.setScore(updatedScore);
	    facade.save(graph);
	}
	
	ruleGraphScores = metaRuleGraphFacade.findAll();
	
	for (Iterator<MetaRuleGraph> it = ruleGraphScores.iterator(); it.hasNext();) {
	    graph = it.next();
	    assertEquals(updatedScore, graph.getScore().intValue());
	}
	
    }
    
    @Test
    public void byCategory() throws Exception {
	Long categoryId = MetaCategoryFacade.getInstance().findCategoryId(EntityTypes.LOCATION.name());
	assertNotNull(categoryId);
	
	Iterable<MetaRuleGraph> graphs = MetaRuleGraphFacade.getInstance().findHighestScoring(EntityTypes.LOCATION.name());
	assertNotNull(graphs);
	
	for (MetaRuleGraph graph : graphs) {
	    assertEquals(categoryId, graph.getMetaCategoryId());
	}
	
    }
}
