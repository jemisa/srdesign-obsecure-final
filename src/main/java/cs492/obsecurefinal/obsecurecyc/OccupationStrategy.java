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
public class OccupationStrategy extends CycQueryStrategy {

    @Override
    public CycList exec(CycAccess cycAccess, String text) throws UnknownHostException, IOException {
	CycList constants = getAllCycConstantsContainingText(cycAccess, text);
	if (constants.size() == 0 && text.contains(" ")) {
	    constants = getAllCycConstantsContainingText(cycAccess, text.replace(" ", ""));
	}
	if (constants.size() == 0) {
	    return constants;
	}
	
	CycObject cycObject = (CycObject) constants.get(0);
	CycList generalizations = cycAccess.getGenls(cycObject);
	
	CycConstant homoSapien = cycAccess.getKnownConstantByName("HomoSapiens");
	CycConstant infant = cycAccess.getKnownConstantByName("HumanInfant");
	filter(generalizations, homoSapien, infant); //Spares us from being reminded that a system administrator is "a human being who is not a babe" which is, quite frankly, insulting
	
	CycConstant professional = cycAccess.getKnownConstantByName("Professional");
	if (generalizations.contains(professional)) {
	    //TODO:  construct a <INDUSTRY> professional (need industry for this)
	}
	return generalizations;
    }
}