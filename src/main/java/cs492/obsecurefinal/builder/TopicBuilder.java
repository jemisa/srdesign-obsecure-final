package cs492.obsecurefinal.builder;

import java.io.*;

import cc.mallet.classify.tui.Text2Vectors;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.InstanceList;

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

            loadDatabase(file);

            numTopics = database.numTopics;
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

            File f = new File(file);
            if(!f.exists() || f.canWrite())
            {
                database.write(f);
            }       
	}
	
	public ParallelTopicModel loadDatabase(String file)
	{	
            try
            {
                File f = new File(file);
                if(f.exists() && f.canRead())
                {
                    database = ParallelTopicModel.read(f);
                    return database;
                }
                else
                    return null;
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace(System.out);
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