package com.expectale.ebank.listeners;

import com.expectale.ebank.EBank;
import com.expectale.ebank.bank.BankAccount;
import com.expectale.ebank.menus.BankMenu;
import com.expectale.ebank.registry.BankRegistry;
import com.expectale.ebank.registry.ChatInteractionRegistry;
import com.expectale.ebank.services.ConfigurationService;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ItemListeners implements Listener {
    
    @EventHandler
    private void interactEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() != EquipmentSlot.HAND) return;
        ItemStack itemStack = event.getItem();
        if (itemStack == null) return;
        if (!itemStack.hasItemMeta()) return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey key = new NamespacedKey(EBank.getINSTANCE(), "bank-owner");
        if (!itemMeta.getPersistentDataContainer().has(key)) return;
        String id = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        UUID uuid = UUID.fromString(id);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                BankAccount bankAccount = BankRegistry.get(uuid);
                if (bankAccount != null) {
                    if (player.hasPermission("ebank.bank.password_bypass")) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                new BankMenu(bankAccount, player).open();
                            }
                        }.runTask(EBank.getINSTANCE());
                    } else {
                        player.sendMessage(ConfigurationService.MESSAGE_TYPE_PASSWORD);
                        ChatInteractionRegistry.add(player, (p, s) -> {
                            if (bankAccount.getAccessCode().equals(s)) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        new BankMenu(bankAccount, player).open();
                                    }
                                }.runTask(EBank.getINSTANCE());
                            } else {
                                player.sendMessage(ConfigurationService.MESSAGE_WRONG_PASSWORD);
                            }
                        });
                    }
                }
            }
        }.runTaskAsynchronously(EBank.getINSTANCE());
    }
    
}
