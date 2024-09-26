package com.expectale.ebank;

import com.expectale.ebank.commands.BankCommand;
import com.expectale.ebank.commands.BankCreateCommand;
import com.expectale.ebank.commands.EBankCommand;
import com.expectale.ebank.commands.tabcompleter.EBankTabCompleter;
import com.expectale.ebank.database.DatabaseCredentials;
import com.expectale.ebank.listeners.ItemListeners;
import com.expectale.ebank.listeners.PlayerListeners;
import com.expectale.ebank.registry.BankRegistry;
import com.expectale.ebank.registry.PlayerInterestRegistry;
import com.expectale.ebank.services.BankService;
import com.expectale.ebank.services.DatabaseService;
import com.expectale.ebank.services.EconomyService;
import com.expectale.ebank.services.LogService;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xenondevs.invui.InvUI;

public final class EBank extends JavaPlugin {

    private static EBank INSTANCE;
    
    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();
        InvUI.getInstance().setPlugin(this);
        
        EconomyService.enable();
        BankRegistry.enable();
        PlayerInterestRegistry.enable();
        
        this.getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        this.getServer().getPluginManager().registerEvents(new ItemListeners(), this);
        
        PluginCommand ebankCommand = this.getCommand("ebank");
        ebankCommand.setExecutor(new EBankCommand());
        ebankCommand.setTabCompleter(new EBankTabCompleter());
        this.getCommand("bank").setExecutor(new BankCommand());
        this.getCommand("bankcreate").setExecutor(new BankCreateCommand());
        
        DatabaseService.connect();
        /*BankService.enable();
        LogService.enable();*/
    }
    
    public static EBank getINSTANCE() {
        return INSTANCE;
    }
    
    @Override
    public void onDisable() {
        BankRegistry.saveAll();
        DatabaseService.close();
    }
}
