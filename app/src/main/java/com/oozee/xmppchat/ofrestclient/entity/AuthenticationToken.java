package com.oozee.xmppchat.ofrestclient.entity;

public class AuthenticationToken {
    private AuthenticationMode authMode;
    private String password;
    private String sharedSecretKey;
    private String username;

    public AuthenticationToken(String username, String password) {
        this.authMode = AuthenticationMode.BASIC_AUTH;
        this.username = username;
        this.password = password;
    }

    public AuthenticationToken(String sharedSecretKey) {
        this.authMode = AuthenticationMode.SHARED_SECRET_KEY;
        this.sharedSecretKey = sharedSecretKey;
    }

    public AuthenticationMode getAuthMode() {
        return this.authMode;
    }

    public void setAuthMode(AuthenticationMode authMode) {
        this.authMode = authMode;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSharedSecretKey() {
        return this.sharedSecretKey;
    }

    public void setSharedSecretKey(String sharedSecretKey) {
        this.sharedSecretKey = sharedSecretKey;
    }
}
