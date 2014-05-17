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

import cs492.obsecurefinal.spring.domain.metaintelligence.MetaMetric;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaWeight;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaWeightRepository;


/**
 *
 * @author Benjamin Arnold
 */
public class MetaWeightFacade implements MetaIntelligenceCrudFacade<MetaWeight> {
    private static final MetaWeightRepository metaWeightRepository = MetaIntelligenceFacade.getInstance().getMetaWeightRepository();
    
    private static final MetaWeightFacade instance = new MetaWeightFacade();
    
    private MetaWeightFacade() {
	//singleton
    }
    
    public static MetaWeightFacade getInstance() {
	return instance;
    }
    
    @Override
    public MetaWeight save(MetaWeight entity) {
	return metaWeightRepository.save(entity);
    }
    
}
