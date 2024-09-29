package com.expectale.ebank.utils;

import com.expectale.ebank.EBank;
import com.expectale.ebank.bank.BankAccount;
import com.expectale.ebank.registry.PlayerInterestRegistry;
import com.expectale.ebank.services.ConfigurationService;
import com.expectale.ebank.services.EconomyService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        if (itemMeta.getLore() == null) return itemStack;
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
    
    public static ItemStack deserialize(String path) {
        FileConfiguration config = EBank.getINSTANCE().getConfig();
        if (config.get(path) instanceof ConfigurationSection configurationSection) {
            return deserialize(configurationSection.getValues(false));
        }
        
        else return null;
    }
    
    @NotNull
    public static ItemStack deserialize(@NotNull Map<String, Object> args) {
        short damage = 0;
        int amount = 1;
        
        if (args.containsKey("damage")) {
            damage = ((Number) args.get("damage")).shortValue();
        }
        
        int dataVersion = Bukkit.getUnsafe().getDataVersion();
        Material type = Bukkit.getUnsafe().getMaterial((String) args.get("type"), dataVersion);
        
        if (args.containsKey("amount")) {
            amount = ((Number) args.get("amount")).intValue();
        }
        
        ItemStack result = new ItemStack(type, amount, damage);
        
        if (args.containsKey("enchantments")) {
            Object raw = args.get("enchantments");
            
            if (raw instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) raw;
                
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    String stringKey = entry.getKey().toString();
                    stringKey = Bukkit.getUnsafe().get(Enchantment.class, stringKey);
                    NamespacedKey key = NamespacedKey.fromString(stringKey.toLowerCase(Locale.ROOT));
                    
                    Enchantment enchantment = Bukkit.getUnsafe().get(Registry.ENCHANTMENT, key);
                    
                    if ((enchantment != null) && (entry.getValue() instanceof Integer)) {
                        result.addUnsafeEnchantment(enchantment, (Integer) entry.getValue());
                    }
                }
            }
        } else if (args.containsKey("meta")) {
            Object raw = args.get("meta");
            if (raw instanceof ItemMeta) {
                ((ItemMeta) raw).setVersion(dataVersion);
                result.setItemMeta((ItemMeta) raw);
            }
        }
        
        return result;
    }
    
}
