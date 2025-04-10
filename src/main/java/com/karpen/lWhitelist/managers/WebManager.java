package com.karpen.lWhitelist.managers;

import com.karpen.lWhitelist.models.Config;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WebManager {

    private JavaPlugin plugin;
    private Config config;

    public WebManager(JavaPlugin plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void unbanUser(String name) {
        try {
            name = name.trim();

            String url = config.getUnbanUrl();
            String api_key = config.getApiKey();

            String reqUrl;

            reqUrl = url + name + "&key=" + api_key;

            if (config.isDebug()){
                plugin.getLogger().info("Req: " + reqUrl);
            }

            HttpURLConnection connection = null;

            try {
                URL urlObj = new URL(reqUrl);
                connection = (HttpURLConnection) urlObj.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    if (config.isDebug()){
                        plugin.getLogger().info("Error data fetch: HTTP " + responseCode);
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    if (config.isDebug()){
                        plugin.getLogger().info("Error response: " + content.toString());
                    }
                }
            } catch (IOException e) {
                plugin.getLogger().info("Error while making the request: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (Exception e) {
            plugin.getLogger().info("Error encoding parameters: " + e.getMessage());
        }
    }


    public void banUser(String name, String reason) {
        try {
            name = name.trim();
            reason = reason.trim();

            String url = config.getBanUrl();
            String api_key = config.getApiKey();

            String encodedReason = URLEncoder.encode(reason, StandardCharsets.UTF_8);

            String reqUrl;

            reqUrl = url + name + "&reason=" + encodedReason + "&key=" + api_key;

            if (config.isDebug()){
                plugin.getLogger().info("Req: " + reqUrl);
            }

            HttpURLConnection connection = null;

            try {
                URL urlObj = new URL(reqUrl);
                connection = (HttpURLConnection) urlObj.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    if (config.isDebug()){
                        plugin.getLogger().info("Error data fetch: HTTP " + responseCode);
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    if (config.isDebug()){
                        plugin.getLogger().info("Error response: " + content.toString());
                    }
                }
            } catch (IOException e) {
                plugin.getLogger().info("Error while making the request: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (Exception e) {
            plugin.getLogger().info("Error encoding parameters: " + e.getMessage());
        }
    }
}
