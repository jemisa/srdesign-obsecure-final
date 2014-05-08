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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *  <criteria>
	<condition type="comment">DATE</condition>
    </criteria>
 * @author Benjamin Arnold
 */
@Table(name = "METACRITERIA")
@Entity(name = "METACRITERIA")
public class MetaCriteria extends MetaBase implements MetaNode {
    
    @OneToMany(fetch=FetchType.EAGER)
    private Set<MetaCondition> metaConditions = new HashSet<>();

    public Set<MetaCondition> getMetaConditions() {
	return metaConditions;
    }

    public void setMetaConditions(Set<MetaCondition> metaConditions) {
	this.metaConditions = metaConditions;
    }

    public void addMetaCondition(MetaCondition metaCondition) {
	metaConditions.add(metaCondition);
    }
    
}
