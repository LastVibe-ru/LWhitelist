package com.karpen.lWhitelist.services;

import com.karpen.lWhitelist.models.Config;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private Config config;
    private JavaPlugin plugin;

    public ConfigManager(Config config, JavaPlugin plugin){
        this.config = config;
        this.plugin = plugin;
    }

    public void loadConfig(){
        plugin.saveDefaultConfig();

        Configuration configuration = plugin.getConfig();

        config.setDbUrl(configuration.getString("db-url"));
        config.setDbUser(configuration.getString("db-user"));
        config.setDbPassword(configuration.getString("db-password"));
    }
}
