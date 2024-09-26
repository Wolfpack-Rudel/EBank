package com.expectale.ebank.menus;

import com.expectale.ebank.bank.BankAccount;
import com.expectale.ebank.item.BankItem;
import com.expectale.ebank.menus.buttons.BackGroundItem;
import com.expectale.ebank.menus.buttons.CloseButton;
import com.expectale.ebank.registry.BankUserRegistry;
import com.expectale.ebank.services.ConfigurationService;
import com.expectale.ebank.services.EconomyService;
import com.expectale.ebank.utils.ItemUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.window.Window;

import java.util.HashMap;
import java.util.List;

public class BankMenu extends AbstractBankMenu {

    private final BankAccount bankAccount;
    private final Player viewer;
    
    private final AbstractItem depositButton = new DepositButton();
    private final AbstractItem withdrawButton = new WithdrawButton();
    private final AbstractItem logsButton = new LogsButton();
    
    public BankMenu(BankAccount bankAccount, Player viewer) {
        this.bankAccount = bankAccount;
        this.viewer = viewer;
    }
    
    private Gui getGui() {
        List<String> menuBankStructure = ConfigurationService.MENU_BANK_STRUCTURE;
        String[] logArray = menuBankStructure.toArray(new String[0]);
        Structure structure = new Structure(logArray);
        
        return Gui.normal()
            .setStructure(structure)
            .addIngredient('x', new BackGroundItem())
            .addIngredient('d', depositButton)
            .addIngredient('w', withdrawButton)
            .addIngredient('l', logsButton)
            .addIngredient('i', new InfoButton())
            .addIngredient('c', new CloseButton())
            .addIngredient('a', new AccessCardButton())
            .build();
    }
    
    
    @Override
    public void update() {
        depositButton.notifyWindows();
        withdrawButton.notifyWindows();
        logsButton.notifyWindows();
    }
    
    public void open() {
        BankUserRegistry.addUserBank(viewer, bankAccount);
        Window menu = Window.single()
            .setGui(getGui())
            .setTitle(ConfigurationService.MENU_BANK_TITLE)
            .setViewer(viewer)
            .build();
        bankAccount.registerWindow(this, menu);
        menu.open();
    }
    
    class DepositButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(ItemUtils.formatMenuItem(ConfigurationService.MENU_BANK_ITEM_DEPOSIT.clone(), viewer, bankAccount));
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1f);
            new DepositMenu(bankAccount, player).open();
        }
    }
    
    class WithdrawButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(ItemUtils.formatMenuItem(ConfigurationService.MENU_BANK_ITEM_WITHDRAW.clone(), viewer, bankAccount));
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1f);
            new WithdrawMenu(bankAccount, player).open();
        }
    }
    
    class LogsButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            ItemBuilder itemBuilder = new ItemBuilder(ItemUtils.formatMenuItem(ConfigurationService.MENU_BANK_ITEM_LOGS.clone(), viewer, bankAccount));
            List<String> logs = bankAccount.getLogs().getFormattedLogs();
            logs.forEach(itemBuilder::addLoreLines);
            return itemBuilder;
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {}
    }
    
    class InfoButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(ItemUtils.formatMenuItem(ConfigurationService.MENU_BANK_ITEM_INFO.clone(), viewer, bankAccount));
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {}
    }
    
    class AccessCardButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(ItemUtils.formatMenuItem(ConfigurationService.MENU_BANK_ITEM_ACCESS_CARD.clone(), viewer, bankAccount));
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1f);
            Economy economy = EconomyService.getEconomy();
            if (economy.getBalance(player) < ConfigurationService.CARD_PRICE) {
                player.sendMessage(ConfigurationService.MESSAGE_NOT_ENOUGH_MONEY);
                return;
            }
            HashMap<Integer, ItemStack> rest = player.getInventory().addItem(new BankItem(bankAccount).getItem());
            if (!rest.isEmpty()) {
                player.sendMessage(ConfigurationService.MESSAGE_NOT_ENOUGH_INVENTORY_SPACE);
                return;
            }
            economy.withdrawPlayer(player, ConfigurationService.CARD_PRICE);
        }
    }
    
}
