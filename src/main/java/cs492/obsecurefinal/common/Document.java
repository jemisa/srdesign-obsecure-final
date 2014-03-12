/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 *
 * @author JOEL
 */
public class Document
{
    private String text;
    String filename;
    Sentence[] sentences;
    private boolean valid, sentencesCached;

    public Document(String text)
    {
        this.text = text;

        valid = true;
        sentencesCached = false;

        //valid = splitDocument();
    }

    public Document(File file)
    {
        if(file.exists())
        {
            filename = file.getAbsolutePath();

            text = "";

            try
            {
                FileReader fReader = new FileReader(file);

                int c = fReader.read();
                while(c != -1)
                {
                    text += (char)c;
                }

                fReader.close();

                //valid = splitDocument();
                valid = true;
                sentencesCached = false;
            }
            catch(Exception ex)
            {
                ex.printStackTrace(System.out);
                valid = false;
                sentencesCached = false;
            }
        }
    }

    // returns true if succesful, false otherwise
    private boolean splitDocument()
    {
        //FileInputStream modelInput;
        InputStream modelInput;
        SentenceModel sm;

        try
        { 
            //modelInput  = new FileInputStream(DataSourceNames.SENT_MODEL_FILE);
            modelInput = Document.class.getResourceAsStream(DataSourceNames.SENT_MODEL_FILE);

            sm = new SentenceModel(modelInput);
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.out);
            return false;
        }

        try
        {
            if(modelInput != null)            
                modelInput.close();
        }
        catch(Exception ex)
        {                
            ex.printStackTrace(System.out);
            return false;
        }

        if(sm != null)
        {
            SentenceDetectorME detector = new SentenceDetectorME(sm);

            String[] sentenceStrings = detector.sentDetect(text);                
            sentences = Sentence.convertStringArray(sentenceStrings);

            return true;
        }
        else
            return false;

    }

    public void setText(String newText)
    {
        this.text = newText;
        valid = true;
        sentencesCached = false;
    }

    public boolean isValid()
    {
        return valid;
    }

    public void saveToFile(File f)
    {

    }

    public String getText()
    {
        if(valid)
            return text;
        else
            return "";
    }
        
    public Sentence[] getSentences()
    {
        if(valid)
        {
            if(sentencesCached)
                return sentences;
            else
            {
                if(splitDocument())
                {
                    sentencesCached = true;
                    valid = true;
                    return sentences;                    
                }
                else
                {
                    valid = false;
                    sentencesCached = false;
                    return new Sentence[] {};                
                }
            }
        }
        else
            return new Sentence[] {};
    }
}
