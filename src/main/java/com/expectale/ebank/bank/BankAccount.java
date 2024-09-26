package com.expectale.ebank.bank;

import com.expectale.ebank.bank.log.AccountLogs;
import com.expectale.ebank.bank.log.TransactionType;
import com.expectale.ebank.menus.AbstractBankMenu;
import com.expectale.ebank.registry.BankRegistry;
import com.expectale.ebank.services.ConfigurationService;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.window.Window;

import java.util.*;

import static com.expectale.ebank.services.ConfigurationService.EARN;

public class BankAccount {
    
    private final List<Player> users = new ArrayList<>();
    private final UUID owner;
    private final AccountLogs logs;
    private double amount;
    private String accessCode;
    private Map<Window, AbstractBankMenu> openWindows = new HashMap<>();
    
    public BankAccount(UUID owner, double amount, AccountLogs logs, String accessCode) {
        this.owner = owner;
        this.amount = amount;
        this.logs = logs;
        this.accessCode = accessCode;
    }
    
    public BankAccount(UUID owner, String accessCode) {
        this.owner = owner;
        this.amount = 0;
        this.accessCode = accessCode;
        this.logs = new AccountLogs(new ArrayList<>());
    }
    
    public boolean hasUser() {
        return !users.isEmpty();
    }
    
    public void addUser(Player player) {
        users.add(player);
    }
    
    public void removeUser(Player player) {
        users.remove(player);
        if (users.isEmpty()) BankRegistry.remove(this);
    }
    
    public UUID getOwner() {
        return owner;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void addAmount(Player player, double amount) {
        if (amount == 0) return;
        if (!player.hasPermission("ebank.bank.log_bypass")) logs.addLog(player.getName(), TransactionType.DEPOSIT, amount);
        add(amount);
    }
    
    public boolean removeAmount(Player player, double amount) {
        if (amount == 0) return false;
        boolean remove = remove(amount);
        if (remove && !player.hasPermission("ebank.bank.log_bypass")) logs.addLog(player.getName(), TransactionType.WITHDRAW, amount);
        return remove;
    }
    
    public void add(double amount) {
        setAmount(this.amount + amount);
    }
    
    public boolean remove(double amount) {
        if (this.amount < amount) return false;
        setAmount(this.amount - amount);
        return true;
    }
    
    public void interest() {
        double interest = amount * EARN / 100;
        logs.addLog(ConfigurationService.MESSAGE_INTEREST, TransactionType.DEPOSIT, interest);
        setAmount(amount + interest);
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public AccountLogs getLogs() {
        return logs;
    }
    
    public String getAccessCode() {
        return accessCode;
    }
    
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
    
    public void registerWindow(AbstractBankMenu menu, Window window) {
        window.addOpenHandler(() -> openWindows.put(window, menu));
        window.addCloseHandler(() -> openWindows.remove(window));
    }
    
    public void updateMenus() {
        openWindows.values().forEach(AbstractBankMenu::update);
    }
    
}
