package com.expectale.ebank.item;

import com.expectale.ebank.EBank;
import com.expectale.ebank.bank.BankAccount;
import com.expectale.ebank.services.ConfigurationService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BankItem {
    
    private final BankAccount bankAccount;
    
    public BankItem(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
    
    public ItemStack getItem() {
        ItemStack itemStack = ConfigurationService.CARD_ITEM;
        NamespacedKey key = new NamespacedKey(EBank.getINSTANCE(), "bank-owner");
        ItemMeta itemMeta = itemStack.getItemMeta();
        
        List<String> lore = new ArrayList<>();
        for (String line : itemMeta.getLore()) {
            lore.add(line.replace("%owner%", Objects.requireNonNull(Bukkit.getOfflinePlayer(bankAccount.getOwner()).getName())));
        }
        itemMeta.setLore(lore);
        
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, bankAccount.getOwner().toString());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
