package com.expectale.ebank.menus;

import com.expectale.ebank.EBank;
import com.expectale.ebank.bank.BankAccount;
import com.expectale.ebank.menus.buttons.BackButton;
import com.expectale.ebank.menus.buttons.BackGroundItem;
import com.expectale.ebank.registry.ChatInteractionRegistry;
import com.expectale.ebank.services.ConfigurationService;
import com.expectale.ebank.services.EconomyService;
import com.expectale.ebank.utils.ItemUtils;
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

public class WithdrawMenu extends AbstractBankMenu {
    
    private final BankAccount bankAccount;
    private final Player viewer;
    private final AbstractItem withdrawAll = new WithdrawAllButton();
    private final AbstractItem withdrawHalf = new WithdrawHalfButton();
    private final AbstractItem withdrawQuart = new WithdrawQuartButton();
    private final AbstractItem withdrawCustom = new CustomAmountButton();
    
    public WithdrawMenu(BankAccount bankAccount, Player viewer) {
        this.bankAccount = bankAccount;
        this.viewer = viewer;
    }
    
    private Gui geGui() {
        List<String> menuBankStructure = ConfigurationService.MENU_WITHDRAW_STRUCTURE;
        String[] logArray = menuBankStructure.toArray(new String[0]);
        Structure structure = new Structure(logArray);
        
        return Gui.normal()
            .setStructure(structure)
            .addIngredient('x', new BackGroundItem())
            .addIngredient('1', withdrawAll)
            .addIngredient('2', withdrawHalf)
            .addIngredient('3', withdrawQuart)
            .addIngredient('4', withdrawCustom)
            .addIngredient('b', new BackButton(player -> new BankMenu(bankAccount, player).open()))
            .build();
    }
    
    @Override
    public void update() {
        withdrawAll.notifyWindows();
        withdrawHalf.notifyWindows();
        withdrawQuart.notifyWindows();
        withdrawCustom.notifyWindows();
    }
    
    public void open() {
        Window menu = Window.single()
            .setGui(geGui())
            .setTitle(ConfigurationService.MENU_WITHDRAW_TITLE)
            .setViewer(viewer)
            .build();
        bankAccount.registerWindow(this, menu);
        menu.open();
    }
    
    class WithdrawAllButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(ItemUtils.formatMenuItem(ConfigurationService.MENU_WITHDRAW_1.clone(), viewer, bankAccount));
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1f);
            EconomyService.getEconomy().depositPlayer(player, bankAccount.getAmount());
            bankAccount.removeAmount(player, bankAccount.getAmount());
            bankAccount.updateMenus();
        }
    }
    
    class WithdrawHalfButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(ItemUtils.formatMenuItem(ConfigurationService.MENU_WITHDRAW_2.clone(), viewer, bankAccount));
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1f);
            double amount = bankAccount.getAmount() / 2;
            EconomyService.getEconomy().depositPlayer(player, amount);
            bankAccount.removeAmount(player, amount);
            bankAccount.updateMenus();
        }
    }
    
    class WithdrawQuartButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(ItemUtils.formatMenuItem(ConfigurationService.MENU_WITHDRAW_3.clone(), viewer, bankAccount));
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1f);
            double amount = bankAccount.getAmount() * 0.25;
            EconomyService.getEconomy().depositPlayer(player, amount);
            bankAccount.removeAmount(player, amount);
            bankAccount.updateMenus();
        }
    }
    
    class CustomAmountButton extends AbstractItem {
        
        @Override
        public ItemProvider getItemProvider() {
            return new ItemBuilder(ItemUtils.formatMenuItem(ConfigurationService.MENU_WITHDRAW_4.clone(), viewer, bankAccount));
        }
        
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1f);
            player.closeInventory();
            player.sendMessage(ConfigurationService.MESSAGE_WITHDRAW_AMOUNT);
            ChatInteractionRegistry.add(player, (p, message) -> {
                try {
                    int amount = Integer.parseInt(message);
                    if (amount <= 0) {
                        p.sendMessage(ConfigurationService.MESSAGE_FORMAT_ERROR);
                        return;
                    }
                    
                    if (bankAccount.getAmount() < amount) {
                        p.sendMessage(ConfigurationService.MESSAGE_BANK_NOT_ENOUGH_MONEY);
                        return;
                    }
                    
                    EconomyService.getEconomy().depositPlayer(player, amount);
                    bankAccount.removeAmount(p, amount);
                    p.sendMessage(ConfigurationService.MESSAGE_WITHDRAW_SUCCESS);
                    bankAccount.updateMenus();
                } catch (NumberFormatException e) {
                    p.sendMessage(ConfigurationService.MESSAGE_FORMAT_ERROR);
                } finally {
                    ChatInteractionRegistry.remove(p);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            new WithdrawMenu(bankAccount, p).open();
                        }
                    }.runTask(EBank.getINSTANCE());
                }
            });
        }
    }
}
