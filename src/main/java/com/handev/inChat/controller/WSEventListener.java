package com.handev.inChat.controller;

import com.handev.inChat.model.MessageState;
import com.handev.inChat.model.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

/**
 * Listens for and handles events on the WebSocket connection
 * @author Han Xu
 */
@Component
public class WSEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WSEventListener.class);

    // contains methods for working with STOMP messages
    @Autowired
    private SimpMessageSendingOperations sendOps;

    /**
     * Listens for and handles a disconnected session event.
     * Triggers either from user closing browser tab or clicking Logout.
     * @param event obj representing a disconnected session
     */
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        // StompHeaderAccessor creates message from decoded STOMP frame
        // or encodes message to STOMP frame
        var accessor = StompHeaderAccessor.wrap(event.getMessage());

        // retrieves user's username and channel from session map
        Map<String, Object> sessionMap = accessor.getSessionAttributes();
        if (sessionMap != null && !sessionMap.isEmpty()) {
            String userName = (String) sessionMap.get("username");
            String channel = (String) sessionMap.get("channel");

            // logs the user disconnecting and broadcasts to subscribers
            LOGGER.info(userName + " has disconnected from " + channel);
            // constructs message and send to clients subscribed to given channel
            TextMessage message = new TextMessage(
                    MessageState.DISCONNECT,
                    userName
            );

            // updates client channel
            String endpoint = "/topic/public/" + channel;
            sendOps.convertAndSend(endpoint, message);

        }

    }

}
