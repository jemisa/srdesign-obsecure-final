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

package cs492.obsecurefinal.spring.controller.metaintelligence;

import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleGraph;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaRuleGraphRepository;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Benjamin Arnold
 */
public class MetaRuleGraphFacade implements MetaIntelligenceCrudFacade<MetaRuleGraph> {
    private static final MetaRuleGraphRepository metaRuleGraphRepository = MetaIntelligenceFacade.getInstance().getMetaRuleGraphRepository();
    
    private static final MetaRuleGraphFacade instance = new MetaRuleGraphFacade();
    
    private MetaRuleGraphFacade() {
	//singleton
    }
    
    public static MetaRuleGraphFacade getInstance() {
	return instance;
    }
    
    @Override
    public MetaRuleGraph save(MetaRuleGraph entity) {
	
	return metaRuleGraphRepository.save(entity);
    }

    public Iterable<MetaRuleGraph> findAll() {
	return metaRuleGraphRepository.findAll();
    }
    
    public Iterable<MetaRuleGraph> findAll(String categoryName) {
	Long categoryId = MetaCategoryFacade.getInstance().findCategoryId(categoryName);
	Iterable<MetaRuleGraph> categoryGraphs = metaRuleGraphRepository.findAllByMetaCategoryId(categoryId);
	return categoryGraphs;
    }
    
    public Iterable<MetaRuleGraph> findHighestScoring(String categoryName) { 
	return findHighestScoring(categoryName, 1);
    }
    
    public Iterable<MetaRuleGraph> findHighestScoring(String categoryName, int epsilon) {
	Iterable<MetaRuleGraph> categoryGraphs = findAll(categoryName);
	return findHighestScoring(categoryGraphs, epsilon);
    }

    public Iterable<MetaRuleGraph> findHighestScoring(Iterable<MetaRuleGraph> categoryGraphs, int epsilon) {
	Set<MetaRuleGraph> highestScoring = new HashSet<>();
	int highestScore = findHighestScore(categoryGraphs, epsilon);
	for (MetaRuleGraph graph : categoryGraphs) {
	    if (graph.getScore() >= highestScore) {
		highestScoring.add(graph);
	    }
	}
	return highestScoring;
    }
    
    private int findHighestScore(Iterable<MetaRuleGraph> categoryGraphs, int epsilon) {
	int highest = Integer.MIN_VALUE;
	TreeSet<Integer> scores = new TreeSet<Integer>();
	
	for (MetaRuleGraph graph : categoryGraphs) {
	    scores.add(graph.getScore());	    
	}
	
	for (Iterator<Integer> it = scores.iterator(); it.hasNext() && epsilon > 0; epsilon--) {
	    highest = it.next();
	}
	return highest;
    }
}