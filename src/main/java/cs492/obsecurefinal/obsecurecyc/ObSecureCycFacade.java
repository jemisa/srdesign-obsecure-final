/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs492.obsecurefinal.obsecurecyc;

import cs492.obsecurefinal.common.EntityTypes;
import cs492.obsecurefinal.common.GeneralizationResult;
import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.obsecurecyc.opencyc.api.CycAccess;
import cs492.obsecurefinal.obsecurecyc.opencyc.api.CycApiException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ben
 */
public class ObSecureCycFacade {
    private static final ObSecureCycFacade instance = new ObSecureCycFacade();
    private CycAccess cycAccess;
    
    private ObSecureCycFacade() {
	try {
	    //singleton
	    cycAccess = new CycAccess();
	} catch (IOException | CycApiException ex) {
	    Logger.getLogger(ObSecureCycFacade.class.getName()).log(Level.SEVERE, null, ex);
	}
	    
    }
    
    public static final ObSecureCycFacade getInstance() {
	return instance;
    }
    
    public Map<NamedEntity, GeneralizationResult> generalize(List<NamedEntity> sensitiveEntities) {
	Map<NamedEntity, GeneralizationResult> map = new TreeMap<>();
	for (NamedEntity entity : sensitiveEntities) {
	    GeneralizationResult result = new GeneralizationResult();
	    EntityTypes type = entity.getType();
	    CycQueryStrategy strategy = ObSecureCycStrategyFactory.lookupStrategy(type);
	    try {
		new CycQuery(strategy,cycAccess).execute(entity.getContents());
	    } catch (IOException ex) {
		Logger.getLogger(ObSecureCycFacade.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    map.put(entity, result);
	}
	return map;
	
    }
}
