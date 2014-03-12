package cs492.obsecurefinal;

import cc.mallet.topics.ParallelTopicModel;
import cs492.obsecurefinal.algorithm.SanitizationSimple;
import cs492.obsecurefinal.builder.InferenceBuilder;
import cs492.obsecurefinal.builder.TopicBuilder;
import cs492.obsecurefinal.common.Agent;
import cs492.obsecurefinal.common.DatabaseAccess;
import cs492.obsecurefinal.common.Document;
import cs492.obsecurefinal.common.SanitizationHint;
import cs492.obsecurefinal.common.SanitizationResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class App 
{
    // Console interface for sanitization prototype
    public static void main( String[] args )
    {
        if(args.length > 0 && args[0].equals("-text"))
        {
            DatabaseAccess dbAccess = new DatabaseAccess();
            
            if(dbAccess.isAvailable())
            {
                String[] allProfiles = dbAccess.getProfileNames();
                Arrays.sort(allProfiles);

                System.out.println("Available profiles:");
                for(String agentName: allProfiles)
                    System.out.println(agentName);

                Scanner scan = new Scanner(System.in);

                System.out.print("Enter profile to use:");
                String selectedAgent = scan.nextLine();

                while(Arrays.binarySearch(allProfiles, selectedAgent) < 0)
                {
                    System.out.println("Invalid profile name");
                    selectedAgent = scan.nextLine();
                }

                Agent currentProfile = dbAccess.getProfileByName(selectedAgent);

                dbAccess.closeConnection();
                
                System.out.print("Enter text to sanitize:");    

                String text = scan.nextLine();
                Document doc = new Document(text);

                SanitizationSimple sanitizer = new SanitizationSimple(doc, currentProfile);
                SanitizationResult result = sanitizer.sanitize();

                for(SanitizationHint match:result.getResults())
                {
                    System.out.print("Term that reveals private information:");
                    System.out.print("Blach bhla");
                }
            }
        }
        else if (args.length > 0 && args[0].equals("-builder"))
        {
            TopicBuilder tb = new TopicBuilder(10);
            tb.setIterations(10);
            tb.loadRaw("modelFiles");
            ParallelTopicModel model = tb.getModel();        
        }
        else if (args.length > 0 && args[0].equals("-inferencer"))
        {
            if(args.length == 3)
            {
                 // TODO: REMOVE
                TopicBuilder tb = new TopicBuilder(10);
                tb.setIterations(10);
                tb.loadRaw("modelFiles");
                ParallelTopicModel model = tb.getModel();        
                // END REMOVE
                
                InferenceBuilder ib = new InferenceBuilder(model);
                
                try
                {
                    File f = new File(args[1]);
                    
                    if(f.exists() && f.canRead())
                    {
                        String text = "", line = "";
                        String newLine = System.getProperty("line.separator");
                        BufferedReader reader = new BufferedReader(new FileReader(f));
                        while(line != null)
                        {
                            line = reader.readLine();
                            text += line + newLine;
                        }
                    
                        ib.saveInference(text, args[2]);
                        
                        reader.close();
                    }                    
                }
                catch(Exception ex)
                {
                    ex.printStackTrace(System.out);
                }                
            }
            else
            {
                System.out.println("-inference <input file> <output file>");
            }
        }
        else
        {
            // launch gui
        }
    }
}
