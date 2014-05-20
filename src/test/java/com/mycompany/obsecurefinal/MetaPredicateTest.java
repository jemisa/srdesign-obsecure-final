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
import cs492.obsecurefinal.metaintelligence.MetaIntelligenceFramework;
import cs492.obsecurefinal.metaintelligence.MetaPredicate;
import cs492.obsecurefinal.metaintelligence.PredicateHandler;
import cs492.obsecurefinal.obsecurecyc.CycPredicateHandler;
import cs492.obsecurefinal.obsecurecyc.CycQuery;
import cs492.obsecurefinal.obsecurecyc.CycQueryStrategy;
import cs492.obsecurefinal.obsecurecyc.ObSecureCycFacade;
import cs492.obsecurefinal.obsecurecyc.ObSecureCycStrategyFactory;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCondition;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaMetric;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRule;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaWeight;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycObject;

/**
 *
 * @author Benjamin Arnold
 */
public class MetaPredicateTest {
    
    @Test
    public void isa() throws Exception {
	MetaCondition metaCondition = new MetaCondition();
	metaCondition.setType("isa");
	metaCondition.setName("City");
	
	MetaRule rule = new MetaRule();
	
	MetaPredicate metaPredicate = new MetaPredicate(rule, metaCondition);
	PredicateHandler handler = metaPredicate.getHandler();
	CycAccess cycAccess = ObSecureCycFacade.getInstance().getCycAccess();
	CycQueryStrategy strategy = ObSecureCycStrategyFactory.lookupStrategy(EntityTypes.LOCATION);
	CycQuery context = new CycQuery(strategy, cycAccess);
	CycList matches = strategy.cycConstantAutoCompleteExact(cycAccess, "CityOfPhiladelphiaPA");
	
	boolean test = handler.apply((CycObject) matches.get(0), context, new String[]{});
	assertTrue("The city of Philadelphia is a city",test);
    }
    
    @Test
    public void notIsa() throws Exception {
	MetaCondition metaCondition = new MetaCondition();
	metaCondition.setType("isa");
	metaCondition.setName("City");
	MetaRule rule = new MetaRule();
	
	MetaPredicate metaPredicate = new MetaPredicate(rule, metaCondition);
	PredicateHandler handler = metaPredicate.getHandler();
	CycAccess cycAccess = ObSecureCycFacade.getInstance().getCycAccess();
	CycQueryStrategy strategy = ObSecureCycStrategyFactory.lookupStrategy(EntityTypes.LOCATION);
	CycQuery context = new CycQuery(strategy, cycAccess);
	CycList matches = strategy.cycConstantAutoCompleteExact(cycAccess, "PhiladelphiaEagles");
	
	boolean test = handler.apply((CycObject) matches.get(0), context, new String[]{});
	assertFalse("The Philadelphia Eagles are not a city",test);
    }
    
    @Test
    public void commentHistoric() throws Exception {
	MetaCondition metaCondition = new MetaCondition();
	metaCondition.setType("comment");
	metaCondition.setName("DATE");
	MetaRule rule = new MetaRule();
	MetaPredicate metaPredicate = new MetaPredicate(rule, metaCondition);
	PredicateHandler handler = metaPredicate.getHandler();
	CycAccess cycAccess = ObSecureCycFacade.getInstance().getCycAccess();
	CycQueryStrategy strategy = ObSecureCycStrategyFactory.lookupStrategy(EntityTypes.LOCATION);
	CycQuery context = new CycQuery(strategy, cycAccess);
	CycList matches = strategy.cycConstantAutoCompleteExact(cycAccess, "LibertyBell");
	
	boolean test = handler.apply((CycObject) matches.get(0), context, new String[]{});
	assertTrue("The Liberty Bell has historic significance",test);
    }
    
    @Test
    public void commentNotHistoric() throws Exception {
	MetaCondition metaCondition = new MetaCondition();
	metaCondition.setType("comment");
	metaCondition.setName("DATE");
	
	MetaRule rule = new MetaRule();
	MetaPredicate metaPredicate = new MetaPredicate(rule, metaCondition);
	PredicateHandler handler = metaPredicate.getHandler();
	CycAccess cycAccess = ObSecureCycFacade.getInstance().getCycAccess();
	CycQueryStrategy strategy = ObSecureCycStrategyFactory.lookupStrategy(EntityTypes.LOCATION);
	CycQuery context = new CycQuery(strategy, cycAccess);
	CycList matches = strategy.cycConstantAutoCompleteExact(cycAccess, "Kangaroo");
	
	boolean test = handler.apply((CycObject) matches.get(0), context, new String[]{});
	assertFalse("A Kangaroo has no historic significance",test);
    }
    
    @Test
    public void isaAny() throws Exception {
	MetaCondition metaCondition = new MetaCondition();
	metaCondition.setType("isa");
	metaCondition.setName("ANY");
	
	MetaRule rule = new MetaRule();
	
	MetaMetric metric = new MetaMetric();
	MetaWeight city = new MetaWeight();
	city.setName("City");
	city.setValue(2);
	metric.addWeight(city);
	
	MetaWeight eagles = new MetaWeight();
	eagles.setName("FootballTeam");
	city.setValue(1);
	metric.addWeight(eagles);
	rule.setMetric(metric);
	
	MetaPredicate metaPredicate = new MetaPredicate(rule, metaCondition);
	PredicateHandler handler = metaPredicate.getHandler();
	CycAccess cycAccess = ObSecureCycFacade.getInstance().getCycAccess();
	CycQueryStrategy strategy = ObSecureCycStrategyFactory.lookupStrategy(EntityTypes.LOCATION);
	CycQuery context = new CycQuery(strategy, cycAccess);
	CycList matches = strategy.cycConstantAutoCompleteExact(cycAccess, "PhiladelphiaEagles");
	
	boolean test = handler.apply((CycObject) matches.get(0), context, new String[]{});
	assertTrue("ANY on PhiadelphiaEagles should match a FootballTeam",test);
    }
    
    @BeforeClass
    public static void setup() throws Exception {
        MetaIntelligenceFramework.registerService(PredicateHandler.SERVICE_NAME, new CycPredicateHandler());
    }
    
    
}
