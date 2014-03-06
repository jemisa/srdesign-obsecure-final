/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.obsecurefinal;

import cs492.obsecurefinal.algorithm.LocationExtractorStrategy;
import cs492.obsecurefinal.algorithm.WorkplaceExtractorStrategy;
import cs492.obsecurefinal.common.EntityTypes;
import cs492.obsecurefinal.common.GeneralizationResult;
import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.generalization.GeneralizationManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 * OpenCyc must be running for these test cases to pass
 * @author Ben
 */
public class GeneralizationTest {

    @Test
    public void generalizeLocation() throws Exception {
	final String sentence = "Philadelphia";
	LocationExtractorStrategy strategy = new LocationExtractorStrategy(sentence);
	List<NamedEntity> sensitiveEntities = strategy.getEntities();
	
	Map<NamedEntity, GeneralizationResult> res = GeneralizationManager.generalize(sensitiveEntities);
	for (NamedEntity entity : res.keySet()) {
	    GeneralizationResult result = res.get(entity);
	    new GeneralizationAsserter(result).assertContains("Pennsylvania");
	}
    }
    
    @Test
     public void generalizeOrganization() throws Exception {
	final String sentence = "Merck";
	WorkplaceExtractorStrategy strategy = new WorkplaceExtractorStrategy(sentence);
	List<NamedEntity> sensitiveEntities = strategy.getEntities();
	
	Map<NamedEntity, GeneralizationResult> res = GeneralizationManager.generalize(sensitiveEntities);
	for (NamedEntity entity : res.keySet()) {
	    GeneralizationResult result = res.get(entity);
	    new GeneralizationAsserter(result).assertContains("pharmaceutical");
	}
    }
     
//     @Test //FIXME: when we get the strategy we should reenable this
//     public void generalizeOccupation() throws Exception {
//	List<NamedEntity> sensitiveEntities = new ArrayList<>();
//	NamedEntity occupation = new NamedEntity("system administrator", EntityTypes.OCCUPATION);
//	sensitiveEntities.add(occupation);
//	
//	Map<NamedEntity, GeneralizationResult> res = GeneralizationManager.generalize(sensitiveEntities);
//	for (NamedEntity entity : res.keySet()) {
//	    GeneralizationResult result = res.get(entity);
//	    new GeneralizationAsserter(result).assertContains("computer");
//	}
//    }
     
    private static class GeneralizationAsserter {
	private final GeneralizationResult generalizationResult;
	
	private GeneralizationAsserter(GeneralizationResult generalizationResult) {
	    this.generalizationResult = generalizationResult;
	}
	
	void assertContains(String target) {
	    boolean matched = false;
	    for (String result : generalizationResult.getResults()) {
		if (result !=  null && result.contains(target)) {
		    matched = true;
		    break;
		}
	    }
	    assertTrue(target + " not found in generalization results",matched);
	}
    }
}
