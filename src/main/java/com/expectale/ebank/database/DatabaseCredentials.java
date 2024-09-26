package com.expectale.ebank.database;

import com.expectale.ebank.services.ConfigurationService;

public class DatabaseCredentials {
    
    public static String toURI() {
        return new StringBuilder()
            .append("jdbc:mysql://")
            .append(ConfigurationService.SQL_HOST)
            .append(":")
            .append(ConfigurationService.SQL_PORT)
            .append("/")
            .append(ConfigurationService.SQL_DATABASE_NAME)
            .toString();
    }
    
    public static String getUser() {
        return ConfigurationService.SQL_USER;
    }
    
    public static String getPassword() {
        return ConfigurationService.SQL_PASSWORD;
    }
}
