package com.herocraftonline.dthielke.heroblocks;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.persistence.HeroManager;
import com.herocraftonline.dthielke.heroblocks.command.CommandManager;
import com.herocraftonline.dthielke.heroblocks.command.commands.CreateExpBlockCommand;
import com.herocraftonline.dthielke.heroblocks.command.commands.HelpCommand;
import com.herocraftonline.dthielke.heroblocks.command.commands.InspectCommand;
import com.nijikokun.bukkit.Permissions.Permissions;

public class HeroBlocks extends JavaPlugin {

    private static final Logger logger = Logger.getLogger("Minecraft");

    private HBPlayerListener playerListener;
    private HBBlockListener blockListener;
    private BlockManager blockManager;
    private HeroManager heroManager;
    private PermissionManager permissionManager;
    private CommandManager commandManager;

    public static void log(Level level, String msg) {
        logger.log(level, "[HeroBlocks] " + msg);
    }

    @Override
    public void onDisable() {
        blockManager.saveBlocks(new Configuration(new File(getDataFolder(), "config.yml")));

        log(Level.INFO, "version " + getDescription().getVersion() + " disabled.");
    }

    @Override
    public void onEnable() {
        registerEvents();
        setupBlockManager();
        setupHeroManager();
        setupPermissions();
        setupCommands();

        log(Level.INFO, "version " + getDescription().getVersion() + " enabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return commandManager.dispatch(sender, command, label, args);
    }

    private void setupCommands() {
        commandManager = new CommandManager();
        commandManager.addCommand(new InspectCommand(this));
        commandManager.addCommand(new CreateExpBlockCommand(this));
        commandManager.addCommand(new HelpCommand(this));
    }

    private void registerEvents() {
        playerListener = new HBPlayerListener(this);
        blockListener = new HBBlockListener(this);
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Monitor, this);
        pluginManager.registerEvent(Type.BLOCK_BREAK, blockListener, Priority.Highest, this);
    }

    private void setupPermissions() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("Permissions");
        if (plugin == null) {
            permissionManager = new PermissionManager();
        } else {
            if (plugin.isEnabled()) {
                Permissions permissions = (Permissions) plugin;
                permissionManager = new PermissionManager(permissions.getHandler());
            }
        }
    }

    private void setupBlockManager() {
        blockManager = new BlockManager();
        blockManager.loadBlocks(getConfiguration());
    }

    private void setupHeroManager() {
        Plugin plugin = getServer().getPluginManager().getPlugin("Heroes");
        if (plugin == null) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Heroes heroes = (Heroes) plugin;
        heroManager = heroes.getHeroManager();
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public HeroManager getHeroManager() {
        return heroManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }
    
    public CommandManager getCommandManager() {
        return commandManager;
    }

}
