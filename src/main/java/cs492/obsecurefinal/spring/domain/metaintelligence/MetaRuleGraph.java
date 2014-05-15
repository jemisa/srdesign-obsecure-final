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

package cs492.obsecurefinal.spring.domain.metaintelligence;

import cs492.obsecurefinal.spring.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author Benjamin Arnold
 */
@Entity
public class MetaRuleGraph extends BaseEntity implements MetaNode {
    
    @Column
    private Long metaRuleId;
    
    @Column
    private Long metaCategoryId;
    
    @Column
    private Integer score;

    public Long getMetaRuleId() {
	return metaRuleId;
    }

    public void setMetaRuleId(Long metaRuleId) {
	this.metaRuleId = metaRuleId;
    }

    public Long getMetaCategoryId() {
	return metaCategoryId;
    }

    public void setMetaCategoryId(Long metaCategoryId) {
	this.metaCategoryId = metaCategoryId;
    }

    public Integer getScore() {
	return score;
    }

    public void setScore(Integer score) {
	this.score = score;
    }
    
    
}
