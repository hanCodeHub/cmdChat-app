package com.handev.inChat.model;

public class TextMessage {

    private MessageState state;

    private String content;

    private String sender;

    private String time;

    public TextMessage() {
    }

    public TextMessage(MessageState state, String sender) {
        this.state = state;
        this.sender = sender;
    }

    public MessageState getState() {
        return state;
    }

    public void setState(MessageState state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "state=" + state +
                ", content='" + content + '\'' +
                ", name='" + sender + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
