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

package cs492.obsecurefinal.metaintelligence;

import cs492.obsecurefinal.obsecurecyc.CycQuery;
import java.io.IOException;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycObject;

/**
 *
 * @author Benjamin Arnold
 */
public interface FunctionHandler extends MetaHandler {
    
    public CycList invoke(CycObject object, CycQuery context, String[] args) throws IOException;
}
