package cs492.obsecurefinal.builder;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import cc.mallet.classify.tui.Text2Vectors;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.IDSorter;
import cc.mallet.types.InstanceList;

public class TopicBuilder
{
	private boolean loaded;
	private int numTopics;
	private ParallelTopicModel database;
        private int numIterations = 1000;
        
	public TopicBuilder(int num)
	{
		loaded = false;
		numTopics = num;
	}
	
	public TopicBuilder(int num, String dir)
	{
		loaded = false;
		numTopics = num;
		loadRaw(dir);
	}
	
	public void loadRaw(String dir)
	{
		String[] args = {"--input", dir, "--output", "temp.mallet", "--keep-sequence", "--remove-stopwords"};
		
		// Convert the text files in the directory to .mallet format by executing MALLET's implementation
		try
		{	
			Text2Vectors.main(args);
		}
		catch(IOException e)
		{
			System.err.println("Error converting text files to MALLET format");
			return;
		}
		
		InstanceList training = InstanceList.load(new File("temp.mallet"));

		// Set up topic modeler, using default values except for the number of topics
		database = new ParallelTopicModel(numTopics, 50.0, 0.01);
		database.addInstances(training);
		
		database.setTopicDisplay(50, 20);
		database.setNumIterations(numIterations);
		database.setOptimizeInterval(0);
		database.setBurninPeriod(200);
		database.setSymmetricAlpha(false);
		database.setNumThreads(1);
		
		// Fill the topic modeling database
		try
		{
			database.estimate();
		}
		catch(IOException e)
		{
			System.err.println("Error modeling topics");
			return;
		}
		
		loaded = true;
	}
	
	public void saveDatabase(String file)
	{
		if(!loaded)
		{
			System.err.println("Cannot save database - no database loaded");
			return;
		}
		
		BufferedWriter out;
		
		try
		{
			out = new BufferedWriter(new FileWriter(file));
		}
		catch(IOException e)
		{
			System.err.println("Error opening database file");
			return;
		}
		
		try
		{
			ArrayList<TreeSet<IDSorter>> wordlist = database.getSortedWords();
			
			for(int i = 0; i < database.numTopics; i++)
			{
				// Format is as follows:
				// - Index number of topic on its own line
				out.write(i + "\n");
				// - Weight of topic on its own line
				out.write(Double.toString(database.alpha[i]) + "\n");
				
				TreeSet<IDSorter> wl = wordlist.get(i);
				
				Iterator<IDSorter> it = wl.iterator();
				while(it.hasNext())
				{
					IDSorter word = it.next();
					
					// - Each word, followed by the word's weight, on its own line
					out.write(database.alphabet.lookupObject(word.getID()).toString() + "\t");
					out.write(Double.toString(word.getWeight()) + "\n");
				}
				
				// - Terminator to signify the switch to a new topic
				out.write("THISISTHETOPICTERMINATORIHOPETHISISNEVERUSEDASAWORD\n");
			}
			
			// - Terminator to signify the end of the file has been reached without format errors
			out.write("THISISTHEFILETERMINATORIHOPETHISISNEVERUSEDASAWORD\n");
			out.close();
		}
		catch(IOException e)
		{
			System.err.println("Error writing to database file");
			return;
		}
	}
	
	public ParallelTopicModel loadDatabase(String file)
	{	
		BufferedReader in;
		int topicMask;
		int topicBits;
		
		// Set up the bit masks for the strange format used
		if (Integer.bitCount(numTopics) == 1) {
			// exact power of 2
			topicMask = numTopics - 1;
			topicBits = Integer.bitCount(topicMask);
		}
		else {
			// otherwise add an extra bit
			topicMask = Integer.highestOneBit(numTopics) * 2 - 1;
			topicBits = Integer.bitCount(topicMask);
		}
		
		database = new ParallelTopicModel(numTopics);
		database.alphabet = new Alphabet();
		
		try
		{
			in = new BufferedReader(new FileReader(file));
		}
		catch(FileNotFoundException e)
		{
			System.err.println("Could not find the specified database file");
			return null;
		}
		
		try
		{
			String ln = in.readLine();
			int i = 0;
			
			while(ln != null && !ln.equals("THISISTHEFILETERMINATORIHOPETHISISNEVERUSEDASAWORD"))
			{	
				if(Integer.parseInt(ln) != i)
				{
					System.err.println("Incorrectly formatted database file");
					return null;
				}
				
				ln = in.readLine();
				if(ln == null)
				{
					System.err.println("Unexpected end of file");
					return null;
				}
				
				// Put the weight of the topic in the database
				database.alpha[i] = Double.parseDouble(ln);
				
				int j = 0;
				
				ln = in.readLine();
				if(ln == null)
				{
					System.err.println("Unexpected end of file");
					return null;
				}
				
				while(ln != null && !ln.equals("THISISTHETOPICTERMINATORIHOPETHISISNEVERUSEDASAWORD"))
				{
					String[] tokens = ln.split("\t");
					
					if(tokens.length != 2)
					{
						System.err.println("Incorrectly formatted database file");
						return null;
					}
					
					// Add tokens[0] (the word) to the word list
					database.alphabet.lookupIndex(tokens[0]);
					
					// Add tokens[1] (the weight) to the weight indices table
					// This uses a strange format involving bitwise shifts
					database.typeTopicCounts[i][j] = (j << topicBits) + i;
							
					j++;
				}
				
				if(ln == null)
				{
					System.err.println("Unexpected end of file");
					return null;
				}
					
				i++;
			}
			
			in.close();
			
			if(ln == null)
			{
				System.err.println("Unexpected end of file");
				return null;
			}
		}
		catch(IOException e)
		{
			System.err.println("Error reading database file");
			return null;
		}
		
		return database;
	}
        
        // Added this to be able to customize iterations -Mike
        public void setIterations(int i){
            numIterations = i;
        }
        
        // Adding this so I can get the model directly -Mike
        public ParallelTopicModel getModel(){
            return database;
	}
}