package com.karpen.lWhitelist.commands;

import com.karpen.lWhitelist.services.ListManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommands implements CommandExecutor {

    private ListManager manager;

    public ListCommands(ListManager manager){
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 || args.length == 1 ){
            sender.sendMessage(ChatColor.RED + "Используй /vlist <ban | add | unban>");

            return true;
        }

        return switch (args[0].toLowerCase()){
            case "add" -> addUser(args[1], sender);
            case "ban" -> banUser(args[1], sender);
            case "unban" -> unbanUser(args[1], sender);
            default -> def(sender);
        };
    }

    private boolean addUser(String name, CommandSender sender){
        manager.allowUser(name);

        sender.sendMessage(ChatColor.GREEN + "Игрок " + name + " добавлен");
        return true;
    }

    private boolean banUser(String name, CommandSender sender){
        manager.banUserByName(name);

        sender.sendMessage(ChatColor.GREEN + "Игрок " + name + " забанен");
        Bukkit.getPlayer(name).kickPlayer(ChatColor.RED + "Вы забанены, причина lastvibe.ru/bans");

        return true;
    }

    private boolean unbanUser(String name, CommandSender sender){
        manager.unbanUserByName(name);

        sender.sendMessage(ChatColor.GREEN + "Игрок " + name + " разбранен");

        return true;
    }

    private boolean def(CommandSender sender){
        sender.sendMessage(ChatColor.RED + "Используй /vlist <ban | add | unban>");
        return true;
    }
}
