package com.oozee.xmppchat.ofrestclient.entity.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class RosterGroup implements Serializable {
    @JsonProperty("group")
    private List<String> groups;

    public RosterGroup(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getGroups() {
        return this.groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String toString() {
        return this.groups.toString().replace("[", "").replace("]", "");
    }
}
