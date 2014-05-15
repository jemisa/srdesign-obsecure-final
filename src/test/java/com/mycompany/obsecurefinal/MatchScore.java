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

package com.mycompany.obsecurefinal;

import java.util.List;

/**
 *
 * @author Benjamin Arnold
 */
public class MatchScore {
    private final String[][] knownAgentDataSets;
    
    public MatchScore(String[][] knownAgentDataSets) {
	this.knownAgentDataSets = knownAgentDataSets;
    }
    
    public int score(List<Predicate> predicates) {
	int score = 0;
	for (String[] dataSet : knownAgentDataSets)  {
	    int i = 0;
	    for (Predicate predicate : predicates) {
		if (predicate.apply(dataSet[i++])) {
		    score++;
		}
	    }
	    if (score < dataSet.length) {
		score = 0;
	    } else {
		return score;
	    }
	}
	return score;
    }
}
