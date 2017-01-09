package com.oozee.xmppchat.ofrestclient.entity;

import com.oozee.xmppchat.ofrestclient.entity.wrapper.PropertiesWrapper;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String name;
    private String password;
    private PropertiesWrapper properties;
    private String username;

    public User() {

    }

    public User(String username, String name, String email, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PropertiesWrapper getProperties() {
        return this.properties;
    }

    public void setProperties(PropertiesWrapper properties) {
        this.properties = properties;
    }
}
