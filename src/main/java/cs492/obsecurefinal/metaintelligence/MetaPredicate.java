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

import cs492.obsecurefinal.obsecurecyc.CycPredicateHandler;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCondition;

/**
 *
 * @author Benjamin Arnold
 */
public class MetaPredicate implements MetaAction<PredicateHandler> {
    private final PredicateHandler handler;
    private final MetaCondition condition;
    private final String name;
    
    public MetaPredicate(MetaCondition condition) {
	this.condition = condition;
	name = condition.getType().toUpperCase();
	Handler wrapper = Handler.valueOf(name);
	this.handler = wrapper.getHandler(this);
    }

    @Override
    public PredicateHandler getHandler() {
	return handler;
    }
    
    @Override
    public String getName() {
	return name;
    }

    public MetaCondition getCondition() {
	return condition;
    }
    
    
    private static enum Handler implements HandlerAware<MetaPredicate, PredicateHandler> {
	ISA {
	    @Override
	    public PredicateHandler getHandler(MetaPredicate predicate) {
		return new CycPredicateHandler(predicate);
	    }

	},COMMENT {
	      @Override
	    public PredicateHandler getHandler(MetaPredicate predicate) {
		return new CycPredicateHandler(predicate);
	    }
	};
	
    }
    
}
