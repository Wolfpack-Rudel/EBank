package com.expectale.ebank.services;

import com.expectale.ebank.EBank;
import com.expectale.ebank.utils.ItemUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConfigurationService {

    private static final FileConfiguration CONFIG = EBank.getINSTANCE().getConfig();
    
    //MySQLConnection
    public static String SQL_HOST = CONFIG.getString("MySQLConnection.Host");
    public static String SQL_USER = CONFIG.getString("MySQLConnection.User");
    public static String SQL_PASSWORD = CONFIG.getString("MySQLConnection.Password");
    public static String SQL_DATABASE_NAME = CONFIG.getString("MySQLConnection.Database");
    public static int SQL_PORT = CONFIG.getInt("MySQLConnection.Port");
    
    //Account
    public static double INTEREST_TIME = CONFIG.getDouble("Account.Interest-time");
    public static double EARN = CONFIG.getDouble("Account.Interest");
    public static int ACCOUNT_PRICE = CONFIG.getInt("Account.Account-creation-price");
    public static int CARD_PRICE = CONFIG.getInt("Account.Card-price");
    public static int LOG_AMOUNT = CONFIG.getInt("Account.Log-amount");
    public static ItemStack CARD_ITEM = ItemUtils.deserialize(("Account.Card"));
    
    //Menus
    public static ItemStack MENU_COMMON_CLOSE = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Common-items.c")));
    public static ItemStack MENU_COMMON_BACK = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Common-items.b")));
    public static ItemStack MENU_COMMON_BACKGROUND = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Common-items.x")));
    
    public static List<String> MENU_BANK_STRUCTURE = CONFIG.getStringList("Menu.Bank.Structure");
    public static String MENU_BANK_TITLE = CONFIG.getString("Menu.Bank.Title");
    public static ItemStack MENU_BANK_ITEM_INFO = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Bank.Items.i")));
    public static ItemStack MENU_BANK_ITEM_DEPOSIT = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Bank.Items.d")));
    public static ItemStack MENU_BANK_ITEM_WITHDRAW = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Bank.Items.w")));
    public static ItemStack MENU_BANK_ITEM_LOGS = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Bank.Items.l")));
    public static ItemStack MENU_BANK_ITEM_ACCESS_CARD = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Bank.Items.a")));
    public static ItemStack MENU_BANK_ITEM_CODE = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Bank.Items.z")));
    
    public static List<String> MENU_CREATION_STRUCTURE = CONFIG.getStringList("Menu.Creation-menu.Structure");
    public static String MENU_CREATION_TITLE = CONFIG.getString("Menu.Creation-menu.Title");
    public static ItemStack MENU_CREATION_CREATE = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Creation-menu.Items.c")));
    
    public static List<String> MENU_DEPOSIT_STRUCTURE = CONFIG.getStringList("Menu.Deposit.Structure");
    public static String MENU_DEPOSIT_TITLE = CONFIG.getString("Menu.Deposit.Title");
    public static ItemStack MENU_DEPOSIT_1 = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Deposit.Items.1")));
    public static ItemStack MENU_DEPOSIT_2 = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Deposit.Items.2")));
    public static ItemStack MENU_DEPOSIT_3 = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Deposit.Items.3")));
    public static ItemStack MENU_DEPOSIT_4 = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Deposit.Items.4")));
    
    public static List<String> MENU_WITHDRAW_STRUCTURE = CONFIG.getStringList("Menu.Withdraw.Structure");
    public static String MENU_WITHDRAW_TITLE = CONFIG.getString("Menu.Withdraw.Title");
    public static ItemStack MENU_WITHDRAW_1 = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Withdraw.Items.1")));
    public static ItemStack MENU_WITHDRAW_2 = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Withdraw.Items.2")));
    public static ItemStack MENU_WITHDRAW_3 = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Withdraw.Items.3")));
    public static ItemStack MENU_WITHDRAW_4 = ItemUtils.formatItem(ItemUtils.deserialize(("Menu.Withdraw.Items.4")));
    
    //Message
    public static String MESSAGE_NOT_ENOUGH_MONEY = CONFIG.getString("Message.Not-enough-money");
    public static String MESSAGE_WRONG_PASSWORD = CONFIG.getString("Message.Wrong-password");
    public static String MESSAGE_NOT_ENOUGH_INVENTORY_SPACE = CONFIG.getString("Message.Not-enough-inventory_space");
    public static String MESSAGE_BANK_NOT_ENOUGH_MONEY = CONFIG.getString("Message.Bank-not-enough-money");
    public static String MESSAGE_CREATE_PASSWORD = CONFIG.getString("Message.Create-password");
    public static String MESSAGE_TYPE_PASSWORD = CONFIG.getString("Message.Type-password");
    public static String MESSAGE_RESET_PASSWORD = CONFIG.getString("Message.Reset-password");
    public static String MESSAGE_RESET_PASSWORD_NO_PERM = CONFIG.getString("Message.Reset-password-no-permission");
    public static String MESSAGE_WITHDRAW_AMOUNT = CONFIG.getString("Message.Withdraw.Amount");
    public static String MESSAGE_WITHDRAW_SUCCESS = CONFIG.getString("Message.Withdraw.Success");
    public static String MESSAGE_DEPOSIT_AMOUNT = CONFIG.getString("Message.Deposit.Amount");
    public static String MESSAGE_DEPOSIT_SUCCESS = CONFIG.getString("Message.Deposit.Success");
    public static String MESSAGE_FORMAT_ERROR = CONFIG.getString("Message.Format-error");
    public static String MESSAGE_INTEREST = CONFIG.getString("Message.Interest");
    
    //Lore
    public static String LORE_LOG_FORMAT = CONFIG.getString("Lore.Log-format");
    public static String LORE_DATE_FORMAT= CONFIG.getString("Lore.Date-format");

}
