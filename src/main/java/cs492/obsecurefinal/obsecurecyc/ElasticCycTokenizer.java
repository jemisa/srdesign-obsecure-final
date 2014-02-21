package cs492.obsecurefinal.obsecurecyc;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import cs492.obsecurefinal.obsecurecyc.opencyc.api.CycAccess;
import cs492.obsecurefinal.obsecurecyc.opencyc.api.CycObjectFactory;
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycList;
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycObject;
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycSymbol;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;


/**
 *
 * @author Ben
 */
class ElasticCycTokenizer {
    private final CycAccess cycAccess = new CycAccess();
    private final CycSymbol CONSTANT_COMPLETE = CycObjectFactory.makeCycSymbol("CONSTANT-COMPLETE");
    private final String original;
    private final String plainO;
    private final List<String> origTokens = new ArrayList<>();
    private int currentIndex;
    
    public ElasticCycTokenizer(String in) throws IOException {
	original = in;
	
	String[] searchList = ", ? ! . : ; @ ' # $ % ^ & * ( )".split(" ");
	String[] replacementList = new String[searchList.length];
	for (int i = 0; i < searchList.length; i++) {
	    replacementList[i] = StringUtils.EMPTY;
	};
	plainO = StringUtils.replaceEachRepeatedly(in, searchList, replacementList);
	Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(plainO);
	while (m.find()) {
	    String tok = m.group(1).replace("\"", "");
	    origTokens.add(tok);
	}
	currentIndex = 0;
    }
    
    public boolean hasMoreTokens() {
	return currentIndex < origTokens.size();
    }
    
    public CycList next() throws Exception {	
	
	final CycList constants = cycAccess.converseList(CycList.makeCycList(CONSTANT_COMPLETE,origTokens.get(currentIndex)));
	
	CycList queryResult = null;
	if (constants != null) {
	    CycObject cycObject = (CycObject) ((constants.size() == 1) ? constants.get(0) : filterResults(constants));
	    
	    if (cycObject != null) {
		queryResult = cycAccess.getGenls(cycObject);
	    }
	    
	}
	currentIndex++;
	return queryResult;
    }
    
    private CycObject filterResults(CycList results) throws Exception {
	return filterResults(origTokens.get(currentIndex), results);
    }
    
    private CycObject filterResults(String searchTerm, CycList original) throws Exception {
	CycObject cycObject = null;
	StringBuilder sb = new StringBuilder();
	sb.append(searchTerm);
	CycList last = original;
	CycList constants = original;
	for (int i = 1; i < 5 && i+currentIndex < origTokens.size() && constants.size()>1; i++ ) {
	    last = constants;
	    String term = origTokens.get(currentIndex+i);
	    sb.append(" ").append(term);
	    constants = cycAccess.converseList(CycList.makeCycList(CONSTANT_COMPLETE,sb.toString()));
	
	}
	
	if (constants == null || constants.isEmpty()) {
	    constants = last;
	}
	
	cycObject = (CycObject) constants.get(0);
	
	return cycObject;
    }
    
    
}
