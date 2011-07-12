/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.herocraftonline.dthielke.heroblocks.command.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.herocraftonline.dthielke.heroblocks.HeroBlocks;
import com.herocraftonline.dthielke.heroblocks.command.BaseCommand;

public class HelpCommand extends BaseCommand {
    private static final int CMDS_PER_PAGE = 8;

    public HelpCommand(HeroBlocks plugin) {
        super(plugin);
        setName("Help");
        setDescription("Displays the help menu");
        setUsage("§e/hb help §8[page#]");
        setMinArgs(0);
        setMaxArgs(1);
        getIdentifiers().add("hb help");
        getIdentifiers().add("hb");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        int page = 0;
        if (args.length != 0) {
            try {
                page = Integer.parseInt(args[0]) - 1;
            } catch (NumberFormatException e) {}
        }

        List<BaseCommand> commands = plugin.getCommandManager().getCommands();

        int numPages = commands.size() / CMDS_PER_PAGE;
        if (commands.size() % CMDS_PER_PAGE != 0) {
            numPages++;
        }

        if (page >= numPages || page < 0) {
            page = 0;
        }
        sender.sendMessage("§c-----[ " + "§fHeroBlocks Help <" + (page + 1) + "/" + numPages + ">§c ]-----");
        int start = page * CMDS_PER_PAGE;
        int end = start + CMDS_PER_PAGE;
        if (end > commands.size()) {
            end = commands.size();
        }
        for (int c = start; c < end; c++) {
            BaseCommand cmd = commands.get(c);
            sender.sendMessage("  §a" + cmd.getUsage());
        }

        sender.sendMessage("§cFor more info on a particular command, type §f/<command> ?");
    }

}
