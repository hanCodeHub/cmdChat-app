package com.handev.cmdChat.model;

import java.time.LocalDateTime;

/**
 * Builder class for TextMessage objects.
 * MessageState is required.
 * @author Han Xu
 */
public class TextMessageBuilder {
    private final MessageState state;
    private String content;
    private String sender;
    private LocalDateTime dateTime;
    private User user;

    public TextMessageBuilder(MessageState state) {
        this.state = state;
    }

    public TextMessageBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public TextMessageBuilder withSender(String sender) {
        this.sender = sender;
        return this;
    }

    public TextMessageBuilder withDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public TextMessageBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public TextMessage build() {
        return new TextMessage(this);
    }

    // GETTERS

    public MessageState getState() {
        return state;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public User getUser() {
        return user;
    }
}
