package cs492.obsecurefinal;

import cs492.obsecurefinal.algorithm.SanitizationSimple;
import java.util.Scanner;

public class App 
{
    // Console interface for sanitization prototype
    public static void main( String[] args )
    {
        System.out.print("Enter text to sanitize:");
        
        Scanner scan = new Scanner(System.in);
        
        String text = scan.nextLine();
        
        SanitizationSimple sanitizer = new SanitizationSimple(text);
        sanitizer.sanitize();
        
    }
}
