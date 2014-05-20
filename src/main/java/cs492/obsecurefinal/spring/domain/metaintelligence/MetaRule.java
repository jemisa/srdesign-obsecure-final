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
import javax.persistence.*;

/**
 * <rule name="namedLocation" type="OPENCYC">
	<filter type="adaptive" value="3">LocationFilter</filter>
	<metric></metric>
	<criteria></criteria>
    </rule>
 * @author Benjamin Arnold
 */
@Entity
public class MetaRule  extends BaseEntity implements MetaNode {
    
    @Column(name = "NAME")
    private String name;
	
    @Column(name = "TYPE")
    private String type;
    
    @OneToOne//(cascade=CascadeType.ALL)
    private MetaFilter filter;
    
    @OneToOne//(cascade=CascadeType.ALL)
    private MetaMetric metric;
    
    @OneToOne//(cascade=CascadeType.ALL)
    private MetaCriteria criteria;
    
    @OneToOne
    private MetaAction action;
    
    public void setName(String name) {
	this.name = name;
    }
    
    public String getName() {
	return name;
    }

    public void setType(String type) {
	this.type = type;
    }
    
    public String getType() {
	return type;
    }   
    
    public MetaFilter getFilter() {
	return filter;
    }

    public void setFilter(MetaFilter filter) {
	this.filter = filter;
    }

    public MetaMetric getMetric() {
	return metric;
    }

    public void setMetric(MetaMetric metric) {
	this.metric = metric;
    }

    public MetaCriteria getCriteria() {
	return criteria;
    }

    public void setCriteria(MetaCriteria criteria) {
	this.criteria = criteria;
    }

    public MetaAction getAction() {
	return action;
    }

    public void setAction(MetaAction action) {
	this.action = action;
    }
    
}
