package com.oozee.xmppchat.ofrestclient.entity;

public class Message {
    private String body;

    public Message(String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
