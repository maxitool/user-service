package org.example;

import org.example.hibernate.config.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;

import java.util.Properties;

public abstract class AbstractIntegrationTest {

    @BeforeAll
    static void initDatabase() {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.jboss.logging.provider", "slf4j");
        Properties settings = getProperties();

        Configuration configuration = new Configuration();
        configuration.setProperties(settings);
        configuration.addAnnotatedClass(org.example.hibernate.entities.User.class);

        SessionFactory testSessionFactory = configuration.buildSessionFactory();
        HibernateUtil.setSessionFactory(testSessionFactory);
    }

    private static @NotNull Properties getProperties() {
        Properties settings = new Properties();
        settings.put("jakarta.persistence.jdbc.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
        settings.put("jakarta.persistence.jdbc.user", "sa");
        settings.put("jakarta.persistence.jdbc.password", "");
        settings.put("jakarta.persistence.jdbc.driver", "org.h2.Driver");

        settings.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        settings.put("hibernate.hbm2ddl.auto", "create-drop");
        settings.put("hibernate.show_sql", "true");
        settings.put("hibernate.format_sql", "true");
        return settings;
    }
}