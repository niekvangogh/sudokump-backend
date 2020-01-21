package nl.niekvangogh.sudoku.config.adapter;

import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.repository.UserRepository;
import nl.niekvangogh.sudoku.security.TokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.LinkedList;
import java.util.Map;

public class UserInterceptor extends ChannelInterceptorAdapter {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public UserInterceptor(TokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

            if (raw instanceof Map) {
                Object name = ((Map) raw).get("Authorization");
                if (name instanceof LinkedList) {
                    String bearerToken = ((LinkedList) name).get(0).toString();
                    String jwtToken = bearerToken.split(" ")[1];

                    if (this.tokenProvider.validateToken(jwtToken)) {
                        Long userId = this.tokenProvider.getUserIdFromToken(jwtToken);
                        User user = this.userRepository.findById(userId).get();
                        accessor.setUser(user);
                    }
                }
            }
        }
        return message;
    }
}