package com.example.demo.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@PrimaryKeyJoinColumn(name = "User")
public class Author extends User {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
    private String firstname;
    private String lastname;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "author")
    private List<Article> articles = new ArrayList<>();

    public Author() {
    }

    public Author(String username, String email, UserStatus status, String hashedPassword,
                  String firstname, String lastname, List<Article> articles) {
        super(username, email, status, hashedPassword);
        this.firstname = firstname;
        this.lastname = lastname;
        this.articles = articles;
    }

    public Author(String username, String email, UserStatus status, String hashedPassword,
                  String firstname, String lastname) {
        super(username, email, status, hashedPassword);
        this.firstname = firstname;
        this.lastname = lastname;
    }

        public Author(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public String getFullName() {

        return String.join(" ", firstname, lastname);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
