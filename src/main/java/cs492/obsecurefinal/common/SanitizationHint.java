/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.common;

/**
 *
 * @author JOEL
 */
public class SanitizationHint
{
        private int tokenIndex;
        private GeneralizationResult sanitizedMatches;

        public SanitizationHint(NamedEntity ent, GeneralizationResult result)
        {
                
        }

        public String getText(/*TokenSplitDocument*/ Document d)
        {
                return "";
        }

        public GeneralizationResult getResult()
        {
                return sanitizedMatches;
        }

        public int getTokenIndex()
        {
                return 0;
        }
}
