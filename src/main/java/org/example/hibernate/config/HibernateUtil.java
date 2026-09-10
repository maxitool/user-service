package org.example.hibernate.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            createCessionFactory();
        }
        return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        HibernateUtil.sessionFactory = sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    private static void createCessionFactory() {
        try {
            Configuration conf = (new Configuration()).configure();
            conf.setProperty("hibernate.connection.url", System.getenv("DATABASE_URL"));
            conf.setProperty("hibernate.connection.username", System.getenv("DATABASE_USER"));
            conf.setProperty("hibernate.connection.password", System.getenv("DATABASE_PASSWORD"));
            conf.setProperty("default_schema", System.getenv("DATABASE_SCHEMA"));
            sessionFactory = conf.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }
}