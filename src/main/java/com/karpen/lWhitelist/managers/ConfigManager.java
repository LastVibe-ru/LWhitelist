package com.karpen.lWhitelist.managers;

import com.karpen.lWhitelist.models.Config;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

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

        config.setBanUrl(configuration.getString("api.ban.url"));
        config.setUnbanUrl(configuration.getString("api.unban.url"));
        config.setApiKey(configuration.getString("api.key"));

        config.setDebug(configuration.getBoolean("debug"));
    }
}
