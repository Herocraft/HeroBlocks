package com.herocraftonline.dthielke.expblocks;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class HeroBlocks extends JavaPlugin {

    private static final Logger logger = Logger.getLogger("Minecraft");

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        log(Level.INFO, "version " + getDescription().getVersion() + " enabled.");
    }

    public void log(Level level, String msg) {
        logger.log(level, "[HeroBlocks] " + msg);
    }

}
