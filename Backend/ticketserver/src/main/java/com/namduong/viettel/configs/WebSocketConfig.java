package com.namduong.viettel.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // registers the /ws endpoint for websocket connections.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*")
                .setAllowedOrigins("http://localhost:3000").withSockJS();
    }

    // config the message broker
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        //It also designates the /app prefix for messages that are bound for methods annotated with @MessageMapping.
        //This prefix will be used to define all the message mappings.
        // For example, /app/chat.addUser is the endpoint that the ChatController.addUser() handle
        registry.setApplicationDestinationPrefixes("/app");

        // enable a simple memory-based message broker to carry the chat messages to the clients
        // on destinations prefixed with '/topic'
        registry.enableSimpleBroker("/topic");
    }
}
