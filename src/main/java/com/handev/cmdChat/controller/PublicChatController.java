package com.handev.cmdChat.controller;

import com.handev.cmdChat.model.*;
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
 * Controller for handling and broadcasting public chat messages.
 * @author Han Xu
 */
@Controller
public class PublicChatController {

    public static final Logger LOGGER = LoggerFactory.getLogger(PublicChatController.class);

    @Autowired
    private TextMessageRepo textMessageRepo;

    @Autowired
    private UserRepo userRepo;


    /**
     * Handles a new user joining the chat.
     * message.state == CONNECTED
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

        // TODO: 4/20/20 add subscribed channel to user

        // adds timestamp to message
        LOGGER.info(message.getSender() + " has joined " + channel);
        message.setDateTime(LocalDateTime.now());
        return message;  // broadcasts message to channel
    }


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

        LOGGER.info("Message sent by " + message.getSender() + " in public channel " + channel);

        // adds current timestamp to message
        message.setDateTime(LocalDateTime.now());

        // saves chat message to a User if sender name was provided
        if (message.getSender() != null && message.getState() == MessageState.CHAT) {
            User user = userRepo.findByName(message.getSender());
            message.setUser(user);
        }

        // saves message to repo and return it to channel
        textMessageRepo.save(message);
        return message;  // broadcasts message
    }

}
