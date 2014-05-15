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

package com.mycompany.obsecurefinal;

import cs492.obsecurefinal.spring.SpringModel;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCondition;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaConditionRepository;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Benjamin Arnold
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringModel.class)
public class MetaConditionrTest {
    
    @Autowired
    MetaConditionRepository repository;
    
    @Test
    public void create() throws Exception {
	final String name = "createTestConditionName";
	final String type = "createTestConditionType";
	Long id = null;
	{
	    MetaCondition condition = new MetaCondition();
	    condition.setName(name);
	    condition.setType(type);

	    MetaCondition created = repository.save(condition);
	    id = created.getId();
	}
	
	MetaCondition condition = repository.findOne(id);
	assertEquals(name, condition.getName());
	assertEquals(type, condition.getType());
	assertNotNull(condition.getUpdatedTime());	
    }
}
