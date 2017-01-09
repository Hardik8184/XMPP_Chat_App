package com.oozee.xmppchat.bean;

public class Login {

    private String username;
    private String password;
    private String jid;

    public Login(String username, String password, String jid) {
        this.username = username;
        this.password = password;
        this.jid = jid;
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

    public String getJid() {
        return this.jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }
}
