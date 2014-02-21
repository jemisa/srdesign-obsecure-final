/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import java.io.IOException;
import java.net.UnknownHostException;
import cs492.obsecurefinal.obsecurecyc.opencyc.api.CycAccess;
import cs492.obsecurefinal.obsecurecyc.opencyc.api.CycObjectFactory;
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycList;
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycObject;
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycSymbol;

/**
 *
 * @author Ben
 */
public abstract class CycQueryStrategy {
    private final CycSymbol CONSTANT_COMPLETE = CycObjectFactory.makeCycSymbol("CONSTANT-COMPLETE");
    
    public abstract CycList exec(CycAccess cycAccess, String text) throws UnknownHostException, IOException;
    
    protected CycObject getCycConstantForText(CycAccess cycAccess, String text) throws IOException {
	final CycList constants = cycAccess.converseList(CycList.makeCycList(CONSTANT_COMPLETE,text));
	CycObject cycObject = (CycObject) constants.get(0);
	return cycObject;
    }
    
}
