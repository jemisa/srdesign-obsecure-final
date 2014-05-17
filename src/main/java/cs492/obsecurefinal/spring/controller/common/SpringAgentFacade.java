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

package cs492.obsecurefinal.spring.controller.common;

import cs492.obsecurefinal.spring.SpringBeanFactory;
import cs492.obsecurefinal.spring.domain.common.SpringAgent;
import cs492.obsecurefinal.spring.session.common.SpringAgentRepository;
/**
 *
 * @author Benjamin Arnold
 */
public class SpringAgentFacade {
    private static final SpringBeanFactory springModel = SpringBeanFactory.getInstance();
    private static final SpringAgentFacade instance = new SpringAgentFacade();
    
   
    private final SpringAgentRepository springAgentRepository;
    
    private SpringAgentFacade() {
	//singleton
	springAgentRepository = springModel.getSession(SpringAgentRepository.class);
    }
    
    public static SpringAgentFacade getInstance() {
	return instance;
    }
    
    public SpringAgent save(SpringAgent agent) {
	return springAgentRepository.save(agent);
    }
    
    public SpringAgent read(Long id) {
	return springAgentRepository.findOne(id);
    }
    
    public Iterable<SpringAgent> findAll() {
	return springAgentRepository.findAll();
    }

}
