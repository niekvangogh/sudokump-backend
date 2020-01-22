package nl.niekvangogh.sudoku.controller;

import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.exception.ResourceNotFoundException;
import nl.niekvangogh.sudoku.pojo.PublicUser;
import nl.niekvangogh.sudoku.repository.UserRepository;
import nl.niekvangogh.sudoku.security.CurrentUser;
import nl.niekvangogh.sudoku.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @GetMapping("/player/{id}")
    public PublicUser getPlayer(@PathVariable long id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));
        return new PublicUser(user);
    }
}
