package com.karpen.lWhitelist.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ListSuggests implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("vlist")){
            if (args.length == 1){
                suggestions.add("add");
                suggestions.add("ban");
                suggestions.add("unban");
            } else if (args.length == 2){
                for (var player : Bukkit.getOnlinePlayers()){
                    suggestions.add(player.getName());
                }
            }
        }

        return suggestions;
    }
}
