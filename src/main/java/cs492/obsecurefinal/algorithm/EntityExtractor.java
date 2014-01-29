/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.algorithm;

import cs492.obsecurefinal.common.NamedEntity;
import java.util.Vector;

/**
 *
 * @author JOEL
 */
public abstract class EntityExtractor
{
    public EntityExtractor() {}
    
    public abstract Vector<NamedEntity> extractAll();
}
