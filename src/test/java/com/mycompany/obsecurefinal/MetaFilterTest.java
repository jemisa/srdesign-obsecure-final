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
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaFilter;
import cs492.obsecurefinal.spring.session.metaintelligence.MetaFilterRepository;
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
public class MetaFilterTest {
    @Autowired
    MetaFilterRepository repository;
    
    @Test
    public void create() throws Exception {
	final String name = "createTestFilter";
	final String type = "createTestFilterType";
	final Integer value = 5;
	Long id = null;
	{
	    MetaFilter filter = new MetaFilter();
	    filter.setName(name);
	    filter.setType(type);
	    filter.setValue(value);

	    MetaFilter created = repository.save(filter);
	    id = created.getId();
	}
	
	MetaFilter filter = repository.findOne(id);
	assertEquals(name, filter.getName());
	assertEquals(type, filter.getType());
	assertEquals(value.intValue(), filter.getValue().intValue());
	assertNotNull(filter.getUpdatedTime());
	
    }
}
