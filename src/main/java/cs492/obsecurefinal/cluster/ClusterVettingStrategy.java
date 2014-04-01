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

import cs492.obsecurefinal.common.DataSourceNames;
import static cs492.obsecurefinal.common.DataSourceNames.CLUSTERING_DATA_VETTED;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.StringTokenizer;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Vets a PrimaryCluster based on hypernyms
 * Used when cluster-vetted.ser does not exist or is out of date
 * Produces a cluster-vetted.ser for future use
 * Loads Cluster
 * @author Benjamin Arnold
 */
public class ClusterVettingStrategy implements ClusterLoadStrategy {

    @Override
    public void load(BrownClusters brownCluster) throws IOException {
	InputStream inStream = BrownClusters.class.getResourceAsStream(DataSourceNames.CLUSTERING_DATA);
	BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));  
	StringBuilder sb = new StringBuilder();
	String line;
	while ((line = reader.readLine()) != null) {
	    sb.append(line);
	}
	

	Document doc = Jsoup.parse(sb.toString());
	Element table = doc.getElementsByTag("table").get(0).child(0);
	Elements rows = table.children();
	
	TreeMap<String, String> tCluster = new TreeMap<>();
	for (Element row : rows) {
	    Elements contents = row.getElementsByTag("td");
	    if (contents.size() > 0) {
		Element valuesCell = contents.last();
		String valuesText = valuesCell.text();
		StringTokenizer tokens = new StringTokenizer(valuesText, " ");
		String key = tokens.nextToken();
		tCluster.put(key, key);  //self alias
		while (tokens.hasMoreTokens()) {
		    String token = tokens.nextToken();
		    putWord(tCluster, key, token);
		}
	    }
	}
	PrimaryCluster cluster = new PrimaryCluster(tCluster);
	backup(cluster);
    }
    
     private void putWord(TreeMap<String, String> tCluster, String key, String word) {
	String token = Filter.scrub(word, Filter.TAG, Filter.APOSTRAPHE, Filter.UNDERSCORE);
	if (token != null && !StringUtils.EMPTY.equals(token)) {
	    if (WordNetDictionary.getInstance().areRelated(key, token)) {
		if (!(tCluster.containsValue(token))) {
		    tCluster.put(token, key);  //alias all results to intended key
		}
		
	    } 
	}
    }
    
    private void backup(PrimaryCluster cluster) throws FileNotFoundException, IOException {
	String filename = String.format("src/main/resources/%s", CLUSTERING_DATA_VETTED);
	ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
	out.writeObject(cluster);
	out.close();
    }
    
}
