package user.dao.test;

import org.example.hibernate.config.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import java.util.Properties;

public class UserDaoImplementPostgresTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    static void initDatabase() {
        Properties settings = new Properties();
        settings.put("jakarta.persistence.jdbc.url", postgres.getJdbcUrl());
        settings.put("jakarta.persistence.jdbc.user", postgres.getUsername());
        settings.put("jakarta.persistence.jdbc.password", postgres.getPassword());
        settings.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");

        settings.put("hibernate.hbm2ddl.auto", "create-drop");
        settings.put("hibernate.show_sql", "true");

        Configuration configuration = new Configuration();
        configuration.setProperties(settings);
        configuration.addAnnotatedClass(org.example.hibernate.entities.User.class);

        SessionFactory testSessionFactory = configuration.buildSessionFactory();

        HibernateUtil.setSessionFactory(testSessionFactory);
    }
}
