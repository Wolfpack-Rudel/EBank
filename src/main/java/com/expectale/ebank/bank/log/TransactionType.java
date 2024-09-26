package com.expectale.ebank.bank.log;

public enum TransactionType {
    
    WITHDRAW("§c-"),
    DEPOSIT("§a+"),
    INTEREST("§a+");
    
    private final String logo;
    
    TransactionType(String logo) {
        this.logo = logo;
    }
    
    public String getLogo() {
        return logo;
    }
}
