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

import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCategory;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleSet;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaRuleSetRepository;

/**
 *
 * @author Benjamin Arnold
 */
public class MetaRuleSetFacade implements MetaIntelligenceCrudFacade<MetaRuleSet> {
    private static final MetaRuleSetRepository metaRuleSetRepository = MetaIntelligenceFacade.getInstance().getMetaRuleSetRepository();
    
    private static final MetaRuleSetFacade instance = new MetaRuleSetFacade();
    
    private MetaRuleSetFacade() {
	//singleton
    }
    
    public static MetaRuleSetFacade getInstance() {
	return instance;
    }
    
    @Override
    public MetaRuleSet save(MetaRuleSet entity) {
	for (MetaCategory category : entity.getMetaCategories()) {
	    MetaCategoryFacade.getInstance().save(category);
	}
	    
	return metaRuleSetRepository.save(entity);
    }
    
}
