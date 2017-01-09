package com.oozee.xmppchat.utils;

public class Error {
    private String message;

    public Error(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
