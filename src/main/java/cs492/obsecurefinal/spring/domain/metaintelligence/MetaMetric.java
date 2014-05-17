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

package cs492.obsecurefinal.spring.domain.metaintelligence;

import cs492.obsecurefinal.spring.domain.BaseEntity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *  <metric>
	<weight value="1">City</weight>
	<weight value="2">State-UnitedStates</weight>
	<weight value="3">IndependentCountry</weight>
	<weight value="4">Country</weight>
	<weight value="5">Continent</weight>
    </metric>
 * @author Benjamin Arnold
 */
@Entity
public class MetaMetric extends BaseEntity implements MetaNode {
    public static final String CLASS_NAME = "MetaMetric";
    
    @OneToMany(fetch=FetchType.EAGER)
    private Set<MetaWeight> metaWeights = new HashSet<>();

    public Set<MetaWeight> getMetaWeights() {
	return metaWeights;
    }

    public void setMetaWeights(Set<MetaWeight> metaWeights) {
	this.metaWeights = metaWeights;
    }

    public void addWeight(MetaWeight metaWeight) {
	metaWeights.add(metaWeight);
    }
  
}