package org.example.hibernate.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotNull(message = "Name can't be null")
    @Size(min = 1, max = 100, message = "Length of name must be between 1 and 100")
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    @NotNull(message = "Email can't be null")
    @Size(min = 1, max = 100, message = "Length of email must be between 1 and 100")
    @Email(message = "Incorrect email format")
    private String email;

    @Column(nullable = false)
    @NotNull(message = "Age can't be null")
    @Positive(message = "Age must be greater than 0")
    private Integer age;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull(message = "CreatedAt can't be null")
    private LocalDateTime createdAt;

    public User() {
        this.createdAt = LocalDateTime.now();
        this.createdAt = this.createdAt.truncatedTo(ChronoUnit.MICROS);
    }

    public User(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = LocalDateTime.now();
        this.createdAt = this.createdAt.truncatedTo(ChronoUnit.MICROS);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name)
                && Objects.equals(email, user.email) && Objects.equals(age, user.age)
                && Objects.equals(createdAt, user.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age, createdAt);
    }
}