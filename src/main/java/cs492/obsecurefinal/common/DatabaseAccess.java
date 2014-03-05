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

    public static final String GET_NAMES_QUERY = "SELECT PROFILENAME FROM USERPROFILES;";
    public static final String GET_PROFILE_INFO_QUERY = "SELECT LOCATION, OCCUPATION, COMPANY FROM USERPROFILES WHERE PROFILENAME='%s'";
    public static final String SAVE_PROFILE_QUERY = "UPDATE PROFILENAME SET LOCATION='%s', OCCUPATION='%s', COMPANY='%s' WHERE PROFILENAME='%s'";
    
    public static final String LOCATION = "LOCATION";
    public static final String OCCUPATION = "OCCUPATION";
    public static final String WORKPLACE = "COMPANY";
    
    public DatabaseAccess()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            dbConnection = DriverManager.getConnection(DataSourceNames.DB_URL, DataSourceNames.DB_NAME, DataSourceNames.DB_NAME);
            stmt = dbConnection.createStatement();
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            stmt = null;
            dbConnection = null;
        }
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
                    allRows.add(queryResults.getString(0));
                }
                
                return (String[])allRows.toArray();
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
                
                ResultSet queryResults = stmt.executeQuery(GET_NAMES_QUERY);

                agent.setLocation(queryResults.getString(LOCATION));
                agent.setWorkplace(queryResults.getString(WORKPLACE));
                agent.setName(name);
                agent.setOccupation(queryResults.getString(OCCUPATION));
                
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
                String query = String.format(SAVE_PROFILE_QUERY, agent.getLocation(), agent.getOccupation(), agent.getWorkplace(), agent.getName());
                 
                ResultSet queryResults = stmt.executeQuery(GET_NAMES_QUERY);
            }
            catch(Exception ex)
            {
                ex.printStackTrace(System.out);
            }
        }
    }
}
