package com.herocraftonline.dthielke.heroblocks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.util.config.Configuration;

public class BlockManager {

    private Set<AbstractHeroBlock> blocks;

    public BlockManager() {
        blocks = new HashSet<AbstractHeroBlock>();
    }

    public void addBlock(AbstractHeroBlock block) {
        blocks.add(block);
    }

    public void removeBlock(Location location) {
        for (AbstractHeroBlock block : blocks) {
            if (location.equals(block.getLocation())) {
                blocks.remove(block);
                break;
            }
        }
    }

    public AbstractHeroBlock getBlock(Location location) {
        for (AbstractHeroBlock block : blocks) {
            if (location.equals(block.getLocation())) {
                return block;
            }
        }
        return null;
    }

    public void loadBlocks(Configuration config) {
        config.load();
        int count = 0;
        List<String> expBlockKeys = config.getKeys("ExpBlock");
        if (expBlockKeys != null) {
            HeroBlocks.log(Level.INFO, "Loading ExpBlocks...");
            for (String identifier : expBlockKeys) {
                HeroBlocks.log(Level.INFO, "    --> " + identifier);
                AbstractHeroBlock block = ExpBlock.load(config, "ExpBlock." + identifier);
                if (block != null) {
                    blocks.add(block);
                    count++;
                }
            }
            HeroBlocks.log(Level.INFO, "Loaded " + count + " ExpBlocks.");
        }
    }

    public void saveBlocks(Configuration config) {
        int count = 0;
        for (AbstractHeroBlock block : blocks) {
            block.save(config, block.getClass().getSimpleName());
            count++;
        }
        config.save();
        HeroBlocks.log(Level.INFO, "Saved " + count + " blocks");
    }

}
