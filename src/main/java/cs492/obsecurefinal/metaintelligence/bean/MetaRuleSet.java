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

package cs492.obsecurefinal.metaintelligence.bean;

import cs492.obsecurefinal.common.EntityTypes;
import cs492.obsecurefinal.metaintelligence.parsetree.MetaNode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *  <ruleSet name="opencyc">
	<category name="LOCATION"></category>
    </ruleSet>
 * @author Benjamin Arnold
 */
@Table(name = "METARULESET")
@Entity(name = "METARULESET")
public class MetaRuleSet extends MetaBase implements MetaNode {
    
    @Column(name = "NAME")
    private String name;
    
    @OneToMany(fetch=FetchType.EAGER)
    private Set<MetaCategory> metaCategories = new HashSet<>();
        
    public void setName(String name) {
	this.name = name;
    }
    
    public String getName() {
	return name;
    }

    public void setMetaCategories(Set<MetaCategory> metaCategories) {
	this.metaCategories = metaCategories;
    }
    
    public Set<MetaCategory> getMetaCategories() {
	return metaCategories;
    }

    public void addMetaCatagory(MetaCategory metaCategory) {
	metaCategories.add(metaCategory);
    }
    
}
