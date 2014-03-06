/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.algorithm;

import cs492.obsecurefinal.common.EntityTypes;
import java.util.List;
import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.common.Sentence;

/**
 *
 * @author JOEL
 */
public abstract class EntityExtractorStrategy 
{
    protected EntityTypes type;
    protected Sentence sentence;
    protected String[] words;
    
    public EntityExtractorStrategy(Sentence sentence, EntityTypes type)
    {
        this.type = type;
        this.sentence = sentence;
        
        words = sentence.getText().split(" ");
    }
    
    public abstract List<NamedEntity> getEntities();
}
