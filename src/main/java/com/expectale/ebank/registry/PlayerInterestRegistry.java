package com.expectale.ebank.registry;

import com.expectale.ebank.EBank;
import com.expectale.ebank.bank.BankAccount;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static com.expectale.ebank.services.ConfigurationService.INTEREST_TIME;

public class PlayerInterestRegistry {
    
    private static final Map<Player, Long> TIME_MAP = new HashMap<>();
    
    public static void enable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                interest();
            }
        }.runTaskTimerAsynchronously(EBank.getINSTANCE(), 60 * 20, 60 * 20);
    }
    
    public static void addPlayer(Player player) {
        TIME_MAP.put(player, System.currentTimeMillis());
    }
    
    public static void removePlayer(Player player) {
        TIME_MAP.remove(player);
    }
    
    public static String timeLeft(Player player) {
        Long startTime = TIME_MAP.get(player);
        
        long time = System.currentTimeMillis();
        long interestTime = (long) INTEREST_TIME * 60 * 60 * 1000;
        
        long timeElapsed = time - startTime;
        long timeRemaining = interestTime - timeElapsed;
        
        if (timeRemaining <= 0) {
            return "Interest ready!";
        }
        
        long hours = timeRemaining / (60 * 60 * 1000);
        long minutes = (timeRemaining % (60 * 60 * 1000)) / (60 * 1000);
        long seconds = (timeRemaining % (60 * 1000)) / 1000;
        
        return String.format("%02d:%02d:%02d remaining", hours, minutes, seconds);
    }
    
    private static void interest() {
        long time = System.currentTimeMillis();
        long interestTime = (long) INTEREST_TIME * 60 * 60 * 1000;
        
        TIME_MAP.entrySet().stream()
            .filter(entry -> time - entry.getValue() >= interestTime)
            .forEach(entry -> {
                Player player = entry.getKey();
                BankAccount bankAccount = BankRegistry.get(player.getUniqueId());
                if (bankAccount != null) bankAccount.interest();
                TIME_MAP.put(player, time);
            });
    }
    
}
