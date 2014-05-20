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
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycObject;

/**
 *
 * @author Benjamin Arnold
 */
public abstract class CycQueryExecutor {
    
    public CycList execute(CycList constants) throws IOException {
	CycList result = new CycList();
	for (int i = 0; i < constants.size(); i++) {
	    CycList tResult = loop((CycObject) constants.get(i));
	    result.addAll(tResult);
	}
	return filter(result);
    }
    
    public CycList execute(CycObject constant) throws IOException {
	CycList result = loop(constant);
	return filter(result);
    }
    
    public CycList filter(CycList input) throws IOException {
	return input;  //override to imnplement
    }
    
    public abstract CycList loop(CycObject constant) throws UnknownHostException, IOException;
}
