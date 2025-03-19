package com.karpen.lWhitelist;

import com.karpen.lWhitelist.commands.ListCommands;
import com.karpen.lWhitelist.commands.ListSuggests;
import com.karpen.lWhitelist.listeners.MainListeners;
import com.karpen.lWhitelist.models.Config;
import com.karpen.lWhitelist.services.ConfigManager;
import com.karpen.lWhitelist.services.DBManager;
import com.karpen.lWhitelist.services.ListManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LWhitelist extends JavaPlugin {

    private ConfigManager configManager;
    private Config config;
    private DBManager dbManager;
    private ListManager listManager;
    private ListCommands commands;
    private MainListeners listeners;
    private ListSuggests suggests;

    @Override
    public void onEnable() {
        config = new Config();
        configManager = new ConfigManager(config, this);

        configManager.loadConfig();

        dbManager = new DBManager(config, this);
        listManager = new ListManager(dbManager);
        commands = new ListCommands(listManager);
        listeners = new MainListeners(listManager);
        suggests = new ListSuggests();

        getCommand("vlist").setExecutor(commands);
        getCommand("vlist").setTabCompleter(suggests);

        Bukkit.getPluginManager().registerEvents(listeners, this);

        dbManager.loadUsers();

        getLogger().info("Loaded");
    }

    @Override
    public void onDisable() {
        dbManager.close();

        getLogger().info("Stopped");
    }
}
