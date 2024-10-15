package com.expectale.ebank.menus;

import com.expectale.ebank.EBank;
import com.expectale.ebank.bank.BankAccount;
import com.expectale.ebank.menus.buttons.BackButton;
import com.expectale.ebank.menus.buttons.BackGroundItem;
import com.expectale.ebank.registry.ChatInteractionRegistry;
import com.expectale.ebank.services.ConfigurationService;
import com.expectale.ebank.services.EconomyService;
import com.expectale.ebank.utils.ItemUtils;
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

public class DepositMenu extends AbstractBankMenu {

    private final BankAccount bankAccount;
    private final Player viewer;
    private final AbstractItem depositAll = new DepositAllButton();
    private final AbstractItem depositHalf = new DepositHalfButton();
    private final AbstractItem depositQuart = new DepositQuartButton();
    private final AbstractItem depositCustom = new CustomAmountButton();
    
    public DepositMenu(BankAccount bankAccount, Player viewer) {
        this.bankAccount = bankAccount;
        this.viewer = viewer;
    }

    private Gui getGui() {
        List<String> menuBankStructure = ConfigurationService.MENU_DEPOSIT_STRUCTURE;
        String[] logArray = menuBankStructure.toArray(new String[0]);
        Structure structure = new Structure(logArray);
        
        return Gui.normal()
            .setStructure(structure)
            .addIngredient('x', new BackGroundItem())
            .addIngredient('1', depositAll)
            .addIngredient('2', depositHalf)
            .addIngredient('3', depositQuart)
            .addIngredient('4', depositCustom)
            .addIngredient('b', new BackButton(player -> new BankMenu(bankAccount, player).open()))
            .build();
    }
    
    @Override
    public void update() {
        depositAll.notifyWindows();
        depositHalf.notifyWindows();
        depositQuart.notifyWindows();
        depositCustom.notifyWindows();
    }
    
    public void open() {
        Window menu = Window.single()
            .setGui(getGui())
            .setTitle(ConfigurationService.MENU_DEPOSIT_TITLE)
            .setViewer(viewer)
            .build();
        bankAccount.registerWindow(this, menu);
        menu.open();
    }
    
    class DepositAllButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(ItemUtils.formatMenuItem(ConfigurationService.MENU_DEPOSIT_1.clone(), viewer, bankAccount));
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1f);
            Economy economy = EconomyService.getEconomy();
            double amount = economy.getBalance(player);
            bankAccount.addAmount(player, amount);
            economy.withdrawPlayer(player, amount);
            bankAccount.updateMenus();
        }
        
        @Override
        public void notifyWindows() {
            super.notifyWindows();
        }
    }
    
    class DepositHalfButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(ItemUtils.formatMenuItem(ConfigurationService.MENU_DEPOSIT_2.clone(), viewer, bankAccount));
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1f);
            Economy economy = EconomyService.getEconomy();
            double amount = economy.getBalance(player) / 2;
            bankAccount.addAmount(player, amount);
            economy.withdrawPlayer(player, amount);
            bankAccount.updateMenus();
        }
    }
    
    class DepositQuartButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(ItemUtils.formatMenuItem(ConfigurationService.MENU_DEPOSIT_3.clone(), viewer, bankAccount));
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1f);
            Economy economy = EconomyService.getEconomy();
            double amount = economy.getBalance(player) * 0.25;
            bankAccount.addAmount(player, amount);
            economy.withdrawPlayer(player, amount);
            bankAccount.updateMenus();
        }
    }
    
    class CustomAmountButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(ItemUtils.formatMenuItem(ConfigurationService.MENU_DEPOSIT_4.clone(), viewer, bankAccount));
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1f);
            player.closeInventory();
            player.sendMessage(ConfigurationService.MESSAGE_DEPOSIT_AMOUNT);
            ChatInteractionRegistry.add(player, (p, message) -> {
                try {
                    int amount = Integer.parseInt(message);
                    if (amount <= 0) {
                        p.sendMessage(ConfigurationService.MESSAGE_FORMAT_ERROR);
                        return;
                    }
                    Economy economy = EconomyService.getEconomy();
                    
                    if (economy.getBalance(p) < amount) {
                        p.sendMessage(ConfigurationService.MESSAGE_NOT_ENOUGH_MONEY);
                        return;
                    }
                    
                    EconomyService.getEconomy().withdrawPlayer(player, amount);
                    bankAccount.addAmount(p, amount);
                    p.sendMessage(ConfigurationService.MESSAGE_DEPOSIT_SUCCESS);
                    bankAccount.updateMenus();
                } catch (NumberFormatException e) {
                    p.sendMessage(ConfigurationService.MESSAGE_FORMAT_ERROR);
                } finally {
                    ChatInteractionRegistry.remove(p);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            new DepositMenu(bankAccount, p).open();
                        }
                    }.runTask(EBank.getINSTANCE());
                }
            });
        }
    }
    
}
