package cs492.obsecurefinal;

import cs492.obsecurefinal.algorithm.SanitizationSimple;
import cs492.obsecurefinal.common.Agent;
import cs492.obsecurefinal.common.DatabaseAccess;
import cs492.obsecurefinal.common.Document;
import java.util.Arrays;
import java.util.Scanner;

public class App 
{
    // Console interface for sanitization prototype
    public static void main( String[] args )
    {
        if(args.length > 0 && !args[0].equals("builder"))
        {
            DatabaseAccess dbAccess = new DatabaseAccess();
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

            System.out.print("Enter text to sanitize:");    

            String text = scan.nextLine();
            Document doc = new Document(text);

            SanitizationSimple sanitizer = new SanitizationSimple(doc, currentProfile);
            sanitizer.sanitize();
        }
    }
}
