/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.common;

import java.util.Vector;

/**
 *
 * @author JOEL
 */
public class GeneralizationResult
{
        private final Vector<String> results = new Vector<>(); //TODO: why are we using an obsolete collection?

        public Vector<String> getResults()
        {
               return results;
        }

        public void add(String result)
        {
	    results.add(result);
        }
}