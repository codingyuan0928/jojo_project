package com.group1.gosports_jojo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/user"); // 啟用 "/topic" 和 "/user"
        config.setApplicationDestinationPrefixes("/app"); // 設置發送消息的前綴
        config.setUserDestinationPrefix("/user"); // 設置用戶訂閱的前綴
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:8080")  // 設置前端允許的來源
                //.addInterceptors(new CustomHandshakeInterceptor()) // 添加攔截器
                .withSockJS();
    }

}