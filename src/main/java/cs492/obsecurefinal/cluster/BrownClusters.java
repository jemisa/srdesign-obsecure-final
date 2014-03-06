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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Benjamin Arnold
 */
public class BrownClusters {
    private static final BrownClusters instance = new BrownClusters();
    
    private TreeMap<String, String> cluster = new TreeMap<>();
    
    private BrownClusters() {
	//singleton
	init();
    }
    
    public static final BrownClusters getInstance() {
	return instance;
    }
    
    public String cluster(String word) {
	return cluster.get(word);
    }
    
    private void init() {
	InputStream inStream = BrownClusters.class.getResourceAsStream("/cluster_viewer.html");
	BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));  
	StringBuilder sb = new StringBuilder();
	String line = null;
	try {
	    while ((line = reader.readLine()) != null) {
		sb.append(line);
	    }
	} catch (IOException ex) {
	    Logger.getLogger(BrownClusters.class.getName()).log(Level.SEVERE, null, ex);
	}
	
	Document doc = Jsoup.parse(sb.toString());
	Element table = doc.getElementsByTag("table").get(0).child(0);
	Elements rows = table.children();
	
	for (Element row : rows) {
	    Elements contents = row.getElementsByTag("td");
	    if (contents.size() > 0) {
		Element valuesCell = contents.last();
		String valuesText = valuesCell.text();
		StringTokenizer tokens = new StringTokenizer(valuesText, " ");
		String key = tokens.nextToken();
		while (tokens.hasMoreTokens()) {
		    cluster.put(tokens.nextToken(), key);  //alias all results to intended key
		}
	    }
	}
    }
    
}
