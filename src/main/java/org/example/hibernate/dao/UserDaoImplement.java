package org.example.hibernate.dao;

import org.example.hibernate.config.HibernateUtil;
import org.example.hibernate.entities.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoImplement extends AbstractDaoImplement<User, Long> implements UserDao {

    public UserDaoImplement() {
        super(User.class);
    }

    @Override
    public User findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(entityClass, email);
        } catch (Exception e) {
            logger.error("Error finding user by email {}: {}", email, e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User user WHERE user.name = ?1", entityClass);
            query.setParameter(1, name);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding users by name: {}", e.getMessage());
        }
        return List.of();
    }

    @Override
    public List<User> findByAge(Integer age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User user WHERE user.age = ?1", entityClass);
            query.setParameter(1, age);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding users by age: {}", e.getMessage());
        }
        return List.of();
    }
}
