package com.handev.cmdChat.model;

/**
 * Controls constants for the state property on a message.
 *
 * @author Han Xu
 */
public enum MessageState {
  CHAT, // messages sent to client when users or channels are chatting.
  CONNECT, // messages sent to client when a user has connected.
  DISCONNECT // messages sent to client when a user has disconnected.
}
