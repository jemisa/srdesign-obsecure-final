/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JOEL
 */
public class DatabaseAccess 
{
    Connection dbConnection;
    Statement stmt;

    private boolean valid;
    
    public static final String GET_NAMES_QUERY = "SELECT PROFILENAME FROM USERPROFILES";
    public static final String GET_PROFILE_INFO_QUERY = "SELECT LOCATION, OCCUPATION, COMPANY FROM USERPROFILES WHERE PROFILENAME='%s'";
    public static final String SAVE_PROFILE_QUERY = "UPDATE PROFILENAME SET LOCATION='%s', OCCUPATION='%s', COMPANY='%s' WHERE PROFILENAME='%s'";
    
    //public static final String LOCATION = "LOCATION";
    //public static final String OCCUPATION = "OCCUPATION";
    //public static final String WORKPLACE = "ORGANIZATION";
    
    
    public DatabaseAccess()
    {
        try
        {             
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            dbConnection = DriverManager.getConnection(DataSourceNames.DB_URL, DataSourceNames.DB_USER, DataSourceNames.DB_PWORD);
            stmt = dbConnection.createStatement();
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.out);
            stmt = null;
            dbConnection = null;
            valid = false;
        }
        
        valid = true;
    }
    
    public boolean isAvailable()
    {
        return valid;
    }
    
    public void closeConnection()
    {
        if(dbConnection != null && stmt != null)
        {
            try
            {
                stmt.closeOnCompletion();
            }
            catch(Exception ex)
            {
                ex.printStackTrace(System.out);
            }
               
        }
        
    }

    public String[] getProfileNames()
    {
        if(dbConnection != null)
        {
            try
            {
                ResultSet queryResults = stmt.executeQuery(GET_NAMES_QUERY);
                List<String> allRows = new ArrayList<String>();
                                
                while(queryResults.next())
                {
                    allRows.add(queryResults.getString("PROFILENAME"));
                }
                
                String[] rowsArray = new String[allRows.size()];
                return allRows.toArray(rowsArray);
            }
            catch(Exception ex)
            {
                ex.printStackTrace(System.out);
                return new String[] {};
            }
        }
        else 
            return new String[] {};
    }

    public Agent getProfileByName(String name)
    {
        if(dbConnection != null)
        {
            try
            {
                String query = String.format(GET_PROFILE_INFO_QUERY, name);
                Agent agent = new Agent();
                
                ResultSet queryResults = stmt.executeQuery(query);

                if(queryResults.next())
                {
                    agent.setCharacteristic(EntityTypes.LOCATION, queryResults.getString(EntityTypes.LOCATION.toString()));
                    agent.setCharacteristic(EntityTypes.ORGANIZATION, queryResults.getString(EntityTypes.ORGANIZATION.toString()));
                    agent.setName(name);
                    agent.setCharacteristic(EntityTypes.OCCUPATION, queryResults.getString(EntityTypes.OCCUPATION.toString()));
                }
                
                return agent;
            }
            catch(Exception ex)
            {
                ex.printStackTrace(System.out);
                return null;
            }
        }
        else
            return null;
    }

    public void saveProfile(Agent agent)
    {
        if(dbConnection != null)
        {
            try
            {
                String query = String.format(SAVE_PROFILE_QUERY, agent.getCharacteristic(EntityTypes.LOCATION), 
                                             agent.getCharacteristic(EntityTypes.OCCUPATION), 
                                             agent.getCharacteristic(EntityTypes.ORGANIZATION), agent.getName());
                 
                ResultSet queryResults = stmt.executeQuery(GET_NAMES_QUERY);
            }
            catch(Exception ex)
            {
                ex.printStackTrace(System.out);
            }
        }
    }
}
