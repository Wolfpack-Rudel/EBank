package com.expectale.ebank.services;

import com.expectale.ebank.bank.BankAccount;
import com.expectale.ebank.bank.log.AccountLog;
import com.expectale.ebank.bank.log.AccountLogs;
import com.expectale.ebank.bank.log.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class LogService {
    
    public static void enable() {
        createTable();
    }
    
    private static void createTable() {
        try {
            Connection connection = DatabaseService.connection();
            Statement statement = connection.createStatement();
            
            String sql = "CREATE TABLE IF NOT EXISTS AccountLog ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "owner CHAR(36) NOT NULL, "
                + "source VARCHAR(255) NOT NULL, "
                + "type VARCHAR(50) NOT NULL, "
                + "amount DOUBLE NOT NULL, "
                + "date BIGINT NOT NULL, "
                + "FOREIGN KEY (owner) REFERENCES BankAccount(owner))";
            
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static AccountLogs getLogs(UUID owner) {
        List<AccountLog> logs = new ArrayList<>();
        
        try {
            Connection connection = DatabaseService.connection();
            
            String selectSQL = "SELECT source, type, amount, date FROM AccountLog WHERE owner = ?ORDER BY date DESC LIMIT " + ConfigurationService.LOG_AMOUNT;
            PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
            selectStatement.setString(1, owner.toString());
            
            ResultSet resultSet = selectStatement.executeQuery();
            
            while (resultSet.next()) {
                String source = resultSet.getString("source");
                TransactionType type = TransactionType.valueOf(resultSet.getString("type"));
                double amount = resultSet.getDouble("amount");
                long date = resultSet.getLong("date");
                
                logs.add(new AccountLog(source, type, amount, date));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return new AccountLogs(logs);
    }
    
    public static void saveLogs(BankAccount bankAccount) {
        try {
            Connection connection = DatabaseService.connection();
            
            String deleteSQL = "DELETE FROM AccountLog WHERE owner = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSQL);
            deleteStatement.setString(1, bankAccount.getOwner().toString());
            
            deleteStatement.executeUpdate();
            
            String sql = "INSERT INTO AccountLog (owner, source, type, amount, date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            List<AccountLog> allLogs = bankAccount.getLogs().saveLogs();
            allLogs.sort(Comparator.comparingLong(AccountLog::date).reversed());
            
            List<AccountLog> logsToSave = allLogs.stream().limit(ConfigurationService.LOG_AMOUNT).toList();
            
            for (AccountLog log : logsToSave) {
                statement.setString(1, bankAccount.getOwner().toString());
                statement.setString(2, log.source());
                statement.setString(3, log.type().toString());
                statement.setDouble(4, log.amount());
                statement.setLong(5, log.date());
                
                statement.executeUpdate();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
