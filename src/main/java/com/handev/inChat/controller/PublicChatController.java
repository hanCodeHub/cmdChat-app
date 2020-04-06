package com.handev.inChat.controller;

import com.handev.inChat.model.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * Class for receiving messages from one client and broadcasting it to others
 */
@Controller
public class PublicChatController {

    public static final Logger LOGGER = LoggerFactory.getLogger(WSEventListener.class);

    /**
     * Broadcasts incoming message to subscribers of /topic/public
     * @param message the TextMessage JSON received from client
     * @return the message payload to client subscribers of @SendTo destination
     */
    @MessageMapping("/chat.send/public/{channel}")
    @SendTo("/topic/public/{channel}")
    public TextMessage sendMessage(
            @DestinationVariable String channel,
            @Payload TextMessage message) {

        LOGGER.info("Message sent by " + message.getSender() + " in public channel " + channel);
        return message;
    }

    /**
     * Handles a new user joining the chat
     * @param message the TextMessage obj to be returned to client
     * @param accessor provides access to the user session
     * @return the message payload to client subscribers of @SendTo destination
     */
    @MessageMapping("/chat.newUser/public/{channel}")
    @SendTo("/topic/public/{channel}")
    public TextMessage newUser(
            @DestinationVariable String channel,
            @Payload TextMessage message,
            SimpMessageHeaderAccessor accessor) {

        // adds user's username and subscribed channel to web socket session map
        Map<String, Object> session = accessor.getSessionAttributes();
        if (session != null) {
            accessor.getSessionAttributes().put("username", message.getSender());
            accessor.getSessionAttributes().put("channel", channel);
        }
        // logs user joining and return payload message to client
        LOGGER.info(message.getSender() + " has joined " + channel);
        return message;
    }

}
