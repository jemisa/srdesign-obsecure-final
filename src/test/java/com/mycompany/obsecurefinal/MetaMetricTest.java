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

import cs492.obsecurefinal.spring.SpringModel;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaMetric;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaWeight;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaMetricRepository;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaWeightRepository;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Benjamin Arnold
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringModel.class)
public class MetaMetricTest {
    @Autowired
    MetaWeightRepository weightRepository;
    @Autowired
    MetaMetricRepository metricRepository;
    
    @Test
    public void create() throws Exception {
	final String name = "createTestWeight";
	final String name2 = "createTestWeight2";
	final Integer value = 5;
	final Integer value2 = 25; 
	Long metricId = null;
	{
	    MetaWeight weight = new MetaWeight();
	    weight.setName(name);
	    weight.setValue(value);
	    MetaWeight createdWeight = weightRepository.save(weight);
	    
	    MetaWeight weight2 = new MetaWeight();
	    weight2.setName(name2);
	    weight2.setValue(value2);
	    MetaWeight createdWeight2 = weightRepository.save(weight2);
	    
	    MetaMetric metric = new MetaMetric();
	    metric.addWeight(createdWeight);
	    metric.addWeight(createdWeight2);
	    
	    MetaMetric created = metricRepository.save(metric);
	    metricId = created.getId();
	}
	
	MetaMetric metric = metricRepository.findOne(metricId);
	assertNotNull(metric.getUpdatedTime());
	Set<MetaWeight> weights = metric.getMetaWeights();
	assertEquals(2, weights.size());
	
	String[][] expectedAgents = new String[][] {
	    {name,value.toString()},
	    {name2,value2.toString()}
	};
	
	MatchScore matchScore = new MatchScore(expectedAgents);
	int totalScore = 0;	
	Iterator<MetaWeight> it = weights.iterator();
	while(it.hasNext()) {
	    MetaWeight weight = it.next();
	    List<Predicate> predicates = generatePredicates(weight);
	    totalScore += matchScore.score(predicates);
	}
	
	assertEquals(4,totalScore);
    }
    
    private List<Predicate> generatePredicates(final MetaWeight weight) {
	Predicate nameMatch = new Predicate() {
	    @Override
	    public boolean apply(String expectedValue) {
		return expectedValue.equals(weight.getName());
	    }

	};
	Predicate valueMatch = new Predicate() {
	    @Override
	    public boolean apply(String expectedValue) {
		return expectedValue.equals(weight.getValue().toString());
	    }

	};
	return Arrays.asList(new Predicate[]{nameMatch,valueMatch});
    }    
}
