package qubiqule.cheqr.cheQR.service.utility;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker 
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {  
    @Override  
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {  
        // Enable simple in-memory broker for message delivery  
        config.enableSimpleBroker("/topic", "/queue");  
        // Prefix for messages from clients  
        config.setApplicationDestinationPrefixes("/app");  
    }  

    @Override  
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {  
        // Register the STOMP endpoint for WebSocket connections  
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();  
    }   
}  
