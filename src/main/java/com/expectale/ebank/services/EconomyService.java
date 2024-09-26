package com.expectale.ebank.services;

import com.expectale.ebank.EBank;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class EconomyService {
    
    private static Economy econ = null;
    
    public static void enable() {
        if (!setupEconomy() ) {
            EBank instance = EBank.getINSTANCE();
            getServer().getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", instance.getDescription().getName()));
            getServer().getPluginManager().disablePlugin(instance);
        }
    }
    
    private static boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }
    
    public static Economy getEconomy() {
        return econ;
    }
    
}
