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
package cs492.obsecurefinal.builder;

import cs492.obsecurefinal.common.Document;
import cs492.obsecurefinal.common.Sentence;
import cs492.obsecurefinal.wordngrams.WordNGramExtractor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author JOEL
 */
public class NGramBuilder 
{
    public static int COUNT_NGRAMS = 50;
    
    public NGramBuilder()
    {
        
    }
    
    // Loads a map of ngrams and their weight from a file
    // name is the name of a file containing extracted n-grams
    public HashMap<String, Integer> LoadNGrams(String name)
    {
        HashMap<String, Integer> nGramCount = new HashMap<>();
        
        try
        { 
            File f = new File(name);
            if(f.exists() && f.canRead())
            {
                BufferedReader reader = new BufferedReader(new FileReader(f));

                String line = reader.readLine();
                while(line != null)
                {
                    // ngrams seperated by vertical bar
                    String[] ngrams = line.split("|");
                    
                    for(String ngram: ngrams)
                    {
                        // weight and text separated by colon
                        String[] parts = ngram.split(":");
                        
                        String ngramText = parts[0];
                        int ngramCount = Integer.parseInt(parts[1]);
                        
                        nGramCount.put(ngramText, ngramCount);
                    }
                    
                    line = reader.readLine();
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.out);
        }
        
        return nGramCount;
    }
    
    // extracts ngrams from a set of sentences
    // text is the text to extract n-grams from
    // name is the name of the file to save results to
    public void CreateNGrams(Sentence[] sentences, String name, int maxSize)
    {
        HashMap<String, Integer> nGramCount = new HashMap<>();
        
        for(Sentence s: sentences)
        {       
            // remove punctuation from sentence
            String sentenceNoPunct = s.getText().replaceAll("\\p{Punct}", "");
            
            WordNGramExtractor extractor = new WordNGramExtractor(sentenceNoPunct);
            
            // collect ngrams for each size less than the max size
            for(int size = 0; size < maxSize; size++)
            {
                HashMap<String, Integer> nGramsForSentence = extractor.getAllNGramDistributions(size + 1);

                for(String key: nGramsForSentence.keySet())
                {
                    nGramCount.put(key, (nGramCount.get(key) == null ? 0 : nGramCount.get(key)) + nGramsForSentence.get(key));
                }
            }
        }
        
        Map<String, Integer> sortedMap = sortByValues(nGramCount);
        
        try
        {
            File f = new File(name);
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));

            int i = 0;
            for(String ngram: sortedMap.keySet())
            {
                if(i < COUNT_NGRAMS)
                {
                    if(i > 0)
                        writer.write("|");

                    writer.write(ngram + ":" + sortedMap.get(ngram));

                    i++;
                }
                else
                    break;
            }
            
            writer.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.out);
        }
    }
    
    public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map)
    {
        List<Map.Entry<K,V>> entries = new LinkedList<>(map.entrySet());
     
        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

            @Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
     
        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<>();
     
        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
     
        return sortedMap;
    }
}
