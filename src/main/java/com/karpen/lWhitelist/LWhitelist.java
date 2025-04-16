package com.karpen.lWhitelist;

import com.karpen.lWhitelist.commands.ListCommands;
import com.karpen.lWhitelist.commands.ListSuggests;
import com.karpen.lWhitelist.commands.ReloadCommand;
import com.karpen.lWhitelist.listeners.MainListeners;
import com.karpen.lWhitelist.managers.*;
import com.karpen.lWhitelist.models.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LWhitelist extends JavaPlugin {

    private ConfigManager configManager;
    private Config config;
    private DBManager dbManager;
    private ListManager listManager;
    private ListCommands commands;
    private ReloadCommand reloadCommand;
    private MainListeners listeners;
    private ListSuggests suggests;
    private RequestManager requestManager;
    private WebServerManager webServerManager;

    @Override
    public void onEnable() {
        config = new Config();
        configManager = new ConfigManager(config, this);

        configManager.loadConfig();

        dbManager = new DBManager(config);
        listManager = new ListManager(dbManager);
        requestManager = new RequestManager(this, config);
        commands = new ListCommands(listManager, requestManager);
        reloadCommand = new ReloadCommand(configManager);
        listeners = new MainListeners(listManager);
        suggests = new ListSuggests();
        webServerManager = new WebServerManager(this, config, listManager);

        getCommand("vlist").setExecutor(commands);
        getCommand("vlist").setTabCompleter(suggests);

        getCommand("vlist-reload").setExecutor(reloadCommand);

        Bukkit.getPluginManager().registerEvents(listeners, this);

        dbManager.loadUsers();

        webServerManager.startServer();

        getLogger().info("Loaded");
    }

    @Override
    public void onDisable() {
        webServerManager.stopServer();

        dbManager.close();

        getLogger().info("Stopped");
    }
}
