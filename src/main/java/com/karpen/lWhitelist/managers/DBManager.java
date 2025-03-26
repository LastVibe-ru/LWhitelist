package com.karpen.lWhitelist.managers;

import com.karpen.lWhitelist.models.Config;
import com.karpen.lWhitelist.models.User;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    @Setter
    private Connection connection;

    private Config config;
    private JavaPlugin plugin;

    public DBManager(Config config, JavaPlugin plugin){
        this.config = config;
        this.plugin = plugin;

        final String url = config.getDbUrl();
        final String user = config.getDbUser();
        final String password = config.getDbPassword();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);
            createTableIfNotExists();
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPassword());
    }

    private void createTableIfNotExists(){
        String createTableSQL = "CREATE TABLE IF NOT EXISTS players ("+
                "playerName VARCHAR(100) PRIMARY KEY, " +
                "access BIT(1) NOT NULL, " +
                "reason VARCHAR(100), " +
                "banned BIT(1) NOT NULL)";

        try(Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private int convertBoolToBit(boolean bool){
        if (bool){
            return 1;
        } else {
            return 0;
        }
    }

    private boolean convertIntToBool(int bit){
        if (bit == 1){
            return true;
        } else {
            return false;
        }
    }

    public void addUser(User user){
        String insertSQL = "INSERT INTO players (playerName, access, banned, reason) VALUES (?, ?, ?, ?)";

        try(PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            stmt.setString(1, user.getName());
            stmt.setInt(2, convertBoolToBit(user.isAccess()));
            stmt.setInt(3, convertBoolToBit(user.isBaned()));
            stmt.setString(4, user.getReason());

            stmt.addBatch();
            stmt.executeBatch();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void saveUsers(List<User> users) {
        String updateSQL = "UPDATE players SET access = ?, banned = ?, reason = ? WHERE playerName = ?";

        try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
            for (User  user : users) {
                stmt.setInt(1, convertBoolToBit(user.isAccess()));
                stmt.setInt(2, convertBoolToBit(user.isBaned()));
                stmt.setString(3, user.getReason());
                stmt.setString(4, user.getName());

                stmt.addBatch();
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> loadUsers(){
        List<User> users = new ArrayList<>();

        String query = "SELECT playerName, access, banned, reason FROM players";

        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                String playerName = resultSet.getString("playerName");
                boolean access = convertIntToBool(resultSet.getInt("access"));
                boolean banned = convertIntToBool(resultSet.getInt("banned"));
                String reason = resultSet.getString("reason");

                User user = new User();
                user.setName(playerName);
                user.setAccess(access);
                user.setBaned(banned);
                user.setReason(reason);

                users.add(user);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return users;
    }

    public void close(){
        try {
            if (connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
