package com.expectale.ebank.commands;

import com.expectale.ebank.bank.BankAccount;
import com.expectale.ebank.menus.BankMenu;
import com.expectale.ebank.menus.CreationBankMenu;
import com.expectale.ebank.registry.BankRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankCreateCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length > 0) return false;
        if (!(sender instanceof Player player)) return false;
        BankAccount bankAccount = BankRegistry.get(player.getUniqueId());
        if (bankAccount == null) new CreationBankMenu(player).open();
        else player.sendMessage("§aYou already have a bank");
        return true;
    }
    
}
