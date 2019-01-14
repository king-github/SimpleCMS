package com.example.demo.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    public enum UserStatus { UNDEFINED, NEW, ACTIVE, INACTIVE }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(unique=true, nullable=false)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.UNDEFINED;

    private String hashedPassword;


    public User() {
    }

    public User(String username, String email, UserStatus status, String hashedPassword) {
        this.username = username;
        this.email = email;
        this.status = status;
        this.hashedPassword = hashedPassword;
    }

    public User(Long id, String username, String email, UserStatus status, String hashedPassword) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.hashedPassword = hashedPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
