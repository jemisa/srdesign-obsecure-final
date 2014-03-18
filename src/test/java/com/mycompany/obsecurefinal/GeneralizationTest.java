/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.obsecurefinal;

import cs492.obsecurefinal.common.EntityTypes;
import cs492.obsecurefinal.common.GeneralizationResult;
import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.common.Sentence;
import cs492.obsecurefinal.generalization.GeneralizationManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;
import org.opencyc.util.StringUtils;

/**
 * OpenCyc must be running for these test cases to pass
 * @author Ben
 */
public class GeneralizationTest {

    @Test
    public void generalizeLocation() throws Exception {
	final String text = "Philadelphia";
	Sentence sentence = new Sentence("",1) {
	    @Override
	    public String getText() {
		return "";
	    }
	};
	
	List<NamedEntity> sensitiveEntities = new ArrayList<>();
	NamedEntity occupation = new NamedEntity(sentence, null, EntityTypes.LOCATION) {
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
	Sentence sentence = new Sentence("",1) {
	    @Override
	    public String getText() {
		return "";
	    }
	};
	List<NamedEntity> sensitiveEntities = new ArrayList<>();
	NamedEntity occupation = new NamedEntity(sentence, null, EntityTypes.ORGANIZATION) {
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
	 Sentence sentence = new Sentence("",1) {
	    @Override
	    public String getText() {
		return "";
	    }
	};
	List<NamedEntity> sensitiveEntities = new ArrayList<>();
	final String systemAdministrator = "system administrator";
	NamedEntity occupation = new NamedEntity(sentence, null, EntityTypes.OCCUPATION) {
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
	 Sentence sentence = new Sentence("",1) {
	    @Override
	    public String getText() {
		return "";
	    }
	};
	List<NamedEntity> sensitiveEntities = new ArrayList<>();
	final String libertyBell = "Liberty Bell";
	NamedEntity artifact = new NamedEntity(sentence, null, EntityTypes.LOCATION) {
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
