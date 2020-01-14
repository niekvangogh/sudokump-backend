package nl.niekvangogh.sudoku.config.adapter;

import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.repository.UserRepository;
import nl.niekvangogh.sudoku.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class WebSocketAuthenticatorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String jwtToken) throws AuthenticationException {

        Long userId = this.tokenProvider.getUserIdFromToken(jwtToken);

        if (!this.tokenProvider.validateToken(jwtToken)) {
            System.out.println("error");
        }

        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new BadCredentialsException("Bad credentials for user " + userId);
        }

        User user = optionalUser.get();

        return new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                Collections.singleton((GrantedAuthority) () -> "USER")
        );
    }
}