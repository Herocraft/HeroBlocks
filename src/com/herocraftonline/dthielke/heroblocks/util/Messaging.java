package com.herocraftonline.dthielke.heroblocks.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.herocraftonline.dev.heroes.Heroes;

public final class Messaging {

    public static void send(CommandSender player, String msg, Object... params) {
        player.sendMessage(parameterizeMessage(msg, params));
    }

    public static void broadcast(Heroes plugin, String msg, Object... params) {
        plugin.getServer().broadcastMessage(parameterizeMessage(msg, params));
    }

    private static String parameterizeMessage(String msg, Object... params) {
        msg = ChatColor.LIGHT_PURPLE + "HeroBlocks: " + ChatColor.DARK_AQUA + msg;
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                msg = msg.replace("$" + (i + 1), ChatColor.WHITE + params[i].toString() + ChatColor.DARK_AQUA);
            }
        }
        return msg;
    }

}
