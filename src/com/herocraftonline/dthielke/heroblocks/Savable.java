package com.herocraftonline.dthielke.heroblocks;

import org.bukkit.util.config.ConfigurationNode;

public interface Savable {

    public boolean save(ConfigurationNode config, String path);
    
}
