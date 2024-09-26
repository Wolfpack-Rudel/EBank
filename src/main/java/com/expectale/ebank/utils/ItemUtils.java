package com.expectale.ebank.utils;

import com.expectale.ebank.bank.BankAccount;
import com.expectale.ebank.registry.PlayerInterestRegistry;
import com.expectale.ebank.services.ConfigurationService;
import com.expectale.ebank.services.EconomyService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    
    public static ItemStack formatItem(ItemStack itemStack) {
        if (itemStack == null) return null;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return itemStack;
        List<String> lore = new ArrayList<>();
        List<String> metaLore = itemMeta.getLore();
        if (metaLore == null) return itemStack;
        for (String line : metaLore) {
            lore.add(line.replace("%interest%", String.valueOf(ConfigurationService.EARN))
                .replace("%card-price%", String.valueOf(ConfigurationService.CARD_PRICE))
                .replace("%account-price%", String.valueOf(ConfigurationService.ACCOUNT_PRICE))
                .replace("%interest-time%", String.valueOf(ConfigurationService.INTEREST_TIME)));
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static ItemStack formatMenuItem(ItemStack itemStack, Player player, BankAccount bankAccount) {
        Economy economy = EconomyService.getEconomy();
        
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return itemStack;
        List<String> lore = new ArrayList<>();
        for (String line : itemMeta.getLore()) {
            double balance = economy.getBalance(player);
            lore.add(line.replace("%time-left-before-interest%", PlayerInterestRegistry.timeLeft(player))
                .replace("%player-money-100%", String.valueOf(new DecimalFormat("0.00").format(balance)))
                .replace("%player-money-50%", String.valueOf(new DecimalFormat("0.00").format(balance * 0.5)))
                .replace("%player-money-25%", String.valueOf(new DecimalFormat("0.00").format(balance * 0.25)))
                .replace("%amount%", String.valueOf(new DecimalFormat("0.00").format(bankAccount.getAmount())))
                .replace("%amount-50%", String.valueOf(new DecimalFormat("0.00").format(bankAccount.getAmount())))
                .replace("%amount-25%", String.valueOf(new DecimalFormat("0.00").format(bankAccount.getAmount()))));
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
}
