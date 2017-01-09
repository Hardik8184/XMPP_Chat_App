package com.oozee.xmppchat.ofrestclient.entity.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class OutcastGroups implements Serializable {
    @JsonProperty("outcastGroup")
    private List<String> outcastGroups;

    public List<String> getOutcastGroups() {
        return this.outcastGroups;
    }

    public void setOutcastGroups(List<String> outcastGroups) {
        this.outcastGroups = outcastGroups;
    }

    public List<String> asList() {
        return this.outcastGroups;
    }
}
