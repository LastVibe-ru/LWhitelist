package com.karpen.lWhitelist.managers;

import com.karpen.lWhitelist.models.Config;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class WebServerManager implements HttpHandler {
    private HttpServer server;
    private JavaPlugin plugin;

    private ListManager manager;
    private Config config;

    public WebServerManager(JavaPlugin plugin, Config config, ListManager manager){
        this.plugin = plugin;
        this.config = config;
        this.manager = manager;
    }

    public void startServer(){
        try {
            server = HttpServer.create(new InetSocketAddress(9010), 0);
            server.createContext("/api/allow", this);
            server.setExecutor(null);
            server.start();

            plugin.getLogger().info("Web server started at 8010 port");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void stopServer(){
        if (server != null){
            server.stop(0);
            plugin.getLogger().info("Shutdown web server");
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());

        String res;
        int code;

        if (!params.containsKey("name") || !params.containsKey("key")) {
            res = "Bad gateway";
            code = 400;
        } else {
            String name = params.get("name");
            String key = params.get("key");

            if (!key.equals(config.getApiKey())){
                res = "Authorization failed";
                code = 403;
            } else {
                res = "Success";
                code = 200;

                manager.allowUser(name);

                plugin.getLogger().info("User " + name + " added to list");

            }
        }

        exchange.getResponseHeaders().set("Content-Type", "application/text");
        exchange.sendResponseHeaders(code, res.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(res.getBytes());
        os.close();
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null) return result;

        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
