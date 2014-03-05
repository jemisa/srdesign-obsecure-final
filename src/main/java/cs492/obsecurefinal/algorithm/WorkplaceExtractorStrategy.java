/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.algorithm;

import cs492.obsecurefinal.common.DataSourceNames;
import cs492.obsecurefinal.common.EntityTypes;
import cs492.obsecurefinal.common.NamedEntity;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/**
 *
 * @author JOEL
 */
public class WorkplaceExtractorStrategy extends EntityExtractorStrategy
{
    public WorkplaceExtractorStrategy(String[] sentence)
    {
        super(sentence, EntityTypes.COMPANY);
    }
    
    @Override
    public List<NamedEntity> getEntities() 
    {
        List<NamedEntity> workEntities = new Vector<NamedEntity>();
        
        try
        {
            // TODO: change model file name
            InputStream modelFile = new FileInputStream(DataSourceNames.WORKPLACE_MODEL_FILE);
            TokenNameFinderModel tnf = new TokenNameFinderModel(modelFile);
            NameFinderME nf = new NameFinderME(tnf);
            Span spans[] = nf.find(sentence);
            String entities[] = Span.spansToStrings(spans, sentence);
            
            // Add all identified workplace entities to the list
            for(String ent:entities)
                workEntities.add(new NamedEntity(ent, type));
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.out);
        }
        
        return workEntities;
    }
    
}
