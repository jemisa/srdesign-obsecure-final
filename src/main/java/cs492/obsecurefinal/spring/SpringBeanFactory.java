/*
 * Copyright (C) 2014 Pivotal Software, Inc.
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

package cs492.obsecurefinal.spring;

import cs492.obsecurefinal.App;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaCategoryRepository;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaFilterRepository;
import cs492.obsecurefinal.spring.SpringModel;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Benjamin Arnold
 */
public class SpringBeanFactory {
    private static final SpringBeanFactory instance = new SpringBeanFactory();
    private final ConfigurableApplicationContext context;
    
    private SpringBeanFactory() {
	context = SpringApplication.run(SpringModel.class);
    }
    
    public static SpringBeanFactory getInstance() {
	return instance;
    }
    
    public <T extends CrudRepository> T getSession(Class<? extends CrudRepository> klass) {
	return (T) context.getBean(klass);
    }
     
}
