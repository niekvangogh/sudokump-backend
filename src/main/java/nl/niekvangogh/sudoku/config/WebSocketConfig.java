package nl.niekvangogh.sudoku.config;

import nl.niekvangogh.sudoku.config.adapter.UserInterceptor;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.exception.ResourceNotFoundException;
import nl.niekvangogh.sudoku.repository.UserRepository;
import nl.niekvangogh.sudoku.security.TokenProvider;
import nl.niekvangogh.sudoku.service.impl.GameManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.lang.invoke.MethodHandles;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameManagerService gameManagerService;


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
        registry.setUserDestinationPrefix("/user");
        registry.enableSimpleBroker("/queue", "/game");

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(new UserInterceptor(this.tokenProvider, this.userRepository));
    }

    @EventListener
    public void onSocketDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        User user = this.userRepository.findByName(event.getUser().getName()).orElseThrow(() -> new ResourceNotFoundException("User", "Name", event.getUser().getName()));

        if (this.gameManagerService.isInGame(user)) {
            this.gameManagerService.removePlayerFromAllGames(user);
        }

    }

}