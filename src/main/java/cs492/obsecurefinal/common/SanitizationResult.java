/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.common;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JOEL
 */
public class SanitizationResult
{
        private List<SanitizationHint> results;

        public SanitizationResult()
        {
            results = new ArrayList<>();
        }

        public void addHint(SanitizationHint hint)
        {
            results.add(hint);
        }

        public List<SanitizationHint> getResults()
        {
            return results;
        }
}
