package com.expectale.ebank.menus;

import com.expectale.ebank.EBank;
import com.expectale.ebank.bank.BankAccount;
import com.expectale.ebank.menus.buttons.BackGroundItem;
import com.expectale.ebank.registry.BankRegistry;
import com.expectale.ebank.registry.ChatInteractionRegistry;
import com.expectale.ebank.services.ConfigurationService;
import com.expectale.ebank.services.EconomyService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public class CreationBankMenu {
    
    private final Player viewer;
    
    public CreationBankMenu(Player viewer) {
        this.viewer = viewer;
    }
    
    private Gui getGui() {
        List<String> menuBankStructure = ConfigurationService.MENU_CREATION_STRUCTURE;
        String[] logArray = menuBankStructure.toArray(new String[0]);
        Structure structure = new Structure(logArray);
        
        return Gui.normal()
            .setStructure(structure)
            .addIngredient('x', new BackGroundItem())
            .addIngredient('c', new CreateButton())
            .build();
    }
    
    public void open() {
        Window menu = Window.single()
            .setGui(getGui())
            .setTitle(ConfigurationService.MENU_CREATION_TITLE)
            .setViewer(viewer)
            .build();
        menu.open();
    }
    
    static class CreateButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(ConfigurationService.MENU_CREATION_CREATE);
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1f);
            Economy economy = EconomyService.getEconomy();
            double amount = economy.getBalance(player);
            if (amount < ConfigurationService.ACCOUNT_PRICE) {
                player.sendMessage(ConfigurationService.MESSAGE_NOT_ENOUGH_MONEY);
            } else {
                player.sendMessage(ConfigurationService.MESSAGE_CREATE_PASSWORD);
                player.closeInventory();
                ChatInteractionRegistry.add(player, (p, s) -> {
                    Economy eco = EconomyService.getEconomy();
                    if (eco.getBalance(player) < ConfigurationService.ACCOUNT_PRICE) {
                        player.sendMessage(ConfigurationService.MESSAGE_NOT_ENOUGH_MONEY);
                        new CreationBankMenu(p).open();
                        return;
                    }
                    
                    BankAccount account = BankRegistry.create(p.getUniqueId(), s);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            new BankMenu(account, p).open();
                        }
                    }.runTask(EBank.getINSTANCE());
                });
            }
        }
    }
    
}
