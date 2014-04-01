/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.algorithm;

import cs492.obsecurefinal.common.DataSourceNames;
import cs492.obsecurefinal.common.EntityTypes;
import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.common.Sentence;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/**
 *
 * @author JOEL
 */
public class OccupationExtractorStrategy extends EntityExtractorStrategy
{
    public OccupationExtractorStrategy(Sentence sentence)
    {
        super(sentence, EntityTypes.OCCUPATION);
    }

    @Override
    public List<NamedEntity> getEntities() 
    {
        List<NamedEntity> occupationEntities = new ArrayList<>();
               
        try
        {
            InputStream modelFile = OccupationExtractorStrategy.class.getResourceAsStream(DataSourceNames.OCCUPATION_MODEL_FILE);
            TokenNameFinderModel tnf = new TokenNameFinderModel(modelFile);
            NameFinderME nf = new NameFinderME(tnf);
            Span spans[] = nf.find(words);
            //String entityStrings[] = Span.spansToStrings(spans, sentence.getText());
            
            // Add all identified location entities to the list
            for(Span span: spans)
              occupationEntities.add(new NamedEntity(sentence, span, type));
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.out);
        }
        
        return occupationEntities;
    }
}
