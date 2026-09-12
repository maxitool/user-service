package org.example.hibernate.services;

import org.example.hibernate.dao.UserDao;
import org.example.hibernate.entities.User;

import java.util.List;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User save(User user) {
        return userDao.save(user);
    }

    public boolean update(User user) {
        return userDao.update(user);
    }

    public User findById(long id) {
        return userDao.findById(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public boolean delete(User user) {
        return userDao.delete(user);
    }

    public boolean deleteById(long id) {
        return userDao.deleteById(id);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public List<User> findByName(String name) {
        return userDao.findByName(name);
    }

    public List<User> findByAge(Integer age) {
        return userDao.findByAge(age);
    }
}
