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

package cs492.obsecurefinal.obsecurecyc;

import cs492.obsecurefinal.common.EntityTypes;

/**
 * 
 * @author Benjamin Arnold
 */
public class ObSecureCycStrategyFactory {
    
    public static CycQueryStrategy lookupStrategy(EntityTypes entityType) {
	CycQueryStrategy strategy;
	//TODO: we might want to consider externalizing these mt's
	if (EntityTypes.COMPANY == entityType) {
	    strategy = new OrganizationStrategy(entityType, CycQueryStrategy.MICROTHEORY_UNIVERSAL_VOCABULARY); 
	} else if (EntityTypes.LOCATION == entityType) {
	    strategy = new LocationStrategy(entityType, CycQueryStrategy.MICROTHEORY_US_GEOGRAPHY);
	} else if (EntityTypes.OCCUPATION == entityType) {
	    strategy = new OccupationStrategy(entityType, CycQueryStrategy.MICROTHEORY_UNIVERSAL_VOCABULARY);
	} else if (EntityTypes.MEDICAL == entityType) {
	    strategy = new MedicalStrategy(entityType, CycQueryStrategy.MICROTHEORY_HUMAN_AILMENT);
	} else if (EntityTypes.DRUG_ALCOHOL == entityType) {
	    strategy = new DrugAlcoholStrategy(entityType, CycQueryStrategy.MICROTHEORY_UNIVERSAL_VOCABULARY);   
	} else if (EntityTypes.EMOTION == entityType) {
	    strategy = new EmotionStrategy(entityType, CycQueryStrategy.MICROTHEORY_UNIVERSAL_VOCABULARY);   
	} else if (EntityTypes.PERSONAL == entityType) {
	    strategy = new PersonalStrategy(entityType, CycQueryStrategy.MICROTHEORY_UNIVERSAL_VOCABULARY);   
	} else if (EntityTypes.STEREOTYPING == entityType) {
	    strategy = new StereotypeStrategy(entityType, CycQueryStrategy.MICROTHEORY_ENGLISH);   
	} else if (EntityTypes.FAMILY == entityType) {
	    strategy = new FamilyStrategy(entityType, CycQueryStrategy.MICROTHEORY_UNIVERSAL_VOCABULARY);   
	} else {
	    strategy = new PersonStrategy(entityType, CycQueryStrategy.MICROTHEORY_MASS_MEDIA);
	}
	
	return strategy;
    }
}