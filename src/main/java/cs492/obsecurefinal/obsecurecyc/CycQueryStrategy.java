/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import java.io.IOException;
import java.net.UnknownHostException;
import org.opencyc.api.CycAccess;
import org.opencyc.api.CycObjectFactory;
import org.opencyc.cycobject.CycConstant;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycNart;
import org.opencyc.cycobject.CycSymbol;

/**
 *
 * @author Ben
 */
public abstract class CycQueryStrategy {
    protected static final CycSymbol CONSTANT_APROPOS = CycObjectFactory.makeCycSymbol("CONSTANT-APROPOS");
    protected static final CycSymbol CONSTANT_COMPLETE = CycObjectFactory.makeCycSymbol("CONSTANT-COMPLETE");
    protected static final String MICROTHEORY_US_GEOGRAPHY = "UnitedStatesGeographyMt";
    protected static final String MICROTHEORY_INFERENCE_PSC = "InferencePsc";
    protected static final String MICROTHEORY_BASE = "BaseKB";
    protected static final String MICROTHEORY_ENGLISH = "EnglishMt";
    protected static final String MICROTHEORY_UNIVERSAL_VOCABULARY = "UniversalVocabularyMt";
    
    public abstract CycList exec(CycAccess cycAccess, String text) throws UnknownHostException, IOException;
    
    protected CycList getAllCycConstantsContainingText(CycAccess cycAccess, String text) throws IOException {
	final CycList constants = cycAccess.converseList(CycList.makeCycList(CONSTANT_APROPOS,text));
	return constants;
    } 
    
    protected CycList getAllCycConstantsWithPrefix(CycAccess cycAccess, String text) throws IOException {
	final CycList constants = cycAccess.converseList(CycList.makeCycList(CONSTANT_COMPLETE,text));
	return constants;
    }
    
    protected void filter(CycList rawResults, CycConstant...filters) {
	for (CycConstant filter : filters) {
	    boolean removed = rawResults.remove(filter);
	    if (!removed) {
		for (Object o : rawResults) {
		    if (o instanceof CycNart) {
			CycNart nart = (CycNart) o;
			CycList deepList = nart.toDeepCycList();
			boolean contains = false;
			for (CycConstant _filter : filters) { 
			    if (!contains) {
				contains = deepList.contains(_filter);
			    } else {
				break;
			    }
			}
			if (contains) {
			    rawResults.remove(nart);
			}
		    }
		}
	    }
	}
    } 
    
}
