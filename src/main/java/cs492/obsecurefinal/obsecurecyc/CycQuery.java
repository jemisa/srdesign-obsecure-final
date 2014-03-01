/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import cs492.obsecurefinal.obsecurecyc.opencyc.api.CycAccess;
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycConstant;
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycList;
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycObject;
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycVariable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    
    public List<String> execute(String text) throws IOException {
	CycList cycList = strategy.exec(cycAccess, text);
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
    
}
