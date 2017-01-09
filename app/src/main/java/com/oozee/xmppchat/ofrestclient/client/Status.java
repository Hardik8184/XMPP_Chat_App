package com.oozee.xmppchat.ofrestclient.client;

public class Status {
    private int code;
    private String message;

    public Status() {
    }

    public Status(int code) {
        this.code = code;
    }

    public Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "code " + this.code + " message : " + this.message;
    }
}
