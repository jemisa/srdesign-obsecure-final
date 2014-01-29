/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal;

import java.util.Vector;

/**
 *
 * @author JOEL
 */
public class EntityExtractorSimple extends EntityExtractor
{
    String sentence;
    
    public EntityExtractorSimple(String s)
    {
        sentence = s;
    }
    
    // extract named entities and return list
    public Vector<NamedEntity> extractAll()
    {
        Vector<NamedEntity> allEntities = new Vector<NamedEntity>();
        
        return allEntities;
    }
    
    private void extractLocation()
    {
        
    }
}
