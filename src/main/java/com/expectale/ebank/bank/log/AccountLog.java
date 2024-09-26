package com.expectale.ebank.bank.log;

import com.expectale.ebank.services.ConfigurationService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public record AccountLog(String source, TransactionType type, double amount, long date) {
    
    public String getFormattedLog() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConfigurationService.LORE_DATE_FORMAT);
        Date time = new Date(date);
        
        String loreLogFormat = ConfigurationService.LORE_LOG_FORMAT;
        return loreLogFormat.replace("%logo%", type().getLogo())
            .replace("%amount%", new DecimalFormat("0.00").format(amount))
            .replace("%date%", simpleDateFormat.format(time))
            .replace("%source%", source);
    }
}
