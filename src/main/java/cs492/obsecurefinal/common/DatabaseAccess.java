/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs492.obsecurefinal.common;

import java.sql.Connection;

/**
 *
 * @author JOEL
 */
public class DatabaseAccess 
{
    Connection dbConnection;

    public DatabaseAccess()
    {

    }

    public String[] getProfileNames()
    {
        return null;
    }

    public Agent getProfileByName(String name)
    {
        return new Agent("", "", "", "");
    }

    public void saveProfile(String name, String occupation, String workplace, String location)
    {
    
    }
}
