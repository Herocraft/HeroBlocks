package com.herocraftonline.dthielke.expblocks;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.config.ConfigurationNode;

public class ExpBlock {

    private Block block;
    private int exp;
    private Set<String> users = new HashSet<String>();

    public ExpBlock(Block block, int exp) {
        this.block = block;
        this.exp = exp;
    }

    public ExpBlock(Block block, int exp, Set<String> users) {
        this.block = block;
        this.exp = exp;
        this.users = users;
    }
    
    public boolean isEligible(String player) {
        if (users.contains(player.toLowerCase())) {
            return false;
        } else {
            return true;
        }
    }
    
    public void registerUser(String player) {
        users.add(player.toLowerCase());
    }
    
    public void unregisterUser(String player) {
        users.remove(player.toLowerCase());
    }
    
    public void setUserRegistration(String player, boolean registered) {
        if (registered) {
            unregisterUser(player);
        } else {
            registerUser(player);
        }
    }

    public Block getBlock() {
        return block;
    }

    public int getExp() {
        return exp;
    }

    public Set<String> getUsers() {
        return new HashSet<String>(users);
    }
    
    public void save(ConfigurationNode config, String path) {
        Location loc = block.getLocation();
        String identifier = String.format("%s:%d,%d,%d", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        config.setProperty(path + "." + identifier + ".users", users);
        config.setProperty(path + "." + identifier + ".exp", exp);
    }
    
    public static ExpBlock load(ConfigurationNode config, String path) {
        String[] split = path.split("\\.");
        String identifier = split.length == 0 ? path : split[split.length];
        
        Pattern pattern = Pattern.compile("(\\w+):(\\d+),(\\d+),(\\d+)");
        Matcher matcher = pattern.matcher(identifier);
        
        if (matcher.groupCount() != 4) {
            return null;
        }
        
        String world = matcher.group(0);
        int x = Integer.valueOf(matcher.group(1));
        int y = Integer.valueOf(matcher.group(2));
        int z = Integer.valueOf(matcher.group(3));
        
        Block block = Bukkit.getServer().getWorld(world).getBlockAt(x, y, z);
        int exp = config.getInt(path + ".exp", 0);
        Set<String> users = new HashSet<String>(config.getStringList(path + ".users", null));
        
        return new ExpBlock(block, exp, users);
    }

}
