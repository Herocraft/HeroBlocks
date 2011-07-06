package com.herocraftonline.dthielke.heroblocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.config.ConfigurationNode;

import com.herocraftonline.dev.heroes.persistence.Hero;

public abstract class AbstractHeroBlock implements Rewardable, Locatable, Savable {

    protected final Block block;

    public AbstractHeroBlock(Block block) {
        this.block = block;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractHeroBlock other = (AbstractHeroBlock) obj;
        if (block == null) {
            if (other.block != null) {
                return false;
            }
        } else if (!block.getLocation().equals(other.block.getLocation())) {
            return false;
        }
        return true;
    }

    @Override
    public Location getLocation() {
        return block.getLocation();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (block == null ? 0 : block.getLocation().hashCode());
        return result;
    }

    @Override
    public abstract boolean isEligible(Hero hero);

    @Override
    public abstract boolean reward(Hero hero);

    @Override
    public abstract boolean save(ConfigurationNode config, String path);

}
