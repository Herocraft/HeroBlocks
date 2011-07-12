package com.herocraftonline.dthielke.heroblocks.command.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import com.herocraftonline.dthielke.heroblocks.BlockManager;
import com.herocraftonline.dthielke.heroblocks.ExpBlock;
import com.herocraftonline.dthielke.heroblocks.HeroBlocks;
import com.herocraftonline.dthielke.heroblocks.PermissionManager.Permission;
import com.herocraftonline.dthielke.heroblocks.command.BaseCommand;
import com.herocraftonline.dthielke.heroblocks.util.Messaging;

public class CreateExpBlockCommand extends BaseCommand {

    private CmdPlayerListener playerListener = new CmdPlayerListener();
    private Map<Player, Double> creators = new HashMap<Player, Double>();

    public CreateExpBlockCommand(HeroBlocks plugin) {
        super(plugin);
        setName("Create Exp Block");
        setDescription("Creates a block that gives exp when hit");
        setUsage("§e/hb create exp §9<reward>");
        setMinArgs(1);
        setMaxArgs(1);
        getIdentifiers().add("hb create exp");
        
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Normal, plugin);
        pluginManager.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Normal, plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;
        if (!plugin.getPermissionManager().hasPermission(player, Permission.CREATE)) {
            Messaging.send(player, "Insufficient permission.");
            return;
        }

        double exp;
        try {
            exp = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            Messaging.send(player, "Invalid exp.");
            return;
        }

        if (exp <= 0) {
            Messaging.send(player, "Exp must be greater than 0.");
            return;
        }

        Messaging.send(player, "Left click a block to register it.");
        creators.put(player, exp);
    }

    public class CmdPlayerListener extends PlayerListener {

        @Override
        public void onPlayerInteract(PlayerInteractEvent e) {
            if (e.isCancelled()) {
                return;
            }
            
            if (!e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                return;
            }

            Player player = e.getPlayer();
            if (!creators.containsKey(player)) {
                return;
            }

            double exp = creators.get(player);

            BlockManager blockManager = plugin.getBlockManager();
            Block block = e.getClickedBlock();
            Location location = block.getLocation();
            if (blockManager.getBlockAt(location) != null) {
                creators.remove(player);
                Messaging.send(player, "A HeroBlock already exists at this location.");
                return;
            }

            ExpBlock expBlock = new ExpBlock(block, exp);
            blockManager.addBlock(expBlock);
            creators.remove(player);

            Messaging.send(player, "Created Exp Block worth " + exp + " exp.");
        }

        @Override
        public void onPlayerQuit(PlayerQuitEvent e) {
            creators.remove(e.getPlayer());
        }

    }

}
