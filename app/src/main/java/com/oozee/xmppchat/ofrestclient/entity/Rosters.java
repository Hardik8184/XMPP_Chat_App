package com.oozee.xmppchat.ofrestclient.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Rosters {
    @JsonProperty("rosterItem")
    private List<Roster> roster;

    public Rosters() {
    }

    public Rosters(List<Roster> roster) {
        this.roster = roster;
    }

    public List<Roster> getRoster() {
        return this.roster;
    }

    public void setRoster(List<Roster> roster) {
        this.roster = roster;
    }
}
