package cs492.obsecurefinal;

/**
 *
 * @author JOEL
 * Represents a named entity from the entity extractor
 */
public class NamedEntity
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
}
