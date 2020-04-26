package com.handev.cmdChat.model;

import java.time.LocalDateTime;

/**
 * Builder class for TextMessage objects. MessageState is required.
 *
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

  /** Optional builder method to add content to TextMessage product. */
  public TextMessageBuilder withContent(String content) {
    this.content = content;
    return this;
  }

  /** Optional builder method to add sender to TextMessage product. */
  public TextMessageBuilder withSender(String sender) {
    this.sender = sender;
    return this;
  }

  /** Optional builder method to add datetime to TextMessage product. */
  public TextMessageBuilder withDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
    return this;
  }

  /** Optional builder method to add user to TextMessage product. */
  public TextMessageBuilder withUser(User user) {
    this.user = user;
    return this;
  }

  /**
   * Builds the TextMessage product.
   *
   * @return new TextMessage with the optional properties built.
   */
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
