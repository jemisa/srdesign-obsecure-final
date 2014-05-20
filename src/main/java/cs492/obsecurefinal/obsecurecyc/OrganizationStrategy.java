/*
 * Copyright (C) 2014 Drexel University
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

import cs492.obsecurefinal.common.EntityTypes;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycObject;

/**
 *
 * @author Benjamin Arnold
 */
public class OrganizationStrategy extends CycQueryStrategy {
    
    OrganizationStrategy(EntityTypes type, String microtheory) {
	super(type, microtheory);
    }
    
    @Override
    public CycList exec(final CycAccess cycAccess, CycList constants) throws UnknownHostException, IOException {
	CycQueryExecutor executor = new CycQueryExecutor() {
	    @Override
	    public CycList loop(CycObject cycObject) throws UnknownHostException, IOException {
		return cycAccess.getIsas(cycObject);
	    }
	    
   	    @Override
	    public CycList filter(CycList input) throws IOException {
		return CycFilter.filter(cycAccess, input, Arrays.asList(new CycFilter[] {CycFilter.INDIVIDUAL}));
	    }
	};
	
	CycList result = executor.execute(constants);
	return result;
    }
}
