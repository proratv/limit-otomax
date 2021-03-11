/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 *
 *
 * @author Radinugroho
 *
 */
public class DatabaseConnection {

    private static Connection Connect;

    /**
     *
     * @return @throws java.lang.Exception
     */
    public static Connection getConnection() throws Exception {
        String getServerName;
        String getDatabaseName;
        String getUsername;
        String getPassword;
        String integratedSecurity;
        String serverName;
        String databaseName;
        String username;
        String password;
        int getLastIndex;

        File connectionPath = new File("src\\lib\\Authentication.ini");

        try (BufferedReader data = new BufferedReader(new FileReader(connectionPath))) {
            //data readLine
            getServerName = data.readLine();
            getDatabaseName = data.readLine();
            getUsername = data.readLine();
            getPassword = data.readLine();

            //getServerName
            if (getServerName.substring(getServerName.length() - 1).equals(";")) {
                getLastIndex = getServerName.indexOf(";");
            } else {
                getLastIndex = getServerName.length();
            }
            serverName = getServerName.substring(getServerName.indexOf("=") + 1, getLastIndex).trim();

            //getDatabaseName
            if (getDatabaseName.substring(getDatabaseName.length() - 1).equals(";")) {
                getLastIndex = getDatabaseName.indexOf(";");
            } else {
                getLastIndex = getDatabaseName.length();
            }
            databaseName = getDatabaseName.substring(getDatabaseName.indexOf("=") + 1, getLastIndex).trim();

            //getUsername
            if (getUsername.substring(getUsername.length() - 1).equals(";")) {
                getLastIndex = getUsername.indexOf(";");
            } else {
                getLastIndex = getUsername.length();
            }
            username = getUsername.substring(getUsername.indexOf("=") + 1, getLastIndex).trim();

            //getPassword
            if (getPassword.substring(getPassword.length() - 1).equals(";")) {
                getLastIndex = getPassword.indexOf(";");
            } else {
                getLastIndex = getPassword.length();
            }
            password = getPassword.substring(getPassword.indexOf("=") + 1, getLastIndex).trim();

            //System.out.println(serverName);
            //System.out.println(databaseName);
            //System.out.println(username);
            //System.out.println(password);
        }

        if (username.equals("")) {
            integratedSecurity = "true";
        } else {

            integratedSecurity = "false";
        }

        //System.out.println(integratedSecurity);
        try {
            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            Class.forName(driver);
            //System.out.println("Founded Class Driver");

            try {
                if (integratedSecurity.equals("true")) {
                    Connect = DriverManager.getConnection("jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=" + integratedSecurity);
                    //Connect = DriverManager.getConnection(pathAuthentication + ";integratedSecurity=" + integratedSecurity);
                    System.out.println("Succesfully Database Connection : Windows Authentication");
                } else {
                    Connect = DriverManager.getConnection("jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=" + integratedSecurity, username, password);
                    //Connect = DriverManager.getConnection(pathAuthentication + ";integratedSecurity=" + integratedSecurity, username, password);
                    System.out.println("Succesfully Database Connection : SQL Server Authentication");
                }

            } catch (SQLException se) {
                System.out.println("SQL Server Authentication : Failed Database Connection, error message:" + se);
                System.exit(0);
            }
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Class Not Found, error message: " + cnfe);
            System.exit(0);
        }
        return Connect;
    }
}
