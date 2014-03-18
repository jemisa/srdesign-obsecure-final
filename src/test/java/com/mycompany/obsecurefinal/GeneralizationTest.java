/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.obsecurefinal;

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
	final String text = "Philadelphia";
	List<NamedEntity> sensitiveEntities = new ArrayList<>();
	NamedEntity occupation = new NamedEntity(null, null, EntityTypes.LOCATION) {
	    @Override
	    public String getText() {
		return text;
	    }
	};
	sensitiveEntities.add(occupation);

	Map<NamedEntity, GeneralizationResult> res = GeneralizationManager.generalize(sensitiveEntities);
	for (NamedEntity entity : res.keySet()) {
	    GeneralizationResult result = res.get(entity);
	    new GeneralizationAsserter(result).assertContains("Pennsylvania");
	}
    }
    
    @Test
     public void generalizeOrganization() throws Exception {
	final String text = "Merck";
	List<NamedEntity> sensitiveEntities = new ArrayList<>();
	NamedEntity occupation = new NamedEntity(null, null, EntityTypes.ORGANIZATION) {
	    @Override
	    public String getText() {
		return text;
	    }
	};
	sensitiveEntities.add(occupation);

	Map<NamedEntity, GeneralizationResult> res = GeneralizationManager.generalize(sensitiveEntities);
	for (NamedEntity entity : res.keySet()) {
	    GeneralizationResult result = res.get(entity);
	    new GeneralizationAsserter(result).assertContains("pharmaceutical");
	}
    }
     
     @Test 
     public void generalizeOccupation() throws Exception {
	List<NamedEntity> sensitiveEntities = new ArrayList<>();
	final String systemAdministrator = "system administrator";
	NamedEntity occupation = new NamedEntity(null, null, EntityTypes.OCCUPATION) {
	    @Override
	    public String getText() {
		return systemAdministrator;
	    }
	};
	sensitiveEntities.add(occupation);
	
	Map<NamedEntity, GeneralizationResult> res = GeneralizationManager.generalize(sensitiveEntities);
	for (NamedEntity entity : res.keySet()) {
	    GeneralizationResult result = res.get(entity);
	    new GeneralizationAsserter(result).assertContains("computer");
	}
    }
     
     @Test
     public void generalizeKnownArtifact() throws Exception {
	List<NamedEntity> sensitiveEntities = new ArrayList<>();
	final String libertyBell = "Liberty Bell";
	NamedEntity artifact = new NamedEntity(null, null, EntityTypes.LOCATION) {
	    @Override
	    public String getText() {
		return libertyBell;
	    }
	};
	sensitiveEntities.add(artifact);
	
	Map<NamedEntity, GeneralizationResult> res = GeneralizationManager.generalize(sensitiveEntities);
	for (NamedEntity entity : res.keySet()) {
	    GeneralizationResult result = res.get(entity);
	    new GeneralizationAsserter(result).assertContains("place");
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
		System.out.println(result);
		if (result !=  null && (result.contains(target))) {
		    matched = true;
		    break;
		}
	    }
	    assertTrue(target + " not found in generalization results",matched);
	}
    }
}
