package cs492.obsecurefinal.common;

/**
 *
 * @author JOEL
 */

// add to enumerated type as needed
public enum EntityTypes
{
    LOCATION, OCCUPATION, COMPANY, MEDICAL(2), DRUG_ALCOHOL, EMOTION, PERSONAL_ATTACKS, STEREOTYPING, FAMILY;
    
    public static final int DEFAULT_LEVEL = 1;
    private int generalizationLevel;
    
    private EntityTypes(int level) {
	generalizationLevel = level;
    }
    
    private EntityTypes() {
	this(DEFAULT_LEVEL);
    }
    
    public int getGeneralizationLevel() {
	return generalizationLevel;
    }
}
