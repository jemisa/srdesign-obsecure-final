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

package cs492.obsecurefinal.spring.controller.metaintelligence;

import cs492.obsecurefinal.common.GeneralizationResult;
import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.generalization.*;
import cs492.obsecurefinal.metaintelligence.XmlLoader;
import cs492.obsecurefinal.spring.SpringBeanFactory;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCategory;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRule;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleGraph;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleSet;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaActionRepository;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaCategoryRepository;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaConditionRepository;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaCriteriaRepository;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaFilterRepository;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaMetricRepository;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaRuleGraphRepository;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaRuleRepository;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaRuleSetRepository;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaWeightRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Benjamin Arnold
 */
public class MetaIntelligenceFacade implements GeneralizationFacade {
    private static final SpringBeanFactory springModel = SpringBeanFactory.getInstance();
    private static final MetaIntelligenceFacade instance = new MetaIntelligenceFacade();
    
   
    private final MetaRuleSetRepository metaRuleSetRepository;
    private final MetaCategoryRepository metaCategoryRepository;
    private final MetaRuleRepository metaRuleRepository;
    private final MetaMetricRepository metaMetricRepository;
    private final MetaFilterRepository metaFilterRepository;
    private final MetaConditionRepository metaConditionRepository;
    private final MetaCriteriaRepository metaCriteriaRepository;
    private final MetaWeightRepository metaWeightRepository;
    private final MetaRuleGraphRepository metaRuleGraphRepository;
    private final MetaActionRepository metaActionRepository;
    
    
    private MetaIntelligenceFacade() {
	//singleton
	metaRuleSetRepository = springModel.getSession(MetaRuleSetRepository.class);
	metaCategoryRepository = springModel.getSession(MetaCategoryRepository.class);
	metaRuleRepository = springModel.getSession(MetaRuleRepository.class);
	metaMetricRepository = springModel.getSession(MetaMetricRepository.class);
	metaFilterRepository = springModel.getSession(MetaFilterRepository.class);
	metaConditionRepository = springModel.getSession(MetaConditionRepository.class);
	metaCriteriaRepository = springModel.getSession(MetaCriteriaRepository.class);
	metaWeightRepository = springModel.getSession(MetaWeightRepository.class);
	metaRuleGraphRepository = springModel.getSession(MetaRuleGraphRepository.class);
	metaActionRepository = springModel.getSession(MetaActionRepository.class);
    }
    
    public static MetaIntelligenceFacade getInstance() {
	return instance;
    }
    
    public MetaRuleSetRepository getMetaRuleSetRepository() {
	return metaRuleSetRepository;
    }
    
     public MetaCategoryRepository getMetaCategoryRepository() {
	return metaCategoryRepository;
    }

    public MetaRuleRepository getMetaRuleRepository() {
	return metaRuleRepository;
    }
    
    public MetaMetricRepository getMetaMetricRepository() {
	return metaMetricRepository;
    }
    
    public MetaWeightRepository getMetaWeightRepository() {
	return metaWeightRepository;
    }
    
    public MetaCriteriaRepository getMetaCriteriaRepository() {
	return metaCriteriaRepository;
    }
    
    public MetaFilterRepository getMetaFilterRepository() {
	return metaFilterRepository;
    }
    
    public MetaRuleGraphRepository getMetaRuleGraphRepository() {
	return metaRuleGraphRepository;
    }
    
    public MetaConditionRepository getMetaConditionRepository() {
	return metaConditionRepository;
    }
    
     public MetaActionRepository getMetaActionRepository() {
	return metaActionRepository;
    }
    
     
    @Override
    public Map<NamedEntity, GeneralizationResult> generalize(List<NamedEntity> sensitiveEntities) {
	Map<NamedEntity, GeneralizationResult> results = new TreeMap<>();
	for (NamedEntity entity : sensitiveEntities) {
	    GeneralizationResult result = new GeneralizationResult();
	    
	    Iterable<MetaRuleSet> ruleSet = metaRuleSetRepository.findAll();
	    MetaRuleSet metaRuleSet;
	    if (!ruleSet.iterator().hasNext()) {
		metaRuleSet = loadRules();	
	    } else {
		metaRuleSet = ruleSet.iterator().next();
	    }
	    
	    MetaRuleGraphFacade metaRuleGraphFacade = MetaRuleGraphFacade.getInstance();
	    Iterable<MetaRuleGraph> highestGraphs = metaRuleGraphFacade.findHighestScoring(entity.getType().name());
	   
//	    for (MetaRule rule: highestGraphs) {
//		
//	    }
	}
	return results;
    }

    public MetaRuleSet loadRules() {
	MetaRuleSet metaRuleSet =  null;
	XmlLoader loader = new XmlLoader();
	try {
	    metaRuleSet = loader.loadRules();
	    MetaRuleSet created = MetaRuleSetFacade.getInstance().save(metaRuleSet);
	    initializeScores(created);
	} catch (IOException ex) {
	    Logger.getLogger(MetaIntelligenceFacade.class.getName()).log(Level.SEVERE, null, ex);
	}
	return metaRuleSet;
    }

    private void initializeScores(MetaRuleSet created) {
	for (MetaCategory category : created.getMetaCategories()) {
	    Long categoryId = category.getId();
	    for (MetaRule rule : category.getMetaRules()) {
		Long ruleId = rule.getId();
		MetaRuleGraph metaRuleGraph = new MetaRuleGraph();
		metaRuleGraph.setMetaCategoryId(categoryId);
		metaRuleGraph.setMetaRuleId(ruleId);
		metaRuleGraph.setScore(0);
		MetaRuleGraphFacade.getInstance().save(metaRuleGraph);
	    }
	}
    }

   

    public MetaRuleGraph save(MetaRuleGraph graph) {
	Long id = metaRuleGraphRepository.save(graph).getId();
	return metaRuleGraphRepository.findOne(id);
    }

    
}
