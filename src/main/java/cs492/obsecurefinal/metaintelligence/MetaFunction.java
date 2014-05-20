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

import cs492.obsecurefinal.obsecurecyc.CycFunctionHandler;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaAction;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRule;

/**
 *
 * @author Benjamin Arnold
 */
public class MetaFunction implements MetaActionHandlerAware<FunctionHandler> {
    private final String name;
    private final MetaRule rule;
    private final MetaAction action;
    private final FunctionHandler handler;

    public MetaFunction(MetaRule rule, MetaAction action) {
	this.name = action.getType().toUpperCase();
	this.rule = rule;
	this.action = action;
	Handler type = Handler.valueOf(name);
	this.handler = type.getHandler(this);
    }
    
    @Override
    public FunctionHandler getHandler() {
	return handler;
    }

    @Override
    public String getName() {
	return name;
    }
    
    public MetaAction getAction() {
	return action;
    }
    
    public MetaRule getRule() {
	return rule;
    }

   private static enum Handler implements HandlerAware<MetaFunction, FunctionHandler> {
	ISA {
	    @Override
	    public FunctionHandler getHandler(MetaFunction metaFunction) {
		return new CycFunctionHandler(metaFunction);
	    }

	};
   }
    
}
