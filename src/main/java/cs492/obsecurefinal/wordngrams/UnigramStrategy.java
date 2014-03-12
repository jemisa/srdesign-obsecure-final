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
package cs492.obsecurefinal.wordngrams;

import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.types.InstanceList;
import cs492.obsecurefinal.common.DataSourceNames;
import java.io.File;
import java.util.HashMap;

/**
 *
 * @author JOEL
 */
public class UnigramStrategy extends NGramStrategy
{
    @Override
    public HashMap<String, Integer> getNGramDistribution(String sentence)
    { 
        String[] words = sentence.split(" ");
        
        TokenSequenceRemoveStopwords stp = new TokenSequenceRemoveStopwords(new File(DataSourceNames.TOPICS_STOPWORDS), "UTF-8", false, false, false);
        InstanceList iList = new InstanceList(stp);
        //iList.
        
        return distribution;
    }
}
