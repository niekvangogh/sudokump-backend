package nl.niekvangogh.sudoku.controller;

import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.exception.ResourceNotFoundException;
import nl.niekvangogh.sudoku.pojo.game.RemovePotentialGuess;
import nl.niekvangogh.sudoku.pojo.game.SubmitGuessRequest;
import nl.niekvangogh.sudoku.pojo.game.SubmitPotentialGuess;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;
import nl.niekvangogh.sudoku.repository.UserRepository;
import nl.niekvangogh.sudoku.security.CurrentUser;
import nl.niekvangogh.sudoku.security.UserPrincipal;
import nl.niekvangogh.sudoku.service.impl.GameManagerService;
import nl.niekvangogh.sudoku.service.impl.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameManagerService gameManagerService;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @MessageMapping("/game/queue/start")
    public void startQueue(Message<Object> message, @Payload String payload, User user, SimpMessageHeaderAccessor accessor) {
        this.gameManagerService.queuePlayer(user, accessor.getSessionId());


        this.simpUserRegistry.getUsers();
    }

    @MessageMapping("/game/queue/cancel")
    public void cancelQueue() {
//        this.gameManagerService.cancelQueue(player);
    }

    @MessageMapping("/game/sudoku/ready")
    public void onReady(Message<Object> message, @Payload String payload, User user, SimpMessageHeaderAccessor accessor) {
        Game game = this.gameManagerService.getGame(user);

        this.gameService.onPlayerReady(game, user, true);
    }

    @MessageMapping("/game/sudoku/setGuess")
    public void onPlayerSetGuess(Message<Object> message, @Payload SubmitGuessRequest submittedGuess, User user, SimpMessageHeaderAccessor accessor) {
        Game game = this.gameManagerService.getGame(user);


        //todo: check if in game


        Sudoku sudoku = game.getGamePlayer(user.getId()).getSudoku();
        Tile tile = sudoku.getGrid()[submittedGuess.getX()][submittedGuess.getY()];

        this.gameService.onPlayerSubmitTile(game, user, tile, submittedGuess.getGuess());
    }

    @MessageMapping("/game/sudoku/addPotentialTile")
    public void addPotentialTile(Message<Object> message, @Payload SubmitPotentialGuess potentialGuess, User user, SimpMessageHeaderAccessor accessor) {
        Game game = this.gameManagerService.getGame(user);


        //todo: check if in game


        Sudoku sudoku = game.getGamePlayer(user.getId()).getSudoku();
        Tile tile = sudoku.getGrid()[potentialGuess.getX()][potentialGuess.getY()];

        this.gameService.onPlayerAddPotentialTile(game, user, tile, potentialGuess.getGuess());
    }

    @MessageMapping("/game/sudoku/removePotentialTile")
    public void removePotentialTile(Message<Object> message, @Payload RemovePotentialGuess removePotentialGuess, User user, SimpMessageHeaderAccessor accessor) {
        Game game = this.gameManagerService.getGame(user);


        //todo: check if in game


        Sudoku sudoku = game.getGamePlayer(user.getId()).getSudoku();
        Tile tile = sudoku.getGrid()[removePotentialGuess.getX()][removePotentialGuess.getY()];

        this.gameService.onPlayerRemovePotentialTile(game, user, tile, removePotentialGuess.getGuess());
    }

    @GetMapping("/sudoku")
    @PreAuthorize("hasRole('USER')")
    public Tile[][] getSudoku(@CurrentUser UserPrincipal userPrincipal, @Param("gameId") int gameId) throws Exception {
        User user = this.userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        Game game = this.gameManagerService.getGame(user);
        if (this.gameManagerService.getGame(user) == null) {
            throw new Exception("not in game");
        }

        return game.getGamePlayer(user.getId()).getSudoku().toPlayerGrid();
    }

    @MessageExceptionHandler
    @SendToUser("/game/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}