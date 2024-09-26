package com.expectale.ebank.services;

import com.expectale.ebank.bank.BankAccount;

import java.sql.*;
import java.util.UUID;

public class BankService {
    
    public static void enable() {
        createTable();
    }
    
    private static void createTable() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DatabaseService.connection();
            statement = connection.createStatement();
            
            String sql = "CREATE TABLE IF NOT EXISTS BankAccount ("
                + "owner CHAR(36) NOT NULL, "
                + "amount DOUBLE NOT NULL, "
                + "accessCode VARCHAR(255) NOT NULL, "
                + "PRIMARY KEY (owner))";
            
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static BankAccount load(UUID owner) {
        BankAccount bankAccount = null;
        
        try {
            Connection connection = DatabaseService.connection();
            
            String sql = "SELECT amount, accessCode FROM BankAccount WHERE owner = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, owner.toString());
            
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                double amount = resultSet.getDouble("amount");
                String accessCode = resultSet.getString("accessCode");
                
                bankAccount = new BankAccount(owner, amount, LogService.getLogs(owner), accessCode);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bankAccount;
    }
    
    public static void save(BankAccount bankAccount) {
        PreparedStatement statement;
        
        try {
            Connection connection = DatabaseService.connection();
            
            String checkSql = "SELECT COUNT(*) FROM BankAccount WHERE owner = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, bankAccount.getOwner().toString());
            ResultSet checkResult = checkStatement.executeQuery();
            
            boolean exists = false;
            if (checkResult.next()) {
                exists = checkResult.getInt(1) > 0;
            }
            
            if (exists) {
                String updateSql = "UPDATE BankAccount SET amount = ?, accessCode = ? WHERE owner = ?";
                statement = connection.prepareStatement(updateSql);
                statement.setDouble(1, bankAccount.getAmount());
                statement.setString(2, bankAccount.getAccessCode());
                statement.setString(3, bankAccount.getOwner().toString());
            } else {
                String insertSql = "INSERT INTO BankAccount (owner, amount, accessCode) VALUES (?, ?, ?)";
                statement = connection.prepareStatement(insertSql);
                statement.setString(1, bankAccount.getOwner().toString());
                statement.setDouble(2, bankAccount.getAmount());
                statement.setString(3, bankAccount.getAccessCode());
            }
            
            statement.executeUpdate();
            LogService.saveLogs(bankAccount);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
