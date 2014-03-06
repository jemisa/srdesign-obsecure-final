/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

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
public class OrganizationStrategy extends CycQueryStrategy {

    @Override
    public CycList exec(CycAccess cycAccess, String text) throws UnknownHostException, IOException {
	CycList constants = getAllCycConstantsWithPrefix(cycAccess, text);
	CycObject cycObject = (CycObject) constants.get(0);
	
	CycList isAs = cycAccess.getIsas(cycObject);
	CycConstant  individual = cycAccess.getKnownConstantByName("Individual");
	filter(isAs, individual);
	return isAs;
    }
}
