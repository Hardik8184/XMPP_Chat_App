package com.oozee.xmppchat.ofrestclient.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Occupants {
    @JsonProperty("occupant")
    List<Occupant> occupants;

    public Occupants(List<Occupant> participants) {
        this.occupants = participants;
    }

    public List<Occupant> getOccupants() {
        return this.occupants;
    }

    public void setOccupants(List<Occupant> occupants) {
        this.occupants = occupants;
    }
}
