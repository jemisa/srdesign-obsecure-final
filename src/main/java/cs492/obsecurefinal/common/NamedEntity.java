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
    
    public NamedEntity(Sentence sentence, Span entitySpan, EntityTypes type)
    {
        this.sentence = sentence;
        this.entitySpan = entitySpan;
        this.type = type;
    }
    
    public EntityTypes getType()
    {
        return type;
    }
    
    public String getText()
    {
        return entitySpan.getCoveredText(sentence.getText()).toString();
    }

    public Span getSpan()
    {
        return entitySpan;
    }
    
    public Sentence getSentence()
    {
        return sentence;
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
