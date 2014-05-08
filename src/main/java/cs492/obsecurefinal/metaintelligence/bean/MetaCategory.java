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

import cs492.obsecurefinal.metaintelligence.parsetree.MetaNode;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * <category name="LOCATION">
        <rule name="namedLocation" type="OPENCYC" purpose="GENERALIZATION"></rule>
 * </category>
 * @author Benjamin Arnold
 */
@Table(name = "METACATEGORY")
@Entity(name = "METACATEGORY")
public class MetaCategory extends MetaBase implements MetaNode {
    
    @OneToMany(fetch=FetchType.EAGER)
    private Set<MetaRule> metaRules = new HashSet<>();
    
    @Column(name = "NAME")
    private String name;
    
    public void setMetaRules(Set<MetaRule> metaRules) {
	this.metaRules = metaRules;
    }
    
    public Set<MetaRule> getMetaRules() {
	return metaRules;
    }

    public void setName(String name) {
	this.name = name;
    }
    
    public String getName() {
	return name;
    }

    public void addMetaRule(MetaRule metaRule) {
	metaRules.add(metaRule);
    }
}
