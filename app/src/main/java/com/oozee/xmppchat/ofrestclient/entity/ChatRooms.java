package com.oozee.xmppchat.ofrestclient.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChatRooms {
    @JsonProperty("chatRoom")
    List<ChatRoom> chatRooms;

    public ChatRooms(List<ChatRoom> mucRooms) {
        this.chatRooms = mucRooms;
    }

    public List<ChatRoom> getChatRooms() {
        return this.chatRooms;
    }

    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }
}
