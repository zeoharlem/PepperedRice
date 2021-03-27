package com.zeoharlem.gads.pepperedrice.models;

import java.util.Objects;

public class ChatMessage {
    private String uid;
    private String message;
    private String messageId, senderId;
    private long timestamp;

    public ChatMessage(){

    }

    public ChatMessage(String message, String uid, long timestamp) {
        this.message    = message;
        this.uid        = uid;
        this.timestamp  = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return getUid().equals(that.getUid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid());
    }
}
