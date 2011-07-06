package com.herocraftonline.dthielke.heroblocks;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class HeroBlocks extends JavaPlugin {

    private static final Logger logger = Logger.getLogger("Minecraft");
    
    private BlockManager blockManager;

    public static void log(Level level, String msg) {
        logger.log(level, "[HeroBlocks] " + msg);
    }

    @Override
    public void onDisable() {
        blockManager.saveBlocks(getConfiguration());
        
        log(Level.INFO, "version " + getDescription().getVersion() + " disabled.");
    }

    @Override
    public void onEnable() {
        blockManager = new BlockManager();
        blockManager.loadBlocks(getConfiguration());
        
        log(Level.INFO, "version " + getDescription().getVersion() + " enabled.");
    }

}
