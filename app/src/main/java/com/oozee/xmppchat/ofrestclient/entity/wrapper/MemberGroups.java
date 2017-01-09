package com.oozee.xmppchat.ofrestclient.entity.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class MemberGroups implements Serializable {
    @JsonProperty("memberGroup")
    private List<String> memberGroups;

    public List<String> getMemberGroups() {
        return this.memberGroups;
    }

    public void setMemberGroups(List<String> memberGroups) {
        this.memberGroups = memberGroups;
    }

    public List<String> asList() {
        return this.memberGroups;
    }
}
