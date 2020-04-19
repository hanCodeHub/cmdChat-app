package com.handev.inChat.controller;

import com.handev.inChat.model.TextMessage;
import com.handev.inChat.model.User;
import com.handev.inChat.model.UserRepo;
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
 * Controller for handling and broadcasting User events.
 * @author Han Xu
 */
@Controller
public class UserController {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

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

        // saves user to DB
        User user = new User(message.getSender());
        userRepo.save(user);

        // adds timestamp to message
        LOGGER.info(message.getSender() + " has joined " + channel);
        message.setDateTime(LocalDateTime.now());
        return message;  // broadcasts message to channel
    }
}
