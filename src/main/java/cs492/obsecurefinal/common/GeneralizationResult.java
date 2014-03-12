/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Joel Marcinik
 */
public class GeneralizationResult
{
        private final List<String> results = new ArrayList<>();

        public String[] getResults()
        {
	    String[] result = new String[results.size()];
	    return results.toArray(result);
        }

        public void add(String result)
        {
	    results.add(result);
        }
	
	public void addAll(Collection<String> result) {
	    results.addAll(result);
	}
}