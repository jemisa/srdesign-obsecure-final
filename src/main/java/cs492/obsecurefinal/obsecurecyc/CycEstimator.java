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

import cs492.obsecurefinal.common.EntityTypes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycConstant;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycNart;
import org.opencyc.cycobject.CycObject;

/**
 *
 * @author Benjamin Arnold
 */
public class CycEstimator {
    
    public static final int MATCH_SCORE = 5;
    private static final ExecutorService pool = Executors.newFixedThreadPool(8);
   
    private static final CycEstimator instance = new CycEstimator();
   
    private CycEstimator() {
	//singleton
    }
    
    public static CycEstimator getInstance() {
	return instance;
    }
    
    private CycList flattenCandidates(CycList candidates) {
	CycList vettedCandidates = new CycList();
	boolean changed = false;
	for (Object candidate : candidates) {
	    if (candidate instanceof CycNart) {
		vettedCandidates.addAll(((CycNart)candidate).toDeepCycList());
		changed = true;
	    } else if (candidate instanceof CycList) {
		vettedCandidates.addAll(((CycList)candidate).flatten());
		changed = true;
	    } else {
		vettedCandidates.add(candidate);
	    }
	}
	return changed ? flattenCandidates(vettedCandidates) : vettedCandidates;
    }
    
    public void shutDown() {
	pool.shutdown();
    }
    
    public CycList estimate(EntityTypes type, CycAccess cycAccess, CycQueryStrategy strategy, CycList candidates) throws IOException {
	List<Future<Integer>> scores = new ArrayList<Future<Integer>>();
	List<CycObject> values = new ArrayList<>();
	CycList vettedCandidates = flattenCandidates(candidates);
	
	for (Object candidate : vettedCandidates) {
	    CycConstant cc = (CycConstant) candidate;
	    List<String> thing = strategy.disambiguate(cycAccess, cc);
	    Callable<Integer> callable = new CycEstimationWorker(thing, type);
	    Future<Integer> future = pool.submit(callable);
	    scores.add(future);
	    values.add(cc);
	}
	
	TreeMap<Integer,CycList>  sortedValues = new TreeMap<>();
	
	for (int i = 0; i < values.size(); i++) {
	    Future<Integer> future = scores.get(i);
	    try {
		Integer score = future.get();
		CycList scoreVals = sortedValues.get(score);
		if (scoreVals==null) {
		    scoreVals = new CycList();
		}
		scoreVals.add(values.get(i));
		sortedValues.put(score, scoreVals);
	    } catch (InterruptedException | ExecutionException ex) {
		Logger.getLogger(CycEstimator.class.getName()).log(Level.SEVERE, null, ex);
	    }    
	}
	
	CycList result = new CycList();
	NavigableSet<Integer> vals = sortedValues.descendingKeySet();
	
	for (Iterator<Integer> it = vals.iterator(); it.hasNext();) {
	    Integer score = it.next();
	    if (score >= MATCH_SCORE) {
		CycList list = sortedValues.get(score);
		if (list != null) {
		    result.addAll(list);
		    list(result, score);
		}
	    }
	}
	return result;
    }
    
    private void list(CycList result, Integer score) {
	StringBuilder sb = new StringBuilder();
	sb.append(score).append( ": \n");
	for (Object o : result) {
	    CycObject co = (CycObject)  o;
	    sb.append(co.toString()).append("\n");
	}
	System.out.println(sb.toString());
    }
}
