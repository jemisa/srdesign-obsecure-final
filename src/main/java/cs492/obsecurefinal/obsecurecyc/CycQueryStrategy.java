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
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycSymbol;

/**
 *
 * @author Ben
 */
public abstract class CycQueryStrategy {
    protected final CycSymbol CONSTANT_APROPOS = CycObjectFactory.makeCycSymbol("CONSTANT-APROPOS");
    protected final CycSymbol CONSTANT_COMPLETE = CycObjectFactory.makeCycSymbol("CONSTANT-COMPLETE");
    protected final String MICROTHEORY_US_GEOGRAPHY = "UnitedStatesGeographyMt";
    protected final String MICROTHEORY_INFERENCE_PSC = "InferencePsc";
    
    public abstract CycList exec(CycAccess cycAccess, String text) throws UnknownHostException, IOException;
    
    protected CycList getAllCycConstantsContainingText(CycAccess cycAccess, String text) throws IOException {
	final CycList constants = cycAccess.converseList(CycList.makeCycList(CONSTANT_APROPOS,text));
	return constants;
    }
    
}
