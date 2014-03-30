/*
 * Copyright (C) 2014 Rysn
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

package com.mycompany.obsecurefinal;

import cs492.obsecurefinal.common.EntityTypes;
import cs492.obsecurefinal.obsecurecyc.LocationStrategy;
import java.io.IOException;
import static junit.framework.Assert.*;
import org.junit.Test;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycList;
import org.opencyc.util.query.CycQuery;

/**
 *
 * @author Ryan Hersh
 */
public class CycTest 
{
    EntityTypes type;
    
    @Test
    public void LocationTest() throws IOException
    {
        LocationStrategy query = new LocationStrategy(type);
        CycList list = query.exec(CycAccess.sharedCycAccessInstance, CycList.EMPTY_CYC_LIST);
        assertTrue(list.isEmpty());
    }
}
