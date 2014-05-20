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
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycList;

/**
 *
 * @author Benjamin Arnold
 */
class StereotypeStrategy extends CycQueryStrategy {

    protected StereotypeStrategy(EntityTypes entityType, String microtheory) {
	super(entityType, microtheory);
    }

    @Override
    public CycList exec(CycAccess cycAccess, CycList constants) throws UnknownHostException, IOException {
	return null;
    }
    
}
