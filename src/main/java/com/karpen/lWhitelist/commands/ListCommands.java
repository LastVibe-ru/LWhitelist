package com.karpen.lWhitelist.commands;

import com.karpen.lWhitelist.managers.ListManager;
import com.karpen.lWhitelist.managers.WebManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

public class ListCommands implements CommandExecutor {

    private ListManager manager;
    private WebManager web;

    public ListCommands(ListManager manager, WebManager web){
        this.manager = manager;
        this.web = web;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 || args.length == 1 ){
            sender.sendMessage(ChatColor.RED + "Используй /vlist <ban 'ник' (причина) | add 'ник' | unban 'ник'>");

            return true;
        }

        if (args[0].equalsIgnoreCase("ban") && args.length < 3){
            sender.sendMessage(ChatColor.RED + "Используй /vlist <ban 'ник' (причина) | add 'ник' | unban 'ник'>");
            return true;
        }

        if (args[0].equalsIgnoreCase("ban")) {
            String name = args[1];
            String reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
            reason = reason.replaceAll("^\"(.*)\"$", "$1");

            return banUser(name, reason, sender);
        }

        return switch (args[0].toLowerCase()){
            case "add" -> addUser(args[1], sender);
            case "ban" -> banUser(args[1], args[2], sender);
            case "unban" -> unbanUser(args[1], sender);
            default -> def(sender);
        };
    }

    private boolean addUser(String name, CommandSender sender){
        manager.allowUser(name);

        sender.sendMessage(ChatColor.GREEN + "Игрок " + name + " добавлен");
        return true;
    }

    private boolean banUser(String name, String reason, CommandSender sender){
        manager.banUserByName(name, reason);

        sender.sendMessage(ChatColor.GREEN + "Игрок " + name + " забанен");

        Player player = Bukkit.getPlayer(name);
        if (player != null && player.isOnline()) {
            player.kickPlayer(ChatColor.RED + "Вы забанены, " + reason);
        }

        if (manager.getUser(name) == null){
            sender.sendMessage(ChatColor.RED + "Игрок не найден в бд");

            return true;
        }

        web.banUser(name, reason);

        return true;
    }

    private boolean unbanUser(String name, CommandSender sender){
        manager.unbanUserByName(name);

        sender.sendMessage(ChatColor.GREEN + "Игрок " + name + " разбранен");

        return true;
    }

    private boolean def(CommandSender sender){
        sender.sendMessage(ChatColor.RED + "Используй /vlist <ban 'ник' (причина) | add 'ник' | unban 'ник'>");

        return true;
    }
}
