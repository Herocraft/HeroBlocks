package com.herocraftonline.dthielke.heroblocks;

import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import com.herocraftonline.dev.heroes.persistence.Hero;
import com.herocraftonline.dthielke.heroblocks.util.Messaging;

public class HBPlayerListener extends PlayerListener {

    private final HeroBlocks plugin;

    public HBPlayerListener(HeroBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.isCancelled()) {
            return;
        }
        
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Player player = e.getPlayer();
        Hero hero = plugin.getHeroManager().getHero(player);
        if (hero == null) {
            return;
        }

        Block block = e.getClickedBlock();
        Location location = block.getLocation();
        AbstractHeroBlock heroBlock = plugin.getBlockManager().getBlockAt(location);
        if (heroBlock == null) {
            return;
        }

        if (!heroBlock.isEligible(hero)) {
            Messaging.send(player, "You are not eligible for this reward.");
            return;
        }

        heroBlock.reward(hero);
        HeroBlocks.log(Level.INFO, String.format("%s has activated a reward block at %s in %s.", player.getName(), location.toVector(), location.getWorld().getName()));
        Messaging.send(player, "Congratulations, you found a special block!");
    }

}
