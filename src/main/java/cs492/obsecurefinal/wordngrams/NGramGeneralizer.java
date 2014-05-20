/*
 * Copyright (C) 2014 Drexel University
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cs492.obsecurefinal.wordngrams;

import cs492.obsecurefinal.common.DataSourceNames;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JOEL
 */
public class NGramGeneralizer 
{
    Map<String, String> generalizationPairs;
    
    public void loadGeneralizations()
    {
        generalizationPairs = new HashMap<>();
        
         try
        { 
            File f = new File(DataSourceNames.NGRAM_GENERALIZED_PAIRS);
            if(f.exists() && f.canRead())
            {
                BufferedReader reader = new BufferedReader(new FileReader(f));

                String line = reader.readLine();
                while(line != null)
                {
                    // ngrams seperated by vertical bar
                    String[] ngrampairs = line.split(":");
                    
                    for(String pair: ngrampairs)
                    {
                        // weight and text separated by colon
                        String[] parts = pair.split(",");
                        
                        generalizationPairs.put(parts[0], parts[1]);
                    }
                    
                    line = reader.readLine();
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.out);
        }      
    }
    
    public String getGeneralizedNgram(String ngram)
    {
        if(generalizationPairs != null)
        {
            if(generalizationPairs.containsKey(ngram))
                return generalizationPairs.get(ngram);
            else
                return "";
        }
        else
            return "";
    }
}
