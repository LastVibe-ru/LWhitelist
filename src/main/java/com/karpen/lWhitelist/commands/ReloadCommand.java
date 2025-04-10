package com.karpen.lWhitelist.commands;

import com.karpen.lWhitelist.managers.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private ConfigManager manager;

    public ReloadCommand(ConfigManager manager){
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.loadConfig();

        return true;
    }
}
