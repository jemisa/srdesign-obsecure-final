package cs492.obsecurefinal.builder;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import cc.mallet.classify.tui.Text2Vectors;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.IDSorter;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Alphabet;

public class TopicBuilder
{
	private boolean loaded;
	private int numTopics;
	private ParallelTopicModel database;
    private int numIterations = 1000;
        
    // Standard constructor
	public TopicBuilder(int num)
	{
		loaded = false;
		numTopics = num;
	}
	
	// Auto-load from directory
	public TopicBuilder(int num, String dir)
	{
		loaded = false;
		numTopics = num;
		loadRaw(dir);
	}
	
	// Auto-load from model file
	public TopicBuilder(String file)
	{
		loaded = false;
		numTopics = 0;
		BufferedReader in;
		
		try
		{
			in = new BufferedReader(new FileReader(file));
		}
		catch(FileNotFoundException e)
		{
			System.err.println("Could not find the specified database file");
			return;
		}
		
		try
		{
			String ln = in.readLine();
			
			// Count the number of topic terminators and use that as the number of topics
			while(ln != null)
			{
				if(ln.equals("THISISTHETOPICTERMINATORIHOPETHISISNEVERUSEDASAWORD"))
				numTopics++;
			}
			
			in.close();
		}
		catch(IOException e)
		{
			System.err.println("Error reading database file");
			return;
		}
		
		// Now use the number of topics to load a file
		loadDatabase(file);
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
			
			// Format is as follows:
			// - Size of typeTopicCounts array, tab-delimited, on its own line
			out.write(database.typeTopicCounts.length + "\t");
			out.write(database.typeTopicCounts[0].length + "\n");
			
			for(int i = 0; i < database.numTopics; i++)
			{
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
		
		// Extra considerations for backup
		database.write(new File(file + ".bak"));
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
			
			String[] tokens = ln.split("\t");
			
			if(tokens.length != 2)
			{
				System.err.println("Incorrectly formatted database file");
				in.close();
				return null;
			}
			
			// Create needed parts of database
			database.alphabet = new Alphabet();
			
			int a = Integer.parseInt(tokens[0]);
			int b = Integer.parseInt(tokens[1]);
			database.typeTopicCounts = new int[a][b];
			
			ln = in.readLine();
			if(ln == null)
			{
				System.err.println("Unexpected end of file");
				in.close();
				return null;
			}

			
			while(ln != null && !ln.equals("THISISTHEFILETERMINATORIHOPETHISISNEVERUSEDASAWORD"))
			{	
				
				if(Integer.parseInt(ln) != i)
				{
					System.err.println("Incorrectly formatted database file");
					in.close();
					return null;
				}
				
				ln = in.readLine();
				if(ln == null)
				{
					System.err.println("Unexpected end of file");
					in.close();
					return null;
				}
				
				// Put the weight of the topic in the database
				database.alpha[i] = Double.parseDouble(ln);
				
				int j = 0;
				
				ln = in.readLine();
				if(ln == null)
				{
					System.err.println("Unexpected end of file");
					in.close();
					return null;
				}
				
				while(ln != null && !ln.equals("THISISTHETOPICTERMINATORIHOPETHISISNEVERUSEDASAWORD"))
				{
					String[] tokens2 = ln.split("\t");
					
					if(tokens2.length != 2)
					{
						System.err.println("Incorrectly formatted database file");
						in.close();
						return null;
					}
					
					// Add tokens[0] (the word) to the word list
					database.alphabet.lookupIndex(tokens2[0]);
					
					// Add tokens[1] (the weight) to the weight indices table
					// This uses a strange format involving bitwise shifts
					//database.typeTopicCounts[i][j] = (j << topicBits) + i;
							
					j++;
					
					ln = in.readLine();
				}
				
				if(ln == null)
				{
					System.err.println("Unexpected end of file");
					in.close();
					return null;
				}
				else if(ln.equals("THISISTHETOPICTERMINATORIHOPETHISISNEVERUSEDASAWORD"))
				{
					ln = in.readLine();
				}
					
				i++;
			}
			
			in.close();
		}
		catch(IOException e)
		{
			System.err.println("Error reading database file");
			return null;
		}
		
		// Extra considerations for backup
		try
		{
			return ParallelTopicModel.read(new File(file + ".bak"));
		}
		catch(Exception e)
		{
			System.err.println("Error reading database file backup");
			return null;
		}
	}
        
    // Added this to be able to customize iterations -Mike
    public void setIterations(int i)
    {
    	numIterations = i;
    }
        
    // Adding this so I can get the model directly -Mike
	public ParallelTopicModel getModel()
	{
		return database;
	}
}