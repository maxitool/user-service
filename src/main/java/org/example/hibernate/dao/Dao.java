package org.example.hibernate.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T, ID> {
    boolean save(T entity);
    boolean update(T entity);
    T findById(ID id);
    List<T> findAll();
    boolean delete(T entity);
    boolean deleteById(ID id);
}