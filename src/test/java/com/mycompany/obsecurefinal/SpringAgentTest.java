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

import cs492.obsecurefinal.spring.SpringModel;
import cs492.obsecurefinal.spring.domain.common.SpringAgent;
import cs492.obsecurefinal.spring.session.common.SpringAgentRepository;
import java.util.Arrays;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
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
public class SpringAgentTest {
    
    @Autowired
    SpringAgentRepository repository;
    
    @Test
    public void create() {
	final String name = "testAgentName";
	final String occupation = "testAgentOccupation";
	final String location = "testAgentLocation";
	final String workplace = "testAgentWorkplace";
	
	Long id;
	{
	    SpringAgent agent = new SpringAgent();
	    agent.setName(name);
	    agent.setLocation(location);
	    agent.setOccupation(occupation);
	    agent.setWorkplace(workplace);
	    
	    SpringAgent created = repository.save(agent);
	    id = created.getId();
	}
	
	SpringAgent agent = repository.findOne(id);
	assertEquals(name, agent.getName());
	assertEquals(occupation, agent.getOccupation());
	assertEquals(location, agent.getLocation());
	assertEquals(workplace, agent.getWorkplace());
	assertNotNull(agent.getUpdatedTime());
    }
    
    @Test
    public void findByName() {
	final String name = "testAgentWaldoName";
	final String occupation = "testAgentWaldoOccupation";
	final String location = "testAgentWaldoLocation";
	final String workplace = "testAgentWaldoWorkplace";
	
	{
	    SpringAgent agent = new SpringAgent();
	    agent.setName(name);
	    agent.setLocation(location);
	    agent.setOccupation(occupation);
	    agent.setWorkplace(workplace);
	    
	    repository.save(agent);
	}
	
	SpringAgent agent = repository.findByName(name);
	assertEquals(name, agent.getName());
	assertEquals(occupation, agent.getOccupation());
	assertEquals(location, agent.getLocation());
	assertEquals(workplace, agent.getWorkplace());
	assertNotNull(agent.getUpdatedTime());
    }
    
    @Test
    public void findAll() {
	
	final String nameZ = "testAgentZeroName";
	final String occupationZ = "testAgentZeroOccupation";
	final String locationZ = "testAgentZeroLocation";
	final String workplaceZ = "testAgentZeroWorkplace";
	
	{
	    SpringAgent agent = new SpringAgent();
	    agent.setName(nameZ);
	    agent.setLocation(locationZ);
	    agent.setOccupation(occupationZ);
	    agent.setWorkplace(workplaceZ);
	    
	    repository.save(agent);
	}
	
	final String name1 = "testAgentOneName";
	final String occupation1 = "testAgentOneOccupation";
	final String location1 = "testAgentOneLocation";
	final String workplace1 = "testAgentOneWorkplace";
	{
	    SpringAgent agent = new SpringAgent();
	    agent.setName(name1);
	    agent.setLocation(location1);
	    agent.setOccupation(occupation1);
	    agent.setWorkplace(workplace1);
	    
	    repository.save(agent);
	}
	
	
	Iterable<SpringAgent> all = repository.findAll();
	String[][] expectedAgents = new String[][] {
	    {nameZ,occupationZ,locationZ,workplaceZ},
	    {name1,occupation1,location1,workplace1}
	};
	
	
	int totalScore = 0;
	int totalAgents = 0;
	
	MatchScore matchScore = new MatchScore(expectedAgents);
	
	for (SpringAgent agent: all) {
	    List<Predicate> predicates = generatePredicates(agent);
	    totalScore += matchScore.score(predicates);
	    totalAgents++;
	}
	assertTrue(totalAgents >= 2);
	assertEquals(8, totalScore);
    }
    
    private List<Predicate> generatePredicates(final SpringAgent agent) {
	Predicate nameMatch = new Predicate() {
	    @Override
	    public boolean apply(String expectedValue) {
		return expectedValue.equals(agent.getName());
	    }

	};
	Predicate occupationMatch = new Predicate() {
	    @Override
	    public boolean apply(String expectedValue) {
		return expectedValue.equals(agent.getOccupation());
	    }

	};
	Predicate locationMatch = new Predicate() {
	    @Override
	    public boolean apply(String expectedValue) {
		return expectedValue.equals(agent.getLocation());
	    }

	};
	Predicate workplaceMatch = new Predicate() {
	    @Override
	    public boolean apply(String expectedValue) {
		return expectedValue.equals(agent.getWorkplace());
	    }

	};
	return Arrays.asList(new Predicate[]{nameMatch,occupationMatch,locationMatch,workplaceMatch});
	
    }    
}
