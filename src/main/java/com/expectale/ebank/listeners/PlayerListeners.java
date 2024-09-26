package com.expectale.ebank.listeners;

import com.expectale.ebank.EBank;
import com.expectale.ebank.bank.BankAccount;
import com.expectale.ebank.registry.BankRegistry;
import com.expectale.ebank.registry.BankUserRegistry;
import com.expectale.ebank.registry.ChatInteractionRegistry;
import com.expectale.ebank.registry.PlayerInterestRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListeners implements Listener {
    
    @EventHandler
    private void joinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerInterestRegistry.addPlayer(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                BankAccount bankAccount = BankRegistry.get(player.getUniqueId());
                if (bankAccount != null) bankAccount.addUser(player);
            }
        }.runTaskAsynchronously(EBank.getINSTANCE());
    }
    
    @EventHandler
    private void quitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BankUserRegistry.removeUser(player);
        PlayerInterestRegistry.removePlayer(player);
    }
    
    @EventHandler
    private void asyncChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(ChatInteractionRegistry.execute(player, event.getMessage()));
    }
    
}
