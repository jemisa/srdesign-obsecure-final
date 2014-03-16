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

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import edu.mit.jwi.morph.WordnetStemmer;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Benjamin Arnold
 */
public class WordNetDictionary {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);  

    private boolean initialized = false;
    private static final WordNetDictionary instance = new WordNetDictionary();
    private IRAMDictionary dictionary;
    private WordnetStemmer stemmer;
   
    private final POS[] partsOfSpeech = {POS.ADJECTIVE, POS.ADVERB, POS.NOUN, POS.VERB};
    
    private WordNetDictionary() {
	//singleton
	init();
	initialized = true;
    }
    
    public static final WordNetDictionary getInstance() {
	return instance;
    }
    
    public boolean areRelated(String original, String candidate) { 
	EnumMap<POS,Set<String>> originMap = getBaseWordPosMap(original);
	EnumMap<POS,Set<String>> candidMap = getBaseWordPosMap(candidate);
	boolean result = (originMap.isEmpty() || candidMap.isEmpty()) ? true : areRelated(originMap, candidMap);
	return result;
    }
    
    private boolean areRelated(EnumMap<POS,Set<String>> originMap, EnumMap<POS,Set<String>> candidMap) {
	List<IIndexWord> idxWords = getIWords(originMap);
	List<IIndexWord> idx2Words = getIWords(candidMap);
	
	for (IIndexWord idxWord : idxWords) {
	    List<IWordID> wordIDs = idxWord.getWordIDs();
	    for (IIndexWord idx2Word : idx2Words) {
		List<IWordID> word2IDs = idx2Word.getWordIDs();
		for (IWordID wordID : wordIDs) {
		    IWord iWord = dictionary.getWord(wordID);
		    ISynset synset = iWord.getSynset();
		    List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
		    for (ISynsetID hypernym : hypernyms) {
			for (IWordID word2ID: word2IDs) {
			    IWord iWord2 = dictionary.getWord(word2ID);
			    ISynset synset2 = iWord2.getSynset();
			    List<ISynsetID> hypernyms2 = synset2.getRelatedSynsets(Pointer.HYPERNYM);
			    for (ISynsetID hypernym2 : hypernyms2) {
				if (hypernym2.equals(hypernym)) {//if one hypernym in common they are related
				    return true;
				}
			    }
			}
		    }
		}
	    }
	}
	return false;
    }
    
    private List<IIndexWord> getIWords(EnumMap<POS,Set<String>> map) {
	List<IIndexWord> idxWords = new ArrayList<>();
	
	for (POS pos : map.keySet()) {
	    List<IIndexWord> set = getIWord(map.get(pos), pos);
	    idxWords.addAll(set);	    
	}
	return idxWords;
    }
    
    private List<IIndexWord> getIWord(Set<String> words, POS pos) {
	List<IIndexWord> wordSet = new ArrayList<IIndexWord>();
	for (String word : words) {
	    IIndexWord res = dictionary.getIndexWord(word, pos);
	    if (res != null) {
		wordSet.add(res);
	    }
	}
	return wordSet;
    }
      
    private Set<String> getBaseWords(String word, POS pos) {
	Set<String> set = new TreeSet<String>();
	try {
	    List<String> bases = stemmer.findStems(word, pos);
	    set.addAll(bases);
	} catch (IllegalArgumentException ex) {
	    log.log(Level.WARNING,"illegal argument exception {0}", word);
	}
	return set;
    }
	
    
    public Set<String> getBaseWords(String word) {
	Set<String> set = new TreeSet<String>();
	for (POS current : partsOfSpeech) {
	    Set<String> bases = getBaseWords(word, current);
	    set.addAll(bases);
	}
	return set;
    }
    
    public EnumMap<POS,Set<String>> getBaseWordPosMap(String word) {
	EnumMap<POS, Set<String>> map = new EnumMap<POS,Set<String>>(POS.class);
	for (POS current : partsOfSpeech) {
	    Set<String> bases = getBaseWords(word, current);
	    if (bases != null && !bases.isEmpty()) {
		map.put(current, bases);
	    }
	}
	return map;
    }
    
    public boolean isWord(String word) {
	return !getBaseWords(word).isEmpty();
    }
    
    
    private void init() {
	
	try {
	    Handler handler = new FileHandler("WordNetDictionary.log");
	    handler.setFormatter(new SimpleFormatter());
	    log.addHandler(handler);
	    log.setLevel(Level.ALL);
	    
            //get path to resources directory
	    Path path = getFilePath("src/main/resources/dict");
           
            //convert to file reference
            File file = new File(path.toUri());
            
            //load dictionary into memory for fast processing
            //this is done in a singleton to avoid it being loaded multiple times
            dictionary = new RAMDictionary(file, ILoadPolicy.IMMEDIATE_LOAD);
            dictionary.open();
	    stemmer = new WordnetStemmer(dictionary);
	} catch (IOException iox) {
            log.log(Level.WARNING, "Error opening dictionary: {0}", iox);
        } 
    }
    
    private static Path getFilePath(String name) {
            //convert separators to Windows/Unix format depending on host system
        String systemPath = FilenameUtils.separatorsToSystem(name);
	return FileSystems.getDefault().getPath(systemPath, StringUtils.EMPTY);
    }
}
    
