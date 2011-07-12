package com.herocraftonline.dthielke.heroblocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

import com.herocraftonline.dthielke.heroblocks.PermissionManager.Permission;
import com.herocraftonline.dthielke.heroblocks.util.Messaging;

public class HBBlockListener extends BlockListener {

    private final HeroBlocks plugin;

    public HBBlockListener(HeroBlocks plugin) {
        this.plugin = plugin;
    }

    public void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();
        Block block = e.getBlock();
        Location location = block.getLocation();

        BlockManager blockManager = plugin.getBlockManager();
        AbstractHeroBlock heroBlock = blockManager.getBlockAt(location);
        if (heroBlock == null) {
            return;
        }

        if (!plugin.getPermissionManager().hasPermission(player, Permission.REMOVE)) {
            Messaging.send(player, "This block is protected.");
            e.setCancelled(true);
            return;
        }

        blockManager.removeBlockAt(location);
        Messaging.send(player, "Destroyed HeroBlock.");
    }

}
