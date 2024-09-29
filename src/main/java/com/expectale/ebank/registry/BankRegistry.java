package com.expectale.ebank.registry;

import com.expectale.ebank.EBank;
import com.expectale.ebank.bank.BankAccount;
import com.expectale.ebank.services.BankService;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BankRegistry {
    
    private final static Map<UUID, BankAccount> ACCOUNTS = new HashMap<>();
    
    public static void enable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                saveAll();
            }
        }.runTaskTimerAsynchronously(EBank.getINSTANCE(), 5 * 60 * 20, 5 * 60 * 20);
    }
    
    public static @Nullable BankAccount get(UUID uuid) {
        return ACCOUNTS.computeIfAbsent(uuid, BankRegistry::load);
    }
    
    public static BankAccount create(UUID uuid, String pass) {
        BankAccount bankAccount = new BankAccount(uuid, pass);
        ACCOUNTS.put(uuid, bankAccount);
        return bankAccount;
    }
    
    public static @Nullable BankAccount load(UUID uuid) {
        return BankService.load(uuid);
    }
    
    public static void remove(BankAccount bankAccount) {
        remove(bankAccount.getOwner());
    }
    
    public static void remove(UUID uuid) {
        BankAccount bankAccount = ACCOUNTS.get(uuid);
        if (bankAccount == null) return;
        save(bankAccount);
        ACCOUNTS.remove(uuid);
    }
    
    public static void save(BankAccount bankAccount) {
        BankService.save(bankAccount);
    }
    
    public static void saveAll() {
        HashMap<UUID, BankAccount> map = new HashMap<>(ACCOUNTS);
        for (Map.Entry<UUID, BankAccount> entry : map.entrySet()) {
            BankAccount bankAccount = entry.getValue();
            if (!entry.getValue().hasUser()) remove(bankAccount);
            else save(bankAccount);
        }
    }
    
}
