/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.algorithm;

import cs492.obsecurefinal.common.EntityTypes;
import java.util.List;
import cs492.obsecurefinal.common.NamedEntity;

/**
 *
 * @author JOEL
 */
public abstract class EntityExtractorStrategy 
{
    protected EntityTypes type;
    protected String[] sentence;
    
    public EntityExtractorStrategy(String[] sentence, EntityTypes type)
    {
        this.type = type;
        this.sentence = sentence;
    }
    
    public abstract List<NamedEntity> getEntities();
}