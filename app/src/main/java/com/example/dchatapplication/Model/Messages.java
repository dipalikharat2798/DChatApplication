package com.example.dchatapplication.Model;

public class Messages {
    String message,senderId;
    long timeStamp;

    public Messages(){}
    public Messages(String msg, String sUid, long timeStamp) {
        this.message = msg;
        this.senderId = sUid;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
