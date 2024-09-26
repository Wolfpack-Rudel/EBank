package com.expectale.ebank.registry;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ChatInteractionRegistry {
    
    private final static Map<Player, BiConsumer<Player, String>> CHAT_INTERACTION = new HashMap<>();
    
    public static void add(Player player, BiConsumer<Player, String> action) {
        CHAT_INTERACTION.put(player, action);
    }
    
    public static void remove(Player player) {
        CHAT_INTERACTION.remove(player);
    }
    
    public static boolean execute(Player player, String value) {
        if (!CHAT_INTERACTION.containsKey(player)) return false;
        CHAT_INTERACTION.get(player).accept(player, value);
        remove(player);
        return true;
    }
    
}
