package org.example.hibernate.dao;

import org.example.hibernate.entities.User;

import java.util.List;

public interface UserDao extends Dao<User, Long> {
    User findByEmail(String email);

    List<User> findByName(String name);

    List<User> findByAge(Integer age);
}
