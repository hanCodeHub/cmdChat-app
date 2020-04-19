package com.handev.inChat.controller;

import com.handev.inChat.model.TextMessage;
import com.handev.inChat.model.TextMessageRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Class for receiving messages from one client and broadcasting it to others
 */
@Controller
public class PublicChatController {

    // ************* REFACTOR MESSAGE STATES INTO STATE PATTERN ***************

    public static final Logger LOGGER = LoggerFactory.getLogger(WSEventListener.class);

    @Autowired
    private TextMessageRepo textMessageRepo;

    /**
     * Broadcasts incoming message to subscribers of /topic/public.
     * message.state == CHAT
     * @param message the TextMessage JSON received from client
     * @return the message payload to client subscribers of @SendTo destination
     */
    @MessageMapping("/chat.send/public/{channel}")
    @SendTo("/topic/public/{channel}")
    public TextMessage sendMessage(
            @DestinationVariable String channel,
            @Payload TextMessage message) {

        // adds timestamp to message and saves it to DB
        LOGGER.info("Message sent by " + message.getSender() + " in public channel " + channel);
        message.setDateTime(LocalDateTime.now());
        textMessageRepo.save(message);

        return message;  // broadcasts message to channel
    }

    /**
     * Handles a new user joining the chat. State == CONNECTED
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

        LOGGER.info(message.getSender() + " has joined " + channel);
        // adds timestamp to message and returns it to channel
        message.setDateTime(LocalDateTime.now());

        return message;  // broadcasts message to channel
    }

}
