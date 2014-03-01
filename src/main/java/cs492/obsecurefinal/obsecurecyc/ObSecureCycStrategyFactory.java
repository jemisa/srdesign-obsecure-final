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
	CycQueryStrategy strategy = null;
	
	if (EntityTypes.COMPANY == entityType) {
	    strategy = new LocationStrategy();
	} else if (EntityTypes.LOCATION == entityType) {
	    strategy = new OccupationStrategy();
	} else if (EntityTypes.OCCUPATION == entityType) {
	    strategy = new OrganizationStrategy();
	} else {
	    strategy = new PersonStrategy();
	}
	
	return strategy;
    }
}