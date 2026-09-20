package org.example.spring.repositories;

import org.example.spring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByName(String name);

    List<User> findByAge(Integer age);
    boolean existsByEmail(String email);
}
