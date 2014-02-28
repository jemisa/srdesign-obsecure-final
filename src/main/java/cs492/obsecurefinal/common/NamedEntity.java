package cs492.obsecurefinal.common;

/**
 *
 * @author JOEL
 * Represents a named entity from the entity extractor
 */
public class NamedEntity implements Comparable
{
    private EntityTypes type;
    private String contents;
    
    public NamedEntity(String entity, EntityTypes type)
    {
        contents = entity;
        this.type = type;
    }
    
    public EntityTypes getType()
    {
        return type;
    }
    
    public String getContents()
    {
        return contents;
    }

    @Override
    public int compareTo(Object o) {
	int res = -1;
	if (o != null && o instanceof NamedEntity) {
	    NamedEntity entity = (NamedEntity) o;
	    res= contents.compareTo(entity.getContents());
	    if (res == 0) {
		res = type.compareTo(entity.getType());
	    }
	}
	return res;
    }
}
