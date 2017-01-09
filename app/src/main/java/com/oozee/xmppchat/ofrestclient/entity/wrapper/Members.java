package com.oozee.xmppchat.ofrestclient.entity.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Members implements Serializable {
    @JsonProperty("member")
    private List<String> members;

    public List<String> getMembers() {
        return this.members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<String> asList() {
        return this.members;
    }
}
