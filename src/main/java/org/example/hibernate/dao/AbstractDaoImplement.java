package org.example.hibernate.dao;

import org.example.hibernate.config.HibernateUtil;
import org.example.hibernate.validator.ValidatorUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

abstract public class AbstractDaoImplement<T, ID> implements Dao<T, ID> {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractDaoImplement.class);
    protected final Class<T> entityClass;

    protected AbstractDaoImplement(Class<T> entityClass) {
        if (entityClass == null) {
            logger.error("Error: entityClass passed in constructor is null");
        }
        this.entityClass = entityClass;
    }

    @Override
    public final boolean save(T entity) {
        return executeInTransaction(session -> session.persist(entity));
    }

    @Override
    public final boolean update(T entity) {
        return executeInTransaction(session -> session.merge(entity));
    }

    @Override
    public final T findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(entityClass, id);
        } catch (Exception e) {
            logger.error("Error finding user by id {}: {}", id, e.getMessage());
        }
        return null;
    }

    @Override
    public final List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> query = session.createQuery("FROM User", entityClass);
            return query.list();
        } catch (Exception e) {
            logger.error("Error finding all users: {}", e.getMessage());
        }
        return List.of();
    }

    @Override
    public final boolean delete(T entity) {
        return executeInTransaction(session -> session.remove(entity));
    }

    @Override
    public final boolean deleteById(ID id) {
        T entity = findById(id);
        return entity != null && delete(entity);
    }

    protected final boolean executeInTransaction(Consumer<Session> action) {
        logger.info("Transaction: {}", action);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            action.accept(session);
            transaction.commit();
            logger.info("Transaction was successfully completed: {}", action);
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error transaction: {}", e.getMessage());
        }
        return false;
    }
}
