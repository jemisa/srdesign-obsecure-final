/*
 * Copyright (C) 2014 Benjamin Arnold
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
 * @author Benjamin Arnold
 */
public class MedicalStrategy extends CycQueryStrategy {

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
