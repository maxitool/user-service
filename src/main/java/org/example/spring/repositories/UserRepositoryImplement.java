package org.example.spring.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.spring.entities.User;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImplement extends SimpleJpaRepository<User, Long> implements UserRepository {

    private final EntityManagerFactory entityManagerFactory;

    public UserRepositoryImplement(EntityManager entityManager) {
        super(User.class, entityManager);
        entityManagerFactory = entityManager.getEntityManagerFactory();
    }

    @Override
    public final Optional<User> findByEmail(String email) {
        return Optional.ofNullable(
                entityManagerFactory.callInTransaction(entityManager ->
                        entityManager.createQuery("FROM User user WHERE user.email = ?1", User.class)
                                .setParameter(1, email)
                                .getSingleResultOrNull()));
    }

    @Override
    public final List<User> findByName(String name) {
        return entityManagerFactory.callInTransaction(entityManager ->
                entityManager.createQuery("FROM User user WHERE user.name = ?1", User.class)
                        .setParameter(1, name)
                        .getResultList());
    }

    @Override
    public final List<User> findByAge(Integer age) {
        return entityManagerFactory.callInTransaction(entityManager ->
                entityManager.createQuery("FROM User user WHERE user.age = ?1", User.class)
                        .setParameter(1, age)
                        .getResultList());
    }
}
