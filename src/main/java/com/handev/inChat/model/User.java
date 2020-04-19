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

    @Id
    @GeneratedValue
    private Integer id;  // auto-generated primary key

    @Column(unique = true)
    @Size(min = 2, max = 50, message = "User name should be between 2-50 characters.")
    private String name;

    @OneToMany(mappedBy = "user")  // mapped to user column in text_messages table
    private List<TextMessage> messages;  // one user to many text messages

    // ADD A LIST OF CHANNELS HERE

    public User() {}

    public User(String name) {
        this.name = name;
    }

    /* getters and setters */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
