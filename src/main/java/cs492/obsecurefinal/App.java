package cs492.obsecurefinal;

import cc.mallet.topics.ParallelTopicModel;
import cs492.obsecurefinal.algorithm.SanitizationSimple;
import cs492.obsecurefinal.builder.InferenceBuilder;
import cs492.obsecurefinal.builder.TopicBuilder;
import cs492.obsecurefinal.common.Agent;
import cs492.obsecurefinal.common.DataSourceNames;
import cs492.obsecurefinal.common.DatabaseAccess;
import cs492.obsecurefinal.common.Document;
import cs492.obsecurefinal.common.HintNoReplacements;
import cs492.obsecurefinal.common.HintWithReplacements;
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

                if(result != null)
                {
                    for(int i = 0; i < result.getResults().size(); i++)
                    {
                        SanitizationHint match = result.getResults().get(i);

                        if(match instanceof HintWithReplacements)
                        {
                            System.out.println("The following phrase may reveal private information:");
                            System.out.println(match.getText());
                            
                            HintWithReplacements hint = (HintWithReplacements)match;
                            String[] results =  hint.getReplacements().getResults();
                            
                            System.out.println("Suggested replacements:");
                            for(int j = 0; j < results.length; j++)
                            {
                                System.out.println(results[j]);
                            }                            
                        }
                        else if (match instanceof HintNoReplacements)
                        {
                            System.out.println("The following sentence contains private data," +
                                               " but cannot be computationally sanitized:");
                            System.out.println(match.getText());
                        } 
                    }
                }
                else
                    System.out.println("An error occured in the sanitization process");
            }
        }
        else if (args.length > 0 && (args[0].equals("-builder") || args[0].equals("-bob")))
        {
            if(args.length == 4)
            {
                System.out.println("Creating master topic model");
                
                try
                {
                    int topics = Integer.parseInt(args[1]);
                    int iterations = Integer.parseInt(args[2]);
                    String folder = args[3];
                    
                    TopicBuilder tb = new TopicBuilder(topics);
                    tb.setIterations(iterations);
                    tb.loadRaw(folder);
                    tb.saveDatabase(DataSourceNames.MASTER_MODEL);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace(System.out);
                }
            }
        }
        else if (args.length > 0 && args[0].equals("-inferencer"))
        {
            if(args.length == 3)
            {
                 // TODO: REMOVE
                TopicBuilder tb = new TopicBuilder(DataSourceNames.MASTER_MODEL);
                ParallelTopicModel model = tb.getModel();        
                // END REMOVE   
                
                InferenceBuilder ib = new InferenceBuilder(model);
                
                try
                {
                    File f = new File(args[1]);
                    
                    if(f.exists() && f.canRead())
                    {
                        System.out.println("Creating inference from contents of " + args[1]);
                        
                        String text = "";
                        String newLine = System.getProperty("line.separator");
                        BufferedReader reader = new BufferedReader(new FileReader(f));
                        String line = reader.readLine();
                        while(line != null)
                        {                            
                            text += line + newLine;
                            line = reader.readLine();
                        }
                    
                        ib.saveInference(text, args[2]);
                        
                        System.out.println("Inference result saved to " + args[2]);
                        
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
