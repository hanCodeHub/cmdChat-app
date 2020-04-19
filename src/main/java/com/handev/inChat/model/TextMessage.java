package com.handev.inChat.model;


import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Bean for a TextMessage send between client and server.
 * @author Han Xu
 */
@Entity
@Table(name = "text_messages")
public class TextMessage {

    @Id
    @GeneratedValue
    private Integer id;  // primary key is autogenerated

    @NonNull
    @Enumerated(EnumType.STRING)  // saves in DB as String
    private MessageState state;  // one of the states in MessageState

    @Size(min=1, max=200, message="text content should be between 1 - 200 characters.")
    private String content;  // text content of the message

    @Size(min=2, max=50, message="username should should be between 2 - 50 characters.")
    private String sender;  // username of the sender

    private LocalDateTime dateTime;  // timestamp of message


    /**
     * Default constructor for auto mapping.
     */
    public TextMessage() {
    }

    /**
     * Constructor with sender's username and state. Datetime always stamped with now.
     * @param state message state
     * @param sender sender username
     */
    public TextMessage(MessageState state, String sender) {
        this.state = state;
        this.sender = sender;
        this.dateTime = LocalDateTime.now();
    }

    /* GETTERS / SETTERS  */

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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "state=" + state +
                ", content='" + content + '\'' +
                ", name='" + sender + '\'' +
                '}';
    }
}
