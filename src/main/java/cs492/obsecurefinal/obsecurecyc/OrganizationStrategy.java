/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import cs492.obsecurefinal.common.EntityTypes;
import java.io.IOException;
import java.net.UnknownHostException;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycObject;

/**
 *
 * @author Ben
 */
public class OrganizationStrategy extends CycQueryStrategy {
    
    OrganizationStrategy(EntityTypes type) {
	super(type);
    }
    
    @Override
    public CycList exec(final CycAccess cycAccess, CycList constants) throws UnknownHostException, IOException {
	CycQueryExecutor executor = new CycQueryExecutor() {
	    @Override
	    public CycList loop(CycObject cycObject) throws UnknownHostException, IOException {
		return cycAccess.getIsas(cycObject);
	    }
	    
   	    @Override
	    public CycList filter(CycList input) throws IOException {
		return CycFilter.filter(cycAccess, input, CycFilter.INDIVIDUAL);
	    }
	};
	
	CycList result = executor.execute(constants);
	return result;
    }
}
