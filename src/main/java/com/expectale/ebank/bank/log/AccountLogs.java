package com.expectale.ebank.bank.log;

import com.expectale.ebank.services.ConfigurationService;

import java.util.ArrayList;
import java.util.List;

public class AccountLogs {

    private final List<AccountLog> logs;
    
    public AccountLogs(List<AccountLog> logs) {
        this.logs = logs;
    }
    
    public void addLog(String source, TransactionType type, double amount) {
        logs.add(0, new AccountLog(source, type, amount, System.currentTimeMillis()));
    }
    
    public List<AccountLog> saveLogs() {
        return logs;
    }
    
    public List<String> getFormattedLogs() {
        logs.sort((log1, log2) -> Long.compare(log2.date(), log1.date()));
        
        List<String> list = new ArrayList<>();
        int maxLogs = Math.min(ConfigurationService.LOG_AMOUNT, logs.size());
        
        for (int i = 0; i < maxLogs; i++) {
            list.add(logs.get(i).getFormattedLog());
        }
        
        return list;
    }

}
