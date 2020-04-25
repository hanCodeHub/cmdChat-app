package com.handev.cmdChat.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Spring config class with boilerplate code for Websocket Message Broker.
 * @see <a href="https://spring.io/guides/gs/messaging-stomp-websocket/">Spring Websocket</a>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registers STOMP endpoint, enabling fallback if WebSocket not available.
     * @param registry is the object that registers STOMP endpoints
     */
    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {

        // allows SockJs frontend to connect to this STOMP server
        registry.addEndpoint("/ws").withSockJS();
    }

    /**
     * Configures web socket message broker.
     * @param registry object that registers message brokers
     */
    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {

        // maps all incoming messages from endpoints with /app to methods with @MessageMapping
        registry.setApplicationDestinationPrefixes("/app");

        // message broker broadcasts messages to client subscribers of given destinations
        registry.enableSimpleBroker("/topic/", "/queue/");
    }

}
