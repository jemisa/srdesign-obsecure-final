/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import cs492.obsecurefinal.obsecurecyc.opencyc.api.CycAccess;
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycList;
import java.io.IOException;

/**
 *
 * @author Ben
 */
public class CycQuery {
    
    private final CycQueryStrategy strategy;
    private final CycAccess cycAccess;
    
    public CycQuery(CycQueryStrategy strategy, CycAccess cycAccess) {
	this.strategy = strategy;
	this.cycAccess = cycAccess;
    }
    
    public CycList execute(String text) throws IOException {
	return strategy.exec(cycAccess, text);
    }
    
}
