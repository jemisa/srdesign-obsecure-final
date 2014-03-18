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
import org.opencyc.cycobject.CycConstant;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycObject;

/**
 *
 * @author Ben
 */
public class OccupationStrategy extends CycQueryStrategy {

    public OccupationStrategy(EntityTypes type) {
	super(type);
    }

    @Override
    public CycList exec(final CycAccess cycAccess, CycList constants) throws UnknownHostException, IOException {	
	CycQueryExecutor executor = new CycQueryExecutor() {
	    @Override
	    public CycList loop(CycObject cycObject) throws UnknownHostException, IOException {
		return cycAccess.getGenls(cycObject);
	    }
	    
	    @Override
	    public CycList filter(CycList input) throws IOException {
		return CycFilter.filter(cycAccess, input, CycFilter.HOMO_SAPIENS, CycFilter.HUMAN_INFANT);
	    }//Spares us from being reminded that a system administrator is "a human being who is not a babe" which is, quite frankly, insulting
	};
	CycList generalizations = executor.execute(constants);
	
	CycConstant professional = cycAccess.getKnownConstantByName("Professional");
	if (generalizations.contains(professional)) {
	    //TODO:  construct a <INDUSTRY> professional (need industry for this)
	}
	return generalizations;
    }
}