/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.common;

import java.util.HashMap;

/**
 *
 * @author JOEL
 */
public class Agent 
{
    String name, occupation, location, workplace;
    HashMap<EntityTypes, String> agentInfo;
    
    public Agent(String name)
    {
        this.name = name;
        agentInfo = new HashMap<EntityTypes, String>();
    }
        
    public Agent()
    {
        name = "";
        //occupation = "";
        //location = "";
        //workplace = "";
        agentInfo = new HashMap<EntityTypes, String>();
    }
    
    public String getName()
    {
        return name;
    }   
       
    public String getCharacteristic(EntityTypes type)
    {
        if(agentInfo.containsKey(type))
        {
            return agentInfo.get(type);
        }
        else
            return "";
    }
    
    public void setName(String name)
    {
        this.name = name;
    }

    public void setCharacteristic(EntityTypes type, String c)
    {
        agentInfo.put(type, c);
    }
}
