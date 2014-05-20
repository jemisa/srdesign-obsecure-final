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
package cs492.obsecurefinal.common;

import cs492.obsecurefinal.wordngrams.NGram;

/**
 *
 * @author JOEL
 */
public class NGramHintWithReplacement implements SanitizationHint
{
    private NGram origNGram;
    private String newNGramText;
    private double matchValue;
    
    public NGramHintWithReplacement(NGram original, String replacement, double matchValue)
    {
        origNGram = original;
        newNGramText = replacement;
        this.matchValue = matchValue;
    }
    
    @Override
    public String getText() 
    {
        return origNGram.getText();
    }

    public Sentence getSentence()
    {
        return origNGram.getSentence();
    }
    
    @Override
    public double getMatchPercentage()
    {
         return matchValue;
    }
    
    public String getReplacementText()
    {
        return newNGramText;
    }
    
    public String getType()
    {
        return origNGram.getType();
    }
    
}
