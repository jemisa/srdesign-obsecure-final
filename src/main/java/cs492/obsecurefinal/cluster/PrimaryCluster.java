/*
 * Copyright (C) 2014 Benjamin Arnold
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

package cs492.obsecurefinal.cluster;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeMap;

/**
 *
 * @author Benjamin Arnold
 */
public class PrimaryCluster implements Serializable {
    private static final long SERIAL_VERSION_UID = 7526472295622776147L;
    
    private TreeMap<String, String> primaryCluster = new TreeMap<>();
    
    protected PrimaryCluster(TreeMap<String, String> tCluster) {
	primaryCluster = tCluster;
//	list();
    }
    
    protected boolean checkCoPool(String word, String word2) {
	String aliasA = primaryCluster.get(word);
	String aliasB = primaryCluster.get(word2);
	return ((aliasA != null) && (aliasB != null) && (aliasA.equals(aliasB)));
    }
    
    protected String get(String word) {
	return primaryCluster.get(word);
    }
    
    private void list() {
	System.out.println( "Listing cluster mappings:");
	for (String key : primaryCluster.keySet()) {
	    System.out.println(String.format("%s => %s", key, primaryCluster.get(key)));
	}
    }
    
    protected void put(TreeMap<String, String> tCluster ) {
	Collection<String> values = tCluster.values();
	
	for (String alias : tCluster.keySet()) {
	    if (!values.contains(alias)) {
		primaryCluster.put(alias, tCluster.get(alias));
	    }
	}
    }
    
}
