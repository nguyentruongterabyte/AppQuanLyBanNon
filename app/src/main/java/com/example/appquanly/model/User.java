package com.example.appquanly.model;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String email;
    private String username;
    private String mobile;

    public User(int id, String email, String username, String mobile) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.mobile = mobile;
    }
    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getMobile() {
        return mobile;
    }
}

