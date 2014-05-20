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

import cs492.obsecurefinal.spring.domain.metaintelligence.MetaAction;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCategory;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRule;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaActionRepository;

/**
 *
 * @author Benjamin Arnold
 */
public class MetaActionFacade implements MetaIntelligenceCrudFacade<MetaAction> {
    private static final MetaActionRepository metaActionRepository = MetaIntelligenceFacade.getInstance().getMetaActionRepository();
    
    private static final MetaActionFacade instance = new MetaActionFacade();
    
    private MetaActionFacade() {
	//singleton
    }
    
    public static final MetaActionFacade getInstance() {
	return instance;
    }
    
    @Override
    public MetaAction save(MetaAction entity) {
	return metaActionRepository.save(entity);
    }
    
}
