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

import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRule;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaRuleRepository;

/**
 *
 * @author Benjamin Arnold
 */
public class MetaRuleFacade implements MetaIntelligenceCrudFacade<MetaRule> {
    private static final MetaRuleRepository metaRuleRepository = MetaIntelligenceFacade.getInstance().getMetaRuleRepository();
    
    private static final MetaRuleFacade instance = new MetaRuleFacade();
    
    private MetaRuleFacade() {
	//singleton
    }
    
    public static MetaRuleFacade getInstance() {
	return instance;
    }
    
    @Override
    public MetaRule save(MetaRule entity) {
	MetaMetricFacade.getInstance().save(entity.getMetric());
	MetaCriteriaFacade.getInstance().save(entity.getCriteria());
	MetaFilterFacade.getInstance().save(entity.getFilter());
	MetaActionFacade.getInstance().save(entity.getAction());
	return metaRuleRepository.save(entity);
    }

    public MetaRule read(Long ruleId) {
	return metaRuleRepository.findOne(ruleId);
    }
    
    public int getEpisolon(MetaRule rule) {
	return MetaFilterFacade.getInstance().getEpsilon(rule.getFilter());
    }
}
