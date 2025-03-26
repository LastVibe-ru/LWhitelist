package com.karpen.lWhitelist.managers;

import com.karpen.lWhitelist.models.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListManager {

    private List<User> users = new ArrayList<>();
    private User user = new User();

    private DBManager manager;

    public ListManager(DBManager manager){
        this.manager = manager;
    }

    public User getUser(String name){
        users = manager.loadUsers();

        for (User user : users){
            if (user.getName().equalsIgnoreCase(name)){
                return user;
            }
        }

        return null;
    }

    public void banUserByName(String name, String reason){
        users = manager.loadUsers();

        user = getUser(name);

        users.remove(user);

        manager.saveUsers(users);

        user.setBaned(true);
        user.setReason(reason);
        user.setAccess(false);

        users.add(user);

        manager.saveUsers(users);
    }

    public void unbanUserByName(String name){
        users = manager.loadUsers();

        user = getUser(name);

        users.remove(user);

        manager.saveUsers(users);
        users = manager.loadUsers();

        user.setReason(null);
        user.setBaned(false);
        user.setAccess(true);
        users.add(user);

        manager.saveUsers(users);
    }

    public void allowUser(String name){
        users = manager.loadUsers();

        if (getUser(name) != null){
            return;
        }

        user = new User();
        user.setName(name);
        user.setReason(null);
        user.setAccess(true);
        user.setBaned(false);

        users.add(user);

        manager.addUser(user);
    }
}
