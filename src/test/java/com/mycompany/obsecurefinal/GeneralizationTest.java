/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.obsecurefinal;

import cs492.obsecurefinal.common.EntityTypes;
import cs492.obsecurefinal.common.GeneralizationResult;
import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.obsecurecyc.ObSecureCycFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 * OpenCyc must be running for this test to pass
 * @author Ben
 */
public class GeneralizationTest {

    @Test
    public void generalizeLocation() throws Exception {
	ObSecureCycFacade cycFacade = ObSecureCycFacade.getInstance();
	List<NamedEntity> sensitiveEntities = new ArrayList<>();
	NamedEntity philadelphia = new NamedEntity("Philadelphia", EntityTypes.Location);
	sensitiveEntities.add(philadelphia);
	Map<NamedEntity, GeneralizationResult> res = cycFacade.generalize(sensitiveEntities);
	for (NamedEntity entity : res.keySet()) {
	    GeneralizationResult result = res.get(entity);
	    new GeneralizationAsserter(result).assertContains("Pennsylvania");
	}
    }
    
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
