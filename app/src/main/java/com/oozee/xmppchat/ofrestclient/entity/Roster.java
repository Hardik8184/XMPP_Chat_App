package com.oozee.xmppchat.ofrestclient.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.RosterGroup;

import java.io.Serializable;

public class Roster implements Serializable {
    @JsonProperty("groups")
    private RosterGroup groups;
    private String jid;
    private String nickname;
    private int subscriptionType;

    public Roster() {

    }

    public Roster(String jid, String nickname, int subscriptionType) {
        this.jid = jid;
        this.nickname = nickname;
        this.subscriptionType = subscriptionType;
    }

    public String getJid() {
        return this.jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSubscriptionType() {
        return this.subscriptionType;
    }

    public void setSubscriptionType(int subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public RosterGroup getGroups() {
        return this.groups;
    }

    public void setGroups(RosterGroup groups) {
        this.groups = groups;
    }
}
