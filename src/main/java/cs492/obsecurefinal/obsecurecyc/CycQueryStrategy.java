/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import cs492.obsecurefinal.common.EntityTypes;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.opencyc.api.CycAccess;
import org.opencyc.api.CycObjectFactory;
import org.opencyc.cycobject.CycConstant;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycObject;
import org.opencyc.cycobject.CycSymbol;
import org.opencyc.cycobject.CycVariable;
import org.opencyc.cycobject.ELMt;
import org.opencyc.inference.DefaultInferenceParameters;
import org.opencyc.inference.InferenceParameters;
import org.opencyc.inference.InferenceResultSet;

/**
 *
 * @author Ben
 */
public abstract class CycQueryStrategy {
    protected static final CycSymbol CONSTANT_APROPOS = CycObjectFactory.makeCycSymbol("CONSTANT-APROPOS");
    protected static final CycSymbol CONSTANT_COMPLETE = CycObjectFactory.makeCycSymbol("CONSTANT-COMPLETE");
    protected static final String MICROTHEORY_US_GEOGRAPHY = "UnitedStatesGeographyMt";
    protected static final String MICROTHEORY_WORLD_GEOGRAPHY = "WorldGeographyMt";
    protected static final String MICROTHEORY_INFERENCE_PSC = "InferencePsc";
    protected static final String MICROTHEORY_BASE = "BaseKB";
    protected static final String MICROTHEORY_ENGLISH = "EnglishMt";
    protected static final String MICROTHEORY_UNIVERSAL_VOCABULARY = "UniversalVocabularyMt";
    protected static final String MICROTHEORY_WORLD_TEMPORAL = "CurrentWorldDataCollectorMt-NonHomocentric";
    
    private EntityTypes type;
    
    CycQueryStrategy(EntityTypes type) {
	this.type = type;
    }
    
    EntityTypes getEntityTypes() {
	return type;
    }
    
    public abstract CycList exec(CycAccess cycAccess, CycList constants) throws UnknownHostException, IOException;
    
    public CycList cycConstantAutoCompleteContains(CycAccess cycAccess, String text) throws IOException {
	final CycList constants = cycAccess.converseList(CycList.makeCycList(CONSTANT_APROPOS,text));
	return constants;
    } 
    
    public CycList cycConstantAutoCompleteExact(CycAccess cycAccess, String text) throws IOException {
	final CycList constants = cycAccess.converseList(CycList.makeCycList(CONSTANT_COMPLETE,text));
	return constants;
    }
    
    public CycConstant getMicrotheory(CycAccess cycAccess) throws IOException {
	return cycAccess.getConstantByName(MICROTHEORY_US_GEOGRAPHY);
    }
    
    protected CycList getConstants(CycAccess cycAccess, Iterator<String> order) throws IOException {
	CycList constants = null;
	while (order.hasNext() && (constants == null || constants.isEmpty())) {
	    String next = order.next();
	    constants = cycConstantAutoCompleteExact(cycAccess, next); //favor exact matches
	    if (constants == null) {
		constants = cycConstantAutoCompleteContains(cycAccess, next);
	    }
	}
	return constants;
    }

    public CycList delegate(CycAccess cycAccess, WordBall wordBall) throws IOException {
	CycList result;
	
	CycList constants = getConstants(cycAccess, wordBall.iterator());
	
	if (constants == null || constants.isEmpty()) {
	    result = new CycList();
	} else {
	    result = exec(cycAccess, constants);
	    if (result == null || result.isEmpty()) {
		result = execPlanB(cycAccess, constants);
	    }
	}
	CycList estimation = CycEstimator.getInstance().estimate(type, cycAccess, this,result);
	
	return estimation;
    }
    
    
    public CycList execPlanB(final CycAccess cycAccess, CycList constants) throws UnknownHostException, IOException {
	CycQueryExecutor executor = new CycQueryExecutor() {
	    
	   public CycList loop(CycObject cycObject) throws UnknownHostException, IOException {

		CycList query = CycQueryBuilder.makeQuery(cycAccess, cycObject, CycQueryBuilder.QueryType.ISA);
		CycVariable variable = CycQueryBuilder.makeVariable();
		InferenceParameters inference = new DefaultInferenceParameters(cycAccess);
		ELMt context = CycAccess.everythingPSC;
		InferenceResultSet rs = cycAccess.executeQuery(query, context, inference, 1000L);
		rs.first();
		CycList results = new CycList();
		CycObject result = rs.getCycObject(variable);
		results.add(result);
		while (rs.next()) {
		    result = rs.getCycObject(variable);
		    if (result instanceof CycList) {
			results.addAll((CycList) result);
		    } else {
			results.add(result);
		    }
		    results.add(result);
		}
		    
		return results;
	    }
	    
	    @Override
	    public CycList filter(CycList input) throws IOException {
		return CycFilter.filter(cycAccess, input, CycFilter.INDIVIDUAL);
	    }
	};
	CycList result = executor.execute(constants);
	
	return result;
    }

   
    public List<String> disambiguate(CycAccess cycAccess, CycConstant thing) throws IOException {
	List<String> values = new ArrayList<String>();

	if (thing != null) {
	    CycList list = new CycList();
	    list.add(thing);
	    CycList disambiguationExpression = cycAccess.generateDisambiguationPhraseAndTypes(list);
	    for (Object entry : disambiguationExpression) {
		if (entry instanceof CycList) {
		    CycList subList = (CycList) entry;
		    for (Object o: subList) {
			String name = o.toString();
//			if (name != null && !(name.contains("Collection") || name.contains("collection") || name.contains("___"))) {
			    values.add(name);
//			}
		    }
		} else {
		    String name = StringUtils.uncapitalize(entry.toString());
		    if (name != null) {
			values.add(name);
		    }
		}	
	    }
	}
	return values;
    }     
}
