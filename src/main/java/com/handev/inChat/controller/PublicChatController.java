package com.handev.inChat.controller;

import com.handev.inChat.model.TextMessage;
import com.handev.inChat.model.TextMessageRepo;
import com.handev.inChat.model.User;
import com.handev.inChat.model.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * Controller for handling and broadcasting public chat messages.
 * @author Han Xu
 */
@Controller
public class PublicChatController {

    // ************* REFACTOR MESSAGE STATES INTO STATE PATTERN ***************

    public static final Logger LOGGER = LoggerFactory.getLogger(PublicChatController.class);

    @Autowired
    private TextMessageRepo textMessageRepo;

    @Autowired
    private UserRepo userRepo;

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

        // adds current timestamp and user id to message
        message.setDateTime(LocalDateTime.now());

        // saves message to a User if sender name was provided
        if (message.getSender() != null) {
            User user = userRepo.findByName(message.getSender());
            System.out.println(user);

            message.setUser(user);
        }

        // saves message to repo and return it to channel
        textMessageRepo.save(message);
        return message;  // broadcasts message
    }

}
