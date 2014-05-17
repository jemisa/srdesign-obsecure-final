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
package cs492.obsecurefinal.algorithm;

/**
 *
 * @author JOEL
 */
public class PrivateContextMatch 
{
    double matchAmount;
    String sensitiveText;
    String descriptor;
    
    public PrivateContextMatch(String sensitiveText)
    {
        this.matchAmount = 1.0;
        this.sensitiveText = sensitiveText;
        descriptor = "";
    }
    
    public PrivateContextMatch(double matchAmount, String sensitiveText, String descriptor)
    {
        this.matchAmount = matchAmount;
        this.sensitiveText = sensitiveText;
        this.descriptor = descriptor;
    }
    
    public PrivateContextMatch(double matchAmount, String descriptor)
    {
        this.matchAmount = matchAmount;
        this.descriptor = descriptor;
        this.sensitiveText = "";
    }
    
    public PrivateContextMatch(double matchAmount)
    {
        this.matchAmount = matchAmount;
        this.sensitiveText = "";
        this.descriptor = "";
    }
    
    public double getMatchValue()
    {
        return matchAmount;
    }
    
    public String getSensitiveText()
    {
        return sensitiveText;
    }
    
    public String getDescriptor()
    {
        return descriptor;
    }
}
