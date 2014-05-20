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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Benjamin Arnold
 */
public class MetaIntelligenceFramework {
    private static final Map<String, MetaHandler> services = new HashMap<>();
    
    public static void registerService(String serviceName, MetaHandler service) {
	services.put(serviceName, service);
    }
    
    public static MetaHandler getService(String serviceName) {
	return services.get(serviceName);
    }
    
    /* convenience methods */
    public static PredicateHandler getPredicateHandler() {
	return (PredicateHandler) getService(PredicateHandler.SERVICE_NAME).newInstance();
    }
    
     public static FunctionHandler getFunctionHandler() {
	return (FunctionHandler) getService(FunctionHandler.SERVICE_NAME).newInstance();
    }
}
