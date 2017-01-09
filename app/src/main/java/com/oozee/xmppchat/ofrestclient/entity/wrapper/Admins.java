package com.oozee.xmppchat.ofrestclient.entity.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Admins implements Serializable {
    @JsonProperty("admin")
    private List<String> admins;

    public List<String> getAdmins() {
        return this.admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public List<String> asList() {
        return this.admins;
    }
}
