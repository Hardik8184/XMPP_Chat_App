package com.oozee.xmppchat.ofrestclient.entity.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Outcasts implements Serializable {
    @JsonProperty("outcast")
    private List<String> outcasts;

    public List<String> getOutcasts() {
        return this.outcasts;
    }

    public void setOutcasts(List<String> outcasts) {
        this.outcasts = outcasts;
    }

    public List<String> asList() {
        return this.outcasts;
    }
}
