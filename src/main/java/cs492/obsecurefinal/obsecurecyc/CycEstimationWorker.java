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

import cs492.obsecurefinal.cluster.BrownClusters;
import cs492.obsecurefinal.cluster.WordNetDictionary;
import cs492.obsecurefinal.common.EntityTypes;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author Benjamin Arnold
 */
public class CycEstimationWorker implements Callable<Integer> {
    private final String[] original;
    private final  List<String> things;
    
    CycEstimationWorker( List<String> things, EntityTypes type) {
	this.original = type.name().toLowerCase().split("_");
	this.things = things;
    }
    
    @Override
    public Integer call() throws Exception {
	Integer score  = 0;
	
	for (String s : things) {
	    if (related(s)) {
		score += CycEstimator.MATCH_SCORE;
	    } else {
		score--;
	    }
	}
	
	return score;
    }

    private boolean related(String s) {
	for (String orig : original) {
	    System.out.println("Comparing " + orig + " and " + s);
	    if (BrownClusters.getInstance().coPooled(orig, s) || WordNetDictionary.getInstance().areRelated(orig, s)) {
		System.out.println(orig +  "::" + s + " GOOD");
		return true;
	    }
	}
	return false;
    }
    
    
}
