package com.oozee.xmppchat.ofrestclient.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.Admins;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.Members;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Group implements Serializable {
    private Admins admins;
    private String description;
    private Members members;
    private String name;

    public Group() {

    }

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Members getMembers() {
        return this.members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }

    public Admins getAdmins() {
        return this.admins;
    }

    public void setAdmins(Admins admins) {
        this.admins = admins;
    }
}
