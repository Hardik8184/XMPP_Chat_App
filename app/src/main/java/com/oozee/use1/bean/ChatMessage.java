package com.oozee.use1.bean;

import java.io.Serializable;

public class ChatMessage implements Serializable {

    public String user_id, msg_id, sender, message, date_time, is_msg_read;

    public ChatMessage(String user_id, String msg_id, String sender,
                       String message, String date_time, String is_msg_read) {

        this.user_id = user_id;

        this.msg_id = msg_id;

        this.sender = sender;

        this.message = message;

        this.date_time = date_time;

        this.is_msg_read = is_msg_read;

    }

    public String getUser_id() {

        return user_id;

    }

    public void setUser_id(String user_id) {

        this.user_id = user_id;

    }

    public String getSender() {

        return sender;

    }

    public void setSender(String sender) {

        this.sender = sender;

    }

    public String getMsg_id() {

        return msg_id;

    }

    public void setMsg_id(String msg_id) {

        this.msg_id = msg_id;

    }

    public String getMessage() {

        return message;

    }

    public void setMessage(String message) {

        this.message = message;

    }

    public String getDate_time() {

        return date_time;

    }

    public void setDate_time(String date_time) {

        this.date_time = date_time;

    }

    public String getIs_msg_read() {

        return is_msg_read;

    }

    public void setIs_msg_read(String is_msg_read) {

        this.is_msg_read = is_msg_read;

    }
}