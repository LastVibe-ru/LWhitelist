package com.karpen.lWhitelist.managers;

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

    public WebManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private String url = "http://127.0.0.1:8091/ban?name=";

    public void banUser(String name, String reason) {
        try {
            name = name.trim();
            reason = reason.trim();

            String encodedReason = URLEncoder.encode(reason, StandardCharsets.UTF_8);
            String reqUrl = url + name + "&reason=" + encodedReason;

            plugin.getLogger().info("Name: " + name + " Reason: " + reason);
            plugin.getLogger().info(reqUrl);

            HttpURLConnection connection = null;

            try {
                URL urlObj = new URL(reqUrl);
                connection = (HttpURLConnection) urlObj.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    plugin.getLogger().info("Error data fetch: HTTP " + responseCode);
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    plugin.getLogger().info("Error response: " + content.toString());
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
