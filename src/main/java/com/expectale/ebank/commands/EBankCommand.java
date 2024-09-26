package com.expectale.ebank.commands;

import com.expectale.ebank.EBank;
import com.expectale.ebank.bank.BankAccount;
import com.expectale.ebank.menus.BankMenu;
import com.expectale.ebank.registry.BankRegistry;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EBankCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length < 1) return false;
        if (!(sender instanceof Player player)) return false;
        
        if (args[0].equals("reload")) {
            EBank.getINSTANCE().reloadConfig();
            player.sendMessage("§aThe config has been reloaded");
            player.sendMessage("§aIf the modification does not appear, restart the server");
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            BankAccount bankAccount = BankRegistry.get(offlinePlayer.getUniqueId());
            if (bankAccount == null) {
                player.sendMessage("§cThis player don't have bank");
                return true;
            }
            new BankMenu(bankAccount, player).open();
        }
        return true;
    }
    
}
