package com.expectale.ebank.registry;

import com.expectale.ebank.bank.BankAccount;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankUserRegistry {
    
    private static final Map<Player, List<BankAccount>> BANK_USER = new HashMap<>();
    
    public static void addUserBank(Player player, BankAccount account) {
        List<BankAccount> accounts = BANK_USER.computeIfAbsent(player, k -> new ArrayList<>());
        if (accounts.contains(account)) return;
        accounts.add(account);
    }
    
    public static void removeUser(Player player) {
        if (!BANK_USER.containsKey(player)) return;
        for (BankAccount bankAccount : BANK_USER.remove(player)) {
            bankAccount.removeUser(player);
        }
    }
    
}
