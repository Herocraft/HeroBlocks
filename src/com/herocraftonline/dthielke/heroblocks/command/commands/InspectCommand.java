package com.herocraftonline.dthielke.heroblocks.command.commands;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import com.herocraftonline.dthielke.heroblocks.AbstractHeroBlock;
import com.herocraftonline.dthielke.heroblocks.BlockManager;
import com.herocraftonline.dthielke.heroblocks.HeroBlocks;
import com.herocraftonline.dthielke.heroblocks.PermissionManager.Permission;
import com.herocraftonline.dthielke.heroblocks.command.BaseCommand;
import com.herocraftonline.dthielke.heroblocks.util.Messaging;

public class InspectCommand extends BaseCommand {

    private CmdPlayerListener playerListener = new CmdPlayerListener();
    private Set<Player> inspectors = new HashSet<Player>();

    public InspectCommand(HeroBlocks plugin) {
        super(plugin);
        setName("Inspect Block");
        setDescription("Lists information about a Hero Block");
        setUsage("§e/hb inspect");
        setMinArgs(0);
        setMaxArgs(0);
        getIdentifiers().add("hb inspect");

        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Normal, plugin);
        pluginManager.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Normal, plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;
        if (!plugin.getPermissionManager().hasPermission(player, Permission.INSPECT)) {
            Messaging.send(player, "Insufficient permission.");
            return;
        }

        Messaging.send(player, "Left click a block to inspect it.");
        inspectors.add(player);
    }

    public class CmdPlayerListener extends PlayerListener {

        @Override
        public void onPlayerInteract(PlayerInteractEvent e) {
            if (e.isCancelled()) {
                return;
            }

            if (!e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                return;
            }

            Player player = e.getPlayer();
            if (!inspectors.contains(player)) {
                return;
            }

            BlockManager blockManager = plugin.getBlockManager();
            Block block = e.getClickedBlock();
            Location location = block.getLocation();
            AbstractHeroBlock heroBlock = blockManager.getBlockAt(location);
            if (heroBlock == null) {
                inspectors.remove(player);
                Messaging.send(player, "You found an ordinary block!");
                return;
            }

            inspectors.remove(player);

            Messaging.send(player, heroBlock.toString());
        }

        @Override
        public void onPlayerQuit(PlayerQuitEvent e) {
            inspectors.remove(e.getPlayer());
        }

    }

}
