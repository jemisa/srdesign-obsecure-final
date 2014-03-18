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

package cs492.obsecurefinal.generalization;

import cs492.obsecurefinal.common.EntityTypes;
import cs492.obsecurefinal.common.GeneralizationResult;
import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.obsecurecyc.ObSecureCycFacade;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Benjamin Arnold
 */
public class GeneralizationManager {
    
    public static Map<NamedEntity, GeneralizationResult> generalize(List<NamedEntity> entities) throws Exception {
	Map<NamedEntity, GeneralizationResult> results = new TreeMap<>();
	
	augment(results, GeneralizationMethod.CYC, entities);
	augment(results, GeneralizationMethod.TEMPLATE, entities);
	
	return results;
    }
    
    private static void augment(Map<NamedEntity, GeneralizationResult> results, GeneralizationMethod method, List<NamedEntity> entities) throws Exception {
	List<NamedEntity> filtered = method.filter(entities);
	Map<NamedEntity, GeneralizationResult> newResults = method.getFacade().generalize(filtered);
	results.putAll(newResults);
    }
 
    
    private static enum GeneralizationMethod {
	CYC(EntityTypes.LOCATION, EntityTypes.OCCUPATION, EntityTypes.COMPANY, EntityTypes.MEDICAL) {
	    @Override
	    GeneralizationFacade getFacade() throws Exception {
		return ObSecureCycFacade.getInstance();
	    }
	}, //MEDICAL is in both because individual symptons must be manually revices whereas diseases can be generalized
	TEMPLATE(EntityTypes.MEDICAL, EntityTypes.DRUG_ALCOHOL, EntityTypes.EMOTION, EntityTypes.PERSONAL_ATTACKS, EntityTypes.STEREOTYPING, EntityTypes.FAMILY) {
	    @Override
	    GeneralizationFacade getFacade() throws Exception {
		return TemplateFacade.getInstance();
	    }
	};
	
	private final List types;
	
	private GeneralizationMethod(EntityTypes... types) {
	    this.types = Arrays.asList(types);
	}
	
	private List<NamedEntity> filter(List<NamedEntity> entities) {
	    List<NamedEntity> filtered = new ArrayList<>();
	    for (NamedEntity ent : entities) {
		if (types.contains(ent.getType())) {
		    filtered.add(ent);
		}
	    }
	    return filtered;
	}
	
	abstract GeneralizationFacade getFacade() throws Exception;
    }
    
}




