package com.oozee.xmppchat.ofrestclient.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Users {
    @JsonProperty("user")
    List<User> users;

    public Users() {

    }

    public Users(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
