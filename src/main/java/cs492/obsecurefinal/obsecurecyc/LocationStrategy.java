/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycConstant;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycObject;
import org.opencyc.cycobject.CycVariable;

/**
 *
 * @author Ben
 */
public class LocationStrategy extends CycQueryStrategy {
    
    @Override
    public CycList exec(CycAccess cycAccess, String text) throws UnknownHostException, IOException {
	CycList constants = getAllCycConstantsContainingText(cycAccess, text);
	CycObject cycObject = null;
	for (Iterator it = constants.iterator(); it.hasNext();) {
	    cycObject = (CycObject) it.next();
	    String val = cycObject.cycListApiValue().toString();
	    if (Location.CITY.matches(val) || Location.STATE.matches(val)) {
		break;
	    }
	}
	CycList result = null;
	if (cycObject != null) {
	    CycList query = CycQueryBuilder.makeQuery(cycAccess, cycObject, CycQueryBuilder.QueryType.BROADER);
	    CycVariable variable = CycQueryBuilder.makeVariable();
	    CycConstant mtGeo = cycAccess.getConstantByName(MICROTHEORY_US_GEOGRAPHY);
	    result = cycAccess.queryVariable(query, variable, mtGeo);
	}
	
	return result;
    }

    
    private static enum Location {

	CITY("City"), STATE("State");

	private String matchString;
	
	private Location(String matchString) {
	    this.matchString = matchString;
	}
	
	boolean matches(String s) { //presumably this is more efficient than quering ($%isa #$s #$city)
	    return (s != null && s.contains(matchString));
	}
    }
}
