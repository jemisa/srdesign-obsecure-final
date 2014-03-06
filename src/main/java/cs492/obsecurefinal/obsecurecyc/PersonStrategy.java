/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import java.io.IOException;
import java.net.UnknownHostException;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycList;

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