/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.common;

/**
 *
 * @author JOEL
 */
public class Agent 
{
    String name, occupation, location, workplace;
    
    public Agent(String name, String occupation, String location, String workplace)
    {
        this.name = name;
        this.occupation = occupation;
        this.location = location;
        this.workplace = workplace;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getLocation()
    {
        return location;
    }
    
    public String getOccupation()
    {
        return occupation;
    }
    
    public String getWorkplace()
    {
        return workplace;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }

    public void setOccupation(String occupation)
    {
        this.occupation = occupation;
    }

    public void setWorkplace(String workplace)
    {
        this.workplace = workplace;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }
}
