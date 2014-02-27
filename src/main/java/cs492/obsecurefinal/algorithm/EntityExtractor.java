/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.algorithm;

import cs492.obsecurefinal.common.NamedEntity;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author JOEL
 */
public class EntityExtractor
{
    List<EntityExtractorStrategy> allStrats;
    String[] words;
    String sentence;
    
    public EntityExtractor(String sentence)
    {
        this.sentence = sentence;
        words = sentence.split(" ");
        
        allStrats = new Vector<EntityExtractorStrategy>();
        allStrats.add(new LocationExtractorStrategy(words));
        allStrats.add(new WorkplaceExtractorStrategy(words));
    }
    
    public List<NamedEntity> extractAll()
    {
        Vector<NamedEntity> allEntities = new Vector<NamedEntity>();
        
        // add all the entities of different types to a single list
        for(EntityExtractorStrategy strategy: allStrats)
        {
            allEntities.addAll(strategy.getEntities());
        }
        
        return allEntities;
    }
}

