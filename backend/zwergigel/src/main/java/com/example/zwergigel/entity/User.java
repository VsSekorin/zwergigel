package com.example.zwergigel.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String password;
    private String name;

    public User() {
    }

    public User(final String login, final String password, final String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }
}
