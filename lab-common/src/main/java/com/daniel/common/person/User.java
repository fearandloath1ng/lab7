package com.daniel.common.person;

import java.io.Serializable;


public class User implements Serializable {

    static final long serialVersionUID = 6403294230871511967L;

    private String name;
    private String password;
    private int id;

    public User(String name, String password, int id) {
        this.name = name;
        this.password = password;
        this.id = id;
    }

    public User() {
    }

    public User(String login, String encryptPassword) {
        this.name = login;
        this.password = encryptPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
