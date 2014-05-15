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

import cs492.obsecurefinal.spring.controller.metaintelligence.MetaIntelligenceFacade;
import cs492.obsecurefinal.spring.SpringModel;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCategory;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleGraph;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleSet;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaRuleGraphRepository;
import java.util.Iterator;
import static junit.framework.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
	Iterable<MetaRuleGraph> ruleGraphScores = facade.findAllMetaRuleGraphs();
	MetaRuleGraph graph;
	
	for (Iterator<MetaRuleGraph> it = ruleGraphScores.iterator(); it.hasNext(); graph = it.next()) {
	    actualCount++;
	}
	
	assertEquals(expectedRuleCount, actualCount);
    }
    
    @Test
    public void update() throws Exception {
	final int updatedScore = 12;
	Iterable<MetaRuleGraph> ruleGraphScores = facade.findAllMetaRuleGraphs();
	MetaRuleGraph graph;
	
	for (Iterator<MetaRuleGraph> it = ruleGraphScores.iterator(); it.hasNext();) {
	    graph = it.next();
	    graph.setScore(updatedScore);
	    facade.save(graph);
	}
	
	ruleGraphScores = facade.findAllMetaRuleGraphs();
	
	for (Iterator<MetaRuleGraph> it = ruleGraphScores.iterator(); it.hasNext();) {
	    graph = it.next();
	    assertEquals(updatedScore, graph.getScore().intValue());
	}
	
    }
}
