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

package cs492.obsecurefinal.spring.domain.common;

import cs492.obsecurefinal.spring.domain.BaseEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author Benjamin Arnold
 */
@Entity
public class SpringAgent extends BaseEntity implements Serializable {
    
    @Column
    private String name;
    
    @Column
    private String occupation;
    
    @Column
    private String location; 
    
    @Column
    private String workplace;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getOccupation() {
	return occupation;
    }

    public void setOccupation(String occupation) {
	this.occupation = occupation;
    }

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public String getWorkplace() {
	return workplace;
    }

    public void setWorkplace(String workplace) {
	this.workplace = workplace;
    }
    
    
}
