package com.oozee.xmppchat.ofrestclient.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Participants {
    @JsonProperty("participant")
    List<Participant> participants;

    public Participants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<Participant> getParticipants() {
        return this.participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
