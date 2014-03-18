/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import org.opencyc.api.CycAccess;
import org.opencyc.api.CycObjectFactory;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycObject;
import org.opencyc.cycobject.CycVariable;

/**
 *
 * @author Ben
 */
public class CycQueryBuilder {
    private static final String X_VARIABLE = "?X";
    private static final String PREFIX = "#$";
    private static final String BEGIN = "(";
    private static final String END = ")";
    private static final String SPACE = " ";
    
    protected static CycList makeQuery (CycAccess cycAccess, CycObject cycObject, QueryType type) {
	StringBuilder sb = new StringBuilder();
	sb.append(BEGIN).append(PREFIX).append(type.getQueryTerm()).append(SPACE).append(cycObject.cyclify()).append(SPACE).append(X_VARIABLE).append(END);
	CycList query = cycAccess.makeCycList(sb.toString());
	return query;
    }
    
    protected static CycVariable makeVariable() {
	return makeVariable(X_VARIABLE);
    }
    
    protected static CycVariable makeVariable(String x) {
	return  CycObjectFactory.makeCycVariable(x);
    }
    
    protected static enum QueryType {
	BROADER("broaderTerm"),PRETTY("prettyString"),PRETTY_CANONICAL("prettyString-Canonical"),ISA("isa");
	
	private final String query;
	
	private QueryType(String q) {
	    query = q;
	}
	
	private String getQueryTerm() {
	    return query;
	}
    }
}
