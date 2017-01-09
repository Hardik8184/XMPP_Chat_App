package com.oozee.xmppchat.ofrestclient.entity.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class BroadcastPresenceRoles implements Serializable {
    @JsonProperty("broadcastPresenceRole")
    private List<String> broadcastPresenceRoles;

    public List<String> getBroadcastPresenceRoles() {
        return this.broadcastPresenceRoles;
    }

    public void setBroadcastPresenceRoles(List<String> broadcastPresenceRoles) {
        this.broadcastPresenceRoles = broadcastPresenceRoles;
    }

    public List<String> asList() {
        return this.broadcastPresenceRoles;
    }
}
