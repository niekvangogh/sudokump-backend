package nl.niekvangogh.sudoku.controller;

import com.google.gson.Gson;
import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.GameDetails;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.exception.ResourceNotFoundException;
import nl.niekvangogh.sudoku.pojo.game.*;
import nl.niekvangogh.sudoku.pojo.queue.QueueUpdate;
import nl.niekvangogh.sudoku.pojo.queue.QueueUpdateResponse;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;
import nl.niekvangogh.sudoku.repository.UserRepository;
import nl.niekvangogh.sudoku.security.CurrentUser;
import nl.niekvangogh.sudoku.security.UserPrincipal;
import nl.niekvangogh.sudoku.service.impl.GameManagerServiceImpl;
import nl.niekvangogh.sudoku.service.impl.GameServiceImpl;
import nl.niekvangogh.sudoku.service.impl.SudokuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/game")
public class GameController {

    private static Gson GSON = new Gson();

    @Autowired
    private GameManagerServiceImpl gameManagerService;

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/game/queue/start")
    public void startQueue(Message<Object> message, @Payload String payload, Principal principal, SimpMessageHeaderAccessor accessor) {
        User user = this.userRepository.findByEmail(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("User", "email", principal.getName()));
        this.gameManagerService.queuePlayer(user, accessor.getSessionId());

    }

    @MessageMapping("/game/queue/cancel")
    public void cancelQueue() {
//        this.gameManagerService.cancelQueue(player);
    }

    @MessageMapping("/game/sudoku/ready")
    public void onReady(Message<Object> message, @Payload String payload, Principal principal, SimpMessageHeaderAccessor accessor) {
        User user = this.userRepository.findByEmail(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("User", "email", principal.getName()));
        Game game = this.gameManagerService.getGame(user);

        this.gameService.onPlayerReady(game, user, true);
    }

    @MessageMapping("/game/sudoku/setGuess")
    public void onPlayersetGuess(Message<Object> message, @Payload String payload, Principal principal, SimpMessageHeaderAccessor accessor) {
        User user = this.userRepository.findByEmail(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("User", "email", principal.getName()));
        Game game = this.gameManagerService.getGame(user);


        //todo: check if in game


        SubmitGuessRequest submittedGuess = GSON.fromJson(payload, SubmitGuessRequest.class);

        Sudoku sudoku = game.getGamePlayer(user.getId()).getSudoku();
        Tile tile = sudoku.getGrid()[submittedGuess.getX()][submittedGuess.getY()];

        this.gameService.onPlayerSubmitTile(game, user, tile, submittedGuess.getGuess());
    }

    @MessageMapping("/game/sudoku/addPotentialTile")
    public void addPotentialTile(Message<Object> message, @Payload String payload, Principal principal, SimpMessageHeaderAccessor accessor) {
        User user = this.userRepository.findByEmail(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("User", "email", principal.getName()));
        Game game = this.gameManagerService.getGame(user);


        //todo: check if in game


        SubmitPotentialGuess submittedGuess = GSON.fromJson(payload, SubmitPotentialGuess.class);

        Sudoku sudoku = game.getGamePlayer(user.getId()).getSudoku();
        Tile tile = sudoku.getGrid()[submittedGuess.getX()][submittedGuess.getY()];

        this.gameService.onPlayerAddPotentialTile(game, user, tile, submittedGuess.getGuess());
    }

    @MessageMapping("/game/sudoku/removePotentialTile")
    public void removePotentialTile(Message<Object> message, @Payload String payload, Principal principal, SimpMessageHeaderAccessor accessor) {
        User user = this.userRepository.findByEmail(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("User", "email", principal.getName()));
        Game game = this.gameManagerService.getGame(user);


        //todo: check if in game


        RemovePotentialGuess submittedGuess = GSON.fromJson(payload, RemovePotentialGuess.class);

        Sudoku sudoku = game.getGamePlayer(user.getId()).getSudoku();
        Tile tile = sudoku.getGrid()[submittedGuess.getX()][submittedGuess.getY()];

        this.gameService.onPlayerAddPotentialTile(game, user, tile, submittedGuess.getGuess());
    }

    @GetMapping("/sudoku")
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