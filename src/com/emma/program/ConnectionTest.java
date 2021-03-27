/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emma.program;

import lib.DatabaseConnection;

/**
 *
 * @author Radinugroho
 */
public class ConnectionTest {

    /**
     *
     * @param args the command line arguments
     * @throws java.lang.Exception
     *
     */
    public static void main(String[] args) throws Exception {
        String dbName = "EmmaDB";

        DatabaseConnection connection = new DatabaseConnection();
        DatabaseConnection.getConnection(dbName);
    }
}
