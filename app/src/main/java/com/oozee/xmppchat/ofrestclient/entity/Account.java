package com.oozee.xmppchat.ofrestclient.entity;

public class Account {
    private AuthenticationToken authenticationToken;
    private String host;
    private int port;

    public Account(String ip, int port) {
        this.host = ip;
        this.port = port;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public AuthenticationToken getAuthenticationToken() {
        return this.authenticationToken;
    }

    public void setAuthenticationToken(AuthenticationToken authenticationToken) {
        this.authenticationToken = authenticationToken;
    }
}
