package cs492.obsecurefinal.common;

import opennlp.tools.util.Span;

/**
 *
 * @author JOEL
 * Represents a named entity from the entity extractor
 */
public class NamedEntity implements Comparable
{
    private EntityTypes type;
    private Sentence sentence;
    private Span entitySpan;
    private String[] words;
    
    public NamedEntity(Sentence sentence, Span entitySpan, EntityTypes type)
    {
        this.sentence = sentence;
        this.entitySpan = entitySpan;
        this.type = type;
        words = sentence.getText().split(" ");
    }
    
    public EntityTypes getType()
    {
        return type;
    }
    
    public String getText()
    {
        //return entitySpan.get
        String text = "";
        
        boolean wordAdded = false;
        for(int i = 0; i < words.length; i++)
        {
            if(i >= entitySpan.getStart() && i < entitySpan.getEnd())
            {
                if(wordAdded)
                    text += " ";

                text += words[i];
                wordAdded = true;
            }
        }
        
        return text;
    }

    private Span getSpan()
    {
        return entitySpan;
    }
    
    public Sentence getSentence()
    {
        return sentence;
    }
    
    public Sentence getSentenceNoEntity()
    {
        String newSentence = "";
        
        boolean wordAdded = false;
        for(int i = 0; i < words.length; i++)
        {
            if(i < entitySpan.getStart() || i >= entitySpan.getEnd())
            {
                if(wordAdded)
                    newSentence += " ";

                newSentence += words[i];
                wordAdded = true;
            }
        }
        
        return new Sentence(newSentence, sentence.getIndex());
    }
    
    @Override
    public int compareTo(Object o) {
	int res = -1;
	if (o != null && o instanceof NamedEntity) {
	    NamedEntity entity = (NamedEntity) o;
	    res= getText().compareTo(entity.getText());
	    if (res == 0) {
		res = type.compareTo(entity.getType());
	    }
	}
	return res;
    }
}
