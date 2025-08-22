package com.instar.config;
import com.instar.config.interceptor.JwtHandshakeInterceptor;
import com.instar.config.interceptor.UserIdHandshakeHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat") // ep kết nối
                .addInterceptors(jwtHandshakeInterceptor)
                .setHandshakeHandler(new UserIdHandshakeHandler())
                .setAllowedOriginPatterns("http://localhost:63342", "http://localhost:8080", "http://127.0.0.1:5500")
                .withSockJS();// bật hỗ trợ SockJS cho phép kết nối mà ko sợ brower ko hỗ trợ
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // client có thể subscribe /topic/** và /queue/**
        config.enableSimpleBroker("/topic", "/queue");
        // prefix cho @MessageMapping
        config.setApplicationDestinationPrefixes("/app");
        // mỗi user có queue riêng
        config.setUserDestinationPrefix("/user");
    }
}
