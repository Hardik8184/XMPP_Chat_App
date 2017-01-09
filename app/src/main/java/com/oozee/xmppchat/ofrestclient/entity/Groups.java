package com.oozee.xmppchat.ofrestclient.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Groups {
    @JsonProperty("group")
    List<Group> groups;

    public Groups() {

    }

    public Groups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
