package cs492.obsecurefinal.algorithm;

import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.common.Sentence;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

// Entity extractor class, uses a different strategy object to extract each different kind of 
// private information
public class EntityExtractor
{
    List<EntityExtractorStrategy> allStrats;
    String[] words;
    String sentence;
    
    // Constructor, splits the provided sentence into words for processing,
    // and sets up which strategies to use
    public EntityExtractor(Sentence sentence)
    {
        this.sentence = sentence.getText();
        //words = sentence.split(" ");
        
        allStrats = new Vector<EntityExtractorStrategy>();
        allStrats.add(new LocationExtractorStrategy(sentence));
        allStrats.add(new WorkplaceExtractorStrategy(sentence));
        allStrats.add(new OccupationExtractorStrategy(sentence));
    }
    
    // For each strategy, get all entities that match it and return all of them together
    public List<NamedEntity> extractAll()
    {
        ArrayList<NamedEntity> allEntities = new ArrayList<NamedEntity>();
        
        // add all the entities of different types to a single list
        for(EntityExtractorStrategy strategy: allStrats)
        {
            allEntities.addAll(strategy.getEntities());
        }
        
        return allEntities;
    }
}

