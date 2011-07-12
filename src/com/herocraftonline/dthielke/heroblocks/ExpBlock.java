package com.herocraftonline.dthielke.heroblocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.config.ConfigurationNode;

import com.herocraftonline.dev.heroes.classes.HeroClass.ExperienceType;
import com.herocraftonline.dev.heroes.persistence.Hero;

public class ExpBlock extends AbstractHeroBlock implements Rewardable, Locatable, Savable {

    private double exp;
    private Set<String> users;

    public ExpBlock(Block block, double exp) {
        super(block);
        this.exp = exp;
        this.users = new HashSet<String>();
    }

    @Override
    public String toString() {
        return "Exp Block (" + exp + " exp) used by " + users;
    }

    @Override
    public boolean isEligible(Hero hero) {
        String name = hero.getPlayer().getName().toLowerCase();
        return !users.contains(name);
    }

    @Override
    public boolean reward(Hero hero) {
        if (!isEligible(hero)) {
            return false;
        }

        hero.gainExp(exp, ExperienceType.EXTERNAL);
        setEligible(hero, false);

        return true;
    }

    @Override
    public boolean save(ConfigurationNode config, String path) {
        try {
            Location loc = getLocation();
            String identifier = String.format("%s|%d,%d,%d", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            config.setProperty(path + "." + identifier + ".users", new ArrayList<String>(users));
            config.setProperty(path + "." + identifier + ".exp", exp);
        } catch (Exception e) {
            HeroBlocks.log(Level.SEVERE, "Block save failed!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void setEligible(Hero hero, boolean eligible) {
        String name = hero.getPlayer().getName().toLowerCase();

        if (!eligible) {
            users.add(name);
        } else {
            users.remove(name);
        }
    }

    public static ExpBlock load(ConfigurationNode config, String path) {
        String[] split = path.split("\\.");
        String identifier = split.length == 0 ? path : split[split.length - 1];

        Pattern pattern = Pattern.compile("(\\w+)\\|([-]?\\d+),([-]?\\d+),([-]?\\d+)");
        Matcher matcher = pattern.matcher(identifier);

        if (!matcher.matches()) {
            return null;
        }

        if (matcher.groupCount() != 4) {
            return null;
        }

        String world = matcher.group(1);
        int x = Integer.valueOf(matcher.group(2));
        int y = Integer.valueOf(matcher.group(3));
        int z = Integer.valueOf(matcher.group(4));

        Block block = Bukkit.getServer().getWorld(world).getBlockAt(x, y, z);
        double exp = config.getDouble(path + ".exp", 0);
        Set<String> users = new HashSet<String>(config.getStringList(path + ".users", null));

        ExpBlock expBlock = new ExpBlock(block, exp);
        expBlock.users = users;
        return expBlock;
    }

}
