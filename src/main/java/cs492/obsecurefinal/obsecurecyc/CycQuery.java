/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import cs492.obsecurefinal.metaintelligence.IntelligenceGraph;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycConstant;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycObject;
import org.opencyc.cycobject.CycVariable;

/**
 *
 * @author Ben
 */
public class CycQuery {
    
    private final CycQueryStrategy strategy;
    private final CycAccess cycAccess;
    
    public CycQuery(CycQueryStrategy strategy, CycAccess cycAccess) {
	this.strategy = strategy;
	this.cycAccess = cycAccess;
    }
    
    public List<String> execute(WordBall wordBall) throws IOException {
	
	CycList cycList = strategy.delegate(cycAccess, wordBall);
	List<String> results = new ArrayList<>();
	CycVariable variable = CycQueryBuilder.makeVariable();
	for (Iterator it = cycList.iterator(); it.hasNext();) {
	    CycObject obj = (CycObject) it.next();
	    CycList prettyQuery = CycQueryBuilder.makeQuery(cycAccess, obj, CycQueryBuilder.QueryType.PRETTY);
	    CycConstant mtEng = cycAccess.getConstantByName(CycQueryStrategy.MICROTHEORY_ENGLISH);
	    CycList prettyResult = cycAccess.queryVariable(prettyQuery, variable, mtEng);

	    for (Iterator pretties = prettyResult.iterator(); pretties.hasNext();) {
		String prettyString = (String) pretties.next();
		results.add(prettyString);
	    }
	}
	return results;
    }
    
    protected CycQueryStrategy getStrategy() {
	return strategy;
    }
    
    protected CycAccess getCycAccess() {
	return cycAccess;
    }
    
}
