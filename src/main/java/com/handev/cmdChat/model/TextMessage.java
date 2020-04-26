package com.handev.cmdChat.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Bean for a TextMessage sent between client and server.
 *
 * @author Han Xu
 */
@Entity
@Table(name = "text_messages")
public class TextMessage {

  @Id @GeneratedValue private Integer id; // auto-generated primary key

  @NonNull
  @Enumerated(EnumType.STRING) // saves in DB as String
  private MessageState state; // one of the states in MessageState

  @Size(min = 1, max = 200, message = "text content should be between 1 - 200 characters.")
  private String content; // text content of the message

  @Size(min = 2, max = 50, message = "username should should be between 2 - 50 characters.")
  private String sender; // username of the sender, same as User.getName()

  private LocalDateTime dateTime; // timestamp of message

  @ManyToOne(fetch = FetchType.LAZY) // lazy loading
  @JsonBackReference // prevent recursive calls
  private User user; // many text messages to one user

  /** Default constructor for auto mapping. */
  public TextMessage() {}

  public TextMessage(TextMessageBuilder builder) {
    this.content = builder.getContent();
    this.sender = builder.getSender();
    this.state = builder.getState();
    this.dateTime = builder.getDateTime();
    this.user = builder.getUser();
  }

  /* GETTERS / SETTERS  */

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "TextMessage{"
        + "state="
        + state
        + ", content='"
        + content
        + '\''
        + ", name='"
        + sender
        + '\''
        + '}';
  }
}
