package com.expectale.ebank.services;

import com.expectale.ebank.database.DatabaseCredentials;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {
    
    private static Connection CONNECTION;
    
    public static void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            CONNECTION = DriverManager.getConnection(
                DatabaseCredentials.toURI(),
                DatabaseCredentials.getUser(),
                DatabaseCredentials.getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void close() {
        if (CONNECTION == null) return;
        try {
            if (CONNECTION.isClosed()) return;
            CONNECTION.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static Connection connection() {
        try {
            if (CONNECTION != null && !CONNECTION.isClosed()) return CONNECTION;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connect();
        return CONNECTION;
    }
}
