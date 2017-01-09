package com.oozee.xmppchat.ofrestclient.entity;

public class Statistics {
    private String clusterSessions;
    private String localSessions;

    public String getClusterSessions() {
        return this.clusterSessions;
    }

    public void setClusterSessions(String clusterSessions) {
        this.clusterSessions = clusterSessions;
    }

    public String getLocalSessions() {
        return this.localSessions;
    }

    public void setLocalSessions(String localSessions) {
        this.localSessions = localSessions;
    }
}
