/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import cs492.obsecurefinal.common.EntityTypes;

/**
 *
 * @author Ben
 */
public class ObSecureCycStrategyFactory {
    
    public static CycQueryStrategy lookupStrategy(EntityTypes entityType) {
	CycQueryStrategy strategy;
	
	if (EntityTypes.COMPANY == entityType) {
	    strategy = new OrganizationStrategy(entityType);
	} else if (EntityTypes.LOCATION == entityType) {
	    strategy = new LocationStrategy(entityType);
	} else if (EntityTypes.OCCUPATION == entityType) {
	    strategy = new OccupationStrategy(entityType);
	} else if (EntityTypes.MEDICAL == entityType) {
	    strategy = new MedicalStrategy(entityType);
	} else {
	    strategy = new PersonStrategy(entityType);
	}
	
	return strategy;
    }
}