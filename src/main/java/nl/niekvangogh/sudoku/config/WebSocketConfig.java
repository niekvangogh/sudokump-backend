package nl.niekvangogh.sudoku.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.lang.invoke.MethodHandles;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/queue", "/game");
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        log.info("<==> handleSubscribeEvent: username=" + event.getUser().getName() + ", event=" + event);
    }

    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        log.info("===> handleConnectEvent: username=" + event.getUser().getName() + ", event=" + event);
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        log.info("<=== handleDisconnectEvent: username=" + event.getUser().getName() + ", event=" + event);
    }
}