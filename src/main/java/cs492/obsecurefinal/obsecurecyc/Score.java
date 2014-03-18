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

package cs492.obsecurefinal.obsecurecyc;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Benjamin Arnold
 */
class Score implements Comparable {
    private Double score;
    private final List<Integer> scores = new ArrayList<>();
    
    Score(int bias) {
	scores.add(bias);
	score();
    }	
    
    Score() {
	this(0);
    }
    
    synchronized void success() {
	scores.add(1);
	score();
    }
    
    synchronized void fail() {
	scores.add(0);
	score();
    }
    
    private void score() {
	int val = 0;
	for (Integer i : scores) {
	    val += i;
	}
	score = val / (scores.size()*-1.0);  //negative puts biggest scores first in map
    }

    @Override
    public int compareTo(Object o) {
	return (o instanceof Score) ? score.compareTo(((Score) o).score) : 1;
	
    }
    
    
    
}
