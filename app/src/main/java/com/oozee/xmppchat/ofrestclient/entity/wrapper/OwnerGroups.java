package com.oozee.xmppchat.ofrestclient.entity.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class OwnerGroups implements Serializable {
    @JsonProperty("ownerGroup")
    private List<String> ownerGroups;

    public List<String> getOwnerGroups() {
        return this.ownerGroups;
    }

    public void setOwnerGroups(List<String> ownerGroups) {
        this.ownerGroups = ownerGroups;
    }

    public List<String> asList() {
        return this.ownerGroups;
    }
}
