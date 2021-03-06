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
import cs492.obsecurefinal.common.EntityTypes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Benjamin Arnold
 */
public class BrownClusters {
    
    protected static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
    
    private static final BrownClusters instance = new BrownClusters();
    private final TreeMap<String, String> auxillaryClusters = new TreeMap<>();
    private PrimaryCluster primaryCluster;
    
    private BrownClusters() {
	//singleton
	init();
    }
    
    public static final BrownClusters getInstance() {
	return instance;
    }
    
    public boolean coPooled(String word, String word2) {
	boolean coPooled = checkCoPool(auxillaryClusters, word, word2);
	if (!coPooled) {
	    coPooled = primaryCluster.checkCoPool(word, word2);
	}
	if (coPooled) {
	    log.log(Level.FINE, "(coPooled  {0} {1}) : {2}", new Object[]{word, word2, coPooled});
	}
	return coPooled;
    }
    
    private boolean checkCoPool(TreeMap<String, String> cluster, String word, String word2) {
	String aliasA = cluster.get(word);
	String aliasB = cluster.get(word2);
	return ((aliasA != null) && (aliasB != null) && (aliasA.equals(aliasB)));
    }
    
    public String cluster(String word) {
	if (word.contains(" ")) {
	    return clusterSentence(word);
	}
	return primaryCluster.get(word);
    }
    
    public String clusterSentence(String sentence) {
	StringBuilder sb = new StringBuilder();
	String[] oldSentence = sentence.split(" ");
	Pattern p = Pattern.compile("\\b\\W"); //find special character at end of word
	for (String word : oldSentence) {
	    Matcher m = p.matcher(word);
	    String special = StringUtils.EMPTY;
	    if (m.find()) {    //preserve formatting
		special = m.group();
		word = word.substring(0,m.start());
		
	    }
	    String alias = cluster(word);
	    String result = alias != null ? alias : word;
	    if (!word.equals(result)) {
		log.log(Level.FINEST, "{0} clustered to {1}", new Object[]{word, alias});
	    }
	    sb.append(result).append(special).append(" ");
	}
	return sb.toString().trim();
    }
    
    private void init() {
	Handler handler;
	try {
	    handler = new FileHandler("BrownCluster.log");
	    handler.setFormatter(new SimpleFormatter());
	    log.addHandler(handler);
	    log.setLevel(Level.WARNING);
	    
	    ClusterLoadStrategy strategy = new ClusterLoadingStrategy();
	    try {
		strategy.load(this);
	    } catch (FileNotFoundException | ClassNotFoundException fnfx) {
		strategy = new ClusterVettingStrategy();
		strategy.load(this);
	    }
	    
	    TreeMap<String, String> auxCluster = new TreeMap<>();
	    augment(auxCluster, EntityTypes.OCCUPATION);
	    augment(auxCluster, EntityTypes.COMPANY);
	    augment(auxCluster, EntityTypes.LOCATION);
	    putAuxillary(auxCluster);
	} catch (IOException | ClassNotFoundException | SecurityException | IllegalArgumentException ex) {
	    log.log(Level.WARNING, "Error initializing brown clusters: ", ex);
	}
    }
    
    protected void setPrimaryCluster(PrimaryCluster cluster) {
	primaryCluster = cluster;
    }
    
    protected PrimaryCluster getPrimaryCluster() {
	return primaryCluster;
    }
    
    private void addend(TreeMap<String, String> tCluster, String alias, String pool) {
	String properAlias = StringUtils.capitalize(alias);
	tCluster.put(alias, pool);
	tCluster.put(properAlias, pool);
    }
    
    private void augment(TreeMap<String, String> tCluster, EntityTypes type) {
	String name = type.name().toLowerCase();
	
	if (tCluster.containsKey(name)) {
	    tCluster.remove(name);
	}
	addend(tCluster, name, name); //self-alias

	String filename = String.format("src/main/resources/%s.csv", name);
	Charset charset = Charsets.UTF_8;
	try (BufferedReader reader = Files.newBufferedReader(getFilePath(filename), charset)) {
		String line;
		while ((line = reader.readLine()) != null) {
		    StringTokenizer tokenz = new StringTokenizer(line, ",");
		    
		    while (tokenz.hasMoreTokens()) {
			String tok = tokenz.nextToken().trim();
			if (!tCluster.containsKey(tok)) {
			    addend(tCluster, tok, name);
			}
		    }
		}

	} catch (IOException ex) {
	    log.log(Level.WARNING, "Error augmenting brown clusters: ", ex);
	}
	
    }
     
    private void putAuxillary(TreeMap<String, String> auxCluster) {
	auxillaryClusters.putAll(auxCluster);
    }
    
     private static Path getFilePath(String name) {
            //convert separators to Windows/Unix format depending on host system
        String systemPath = FilenameUtils.separatorsToSystem(name);
	return FileSystems.getDefault().getPath(systemPath, StringUtils.EMPTY);
    }

    void setCluster(TreeMap<String, String> tCluster) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  
}
 