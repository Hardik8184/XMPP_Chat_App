package com.oozee.xmppchat.ofrestclient.entity.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class AdminGroups implements Serializable {
    @JsonProperty("adminGroup")
    private List<String> adminGroups;

    public List<String> getAdminGroups() {
        return this.adminGroups;
    }

    public void setAdminGroups(List<String> adminGroups) {
        this.adminGroups = adminGroups;
    }

    public List<String> asList() {
        return this.adminGroups;
    }
}
