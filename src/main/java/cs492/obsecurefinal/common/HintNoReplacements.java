/*
 * Copyright (C) 2014 JOEL
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

/**
 *
 * @author JOEL
 */
public class HintNoReplacements implements SanitizationHint
{
    private Sentence sentence;
    private double matchValue;
    
    public HintNoReplacements(Sentence s, double val)
    {
        sentence = s;
        matchValue = val;
    }
    
    public Sentence getSentence()
    {
        return sentence;
    }
    
    public double getMatchValue()
    {
        return matchValue;
    }

    @Override
    public String getText() 
    {
        return sentence.getText();
    }
}
