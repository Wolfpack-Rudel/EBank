package com.expectale.ebank.menus.buttons;

import com.expectale.ebank.services.ConfigurationService;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.function.Consumer;

public class BackButton extends AbstractItem {
    
    private final Consumer<Player> click;
    
    public BackButton(Consumer<Player> click) {
        this.click = click;
    }
    
    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(ConfigurationService.MENU_COMMON_BACK);
    }
    
    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1f);
        click.accept(player);
    }
}
