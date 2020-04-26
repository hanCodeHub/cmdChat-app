package com.handev.cmdChat.model;

import java.util.List;

/**
 * Builder class for User objects.
 * Username is required.
 * @author Han Xu
 */
public class UserBuilder {
    private final String name;
    private Integer oauthClientId;
    private String password;
    private List<TextMessage> messages;

    public UserBuilder(String name) {
        this.name = name;
    }

    public UserBuilder withOauthClientId(Integer oauthClientId) {
        this.oauthClientId = oauthClientId;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withMessages(List<TextMessage> messages) {
        this.messages = messages;
        return this;
    }

    public User build() {
        return new User(this);
    }

    // GETTERS

    public String getName() {
        return name;
    }

    public Integer getOauthClientId() {
        return oauthClientId;
    }

    public String getPassword() {
        return password;
    }

    public List<TextMessage> getMessages() {
        return messages;
    }
}