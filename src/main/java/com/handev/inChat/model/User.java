package com.handev.inChat.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Bean for a User which has a collection of TextMessage
 * @author Han Xu
 */
@Entity
@Table(name = "users")
public class User {

    // TODO: 4/20/20 implement Builder class for User

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // auto-generated primary key

    @Column(unique = true)
    private Integer oauthClientId;  // id obtained from 3rd party OAuth2 client authenticator

    @Size(min = 2, max = 50, message = "User name should be between 2-50 characters.")
    private String name;

    @Size(min = 5, max = 50, message = "Password must be between 5-50 characters.")
    private String password;

    @OneToMany(mappedBy = "user")  // mapped to user column in text_messages table
    private List<TextMessage> messages;  // one user to many text messages

    // TODO: 4/19/20
    // ADD A LIST OF CHANNELS HERE THAT USER INSTANCE BELONGS TO

    /* constructors */

    public User() {}

    public User(String name) {
        this.name = name;
    }

    public User(String name, Integer oauthClientId) {
        this.oauthClientId = oauthClientId;
        this.name = name;
    }

    /* getters and setters */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOauthClientId() {
        return oauthClientId;
    }

    public void setOauthClientId(Integer clientId) {
        this.oauthClientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TextMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<TextMessage> messages) {
        this.messages = messages;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
