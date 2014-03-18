/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import cs492.obsecurefinal.common.EntityTypes;
import static cs492.obsecurefinal.obsecurecyc.CycQueryStrategy.MICROTHEORY_US_GEOGRAPHY;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
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

    public LocationStrategy(EntityTypes type) {
	super(type);
    }
    
    @Override
    protected CycList getConstants(CycAccess cycAccess, Iterator<String> order) throws IOException {
	CycList constants = null;
	while (order.hasNext() && (constants == null || constants.isEmpty())) {
	    String next = order.next();
	    constants = cycConstantAutoCompleteContains(cycAccess, next);
	}
	return constants;
    }
	    
    
    @Override
    public CycList exec(final CycAccess cycAccess, CycList constants) throws UnknownHostException, IOException {
	CycList matches = new CycList();
	for (Iterator it = constants.iterator(); it.hasNext();) {
	    CycObject cycObject = (CycObject) it.next();
	    String val = cycObject.cycListApiValue().toString();
	    if (Location.CITY.matches(val) || Location.STATE.matches(val)) {
		matches.add(cycObject);
	    }
	}

	CycQueryExecutor executor = new CycQueryExecutor() {
	    @Override
	    public CycList loop(CycObject cycObject) throws UnknownHostException, IOException {
		CycList query = CycQueryBuilder.makeQuery(cycAccess, cycObject, CycQueryBuilder.QueryType.BROADER);
		CycVariable variable = CycQueryBuilder.makeVariable();
		CycConstant mtGeo = cycAccess.getConstantByName(MICROTHEORY_US_GEOGRAPHY);
		return cycAccess.queryVariable(query, variable, mtGeo);
	    }
	};
	
    	CycList result = executor.execute(matches);
	return result;
    }
    
    private static enum Location {
	CITY, STATE;
	
	boolean matches(String s) { //presumably this is more efficient than quering ($%isa #$s #$city)
	    String name = StringUtils.capitalize(name().toLowerCase());
	    return (s != null && s.contains(name));
	}
    }
}
