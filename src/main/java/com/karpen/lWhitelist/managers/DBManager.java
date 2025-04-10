package com.karpen.lWhitelist.managers;

import com.karpen.lWhitelist.models.Config;
import com.karpen.lWhitelist.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;
import java.util.function.Function;

public class DBManager {

    private final SessionFactory sessionFactory;

    public DBManager(Config config){
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", config.getDbUrl());
        configuration.setProperty("hibernate.connection.username", config.getDbUser());
        configuration.setProperty("hibernate.connection.password", config.getDbPassword());
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.addAnnotatedClass(User.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    private <T> T executeInTransaction(Function<Session, T> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                T result = function.apply(session);
                transaction.commit();
                return result;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public void addUser(User user){
        executeInTransaction(session -> {
            session.persist(user);

            return null;
        });
    }

    public void saveUsers(List<User> users){
        executeInTransaction(session -> {
            for (User user : users){
                session.merge(user);
            }

            return null;
        });
    }

    public List<User> loadUsers() {
        return executeInTransaction(session ->
                session.createQuery("FROM User", User.class).list()
        );
    }

    public void close(){
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            if (!sessionFactory.isClosed()){
                sessionFactory.close();
            }
        }
    }
}
