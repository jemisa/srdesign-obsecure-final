/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.common;

/**
 *
 * @author JOEL
 */
public class HintWithReplacements implements SanitizationHint
{
    private NamedEntity entity;
    private GeneralizationResult sanitizedMatches;

    public HintWithReplacements(NamedEntity ent, GeneralizationResult result)
    {
        entity = ent;
        sanitizedMatches = result;
    }

    public GeneralizationResult getReplacements()
    {
        return sanitizedMatches;
    }

    @Override
    public String getText() 
    {
        return entity.getText();
    }
    
    public NamedEntity getEntity()
    {
        return entity;
    }
}
