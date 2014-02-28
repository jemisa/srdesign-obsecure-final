/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import cs492.obsecurefinal.obsecurecyc.opencyc.api.CycAccess;
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycList;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 *
 * @author Ben
 */
public class PersonStrategy extends CycQueryStrategy {

    @Override
    public CycList exec(CycAccess cycAccess, String text) throws UnknownHostException, IOException {
	return cycAccess.getGenls(getAllCycConstantsContainingText(cycAccess, text));
    }
}