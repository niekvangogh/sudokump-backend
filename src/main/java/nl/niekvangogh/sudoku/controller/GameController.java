package nl.niekvangogh.sudoku.controller;

import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.exception.ApiException;
import nl.niekvangogh.sudoku.exception.NotInGameException;
import nl.niekvangogh.sudoku.exception.ResourceNotFoundException;
import nl.niekvangogh.sudoku.pojo.GamePlayer;
import nl.niekvangogh.sudoku.pojo.game.UpdateTileRequest;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;
import nl.niekvangogh.sudoku.repository.UserRepository;
import nl.niekvangogh.sudoku.security.CurrentUser;
import nl.niekvangogh.sudoku.security.UserPrincipal;
import nl.niekvangogh.sudoku.service.impl.GameManagerService;
import nl.niekvangogh.sudoku.service.impl.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    private Logger logger = LoggerFactory.getLogger("GameController");

    @Autowired
    private GameManagerService gameManagerService;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/game/queue/start")
    public void startQueue(Message<Object> message, @Payload String payload, User user, SimpMessageHeaderAccessor accessor) {
        this.gameManagerService.queuePlayer(user);
    }

    @MessageMapping("/game/sudoku/ready")
    public void onReady(Message<Object> message, @Payload String payload, User user, SimpMessageHeaderAccessor accessor) {
        Game game = this.gameManagerService.findGameByUser(user);
        this.gameService.onPlayerReady(game, user, true);
    }

    @MessageMapping("/game/sudoku/updateTile")
    public void updateTile(Message<Object> message, @Payload UpdateTileRequest request, User user, SimpMessageHeaderAccessor accessor) {
        Game game = this.gameManagerService.findGameByUser(user);

        Sudoku sudoku = game.getGamePlayer(user.getId()).getSudoku();
        Tile tile = sudoku.getGrid()[request.getX()][request.getY()];

        switch (request.getMethod()) {
            case "removePotentialSolution":
                this.gameService.onPlayerRemovePotentialTile(game, user, tile, request.getNumber());
                break;
            case "addPotentialSolution":
                this.gameService.onPlayerAddPotentialTile(game, user, tile, request.getNumber());
                break;
            case "setGuess":
                this.gameService.onPlayerSubmitTile(game, user, tile, request.getNumber());
                break;
            default:
                this.logger.warn(String.format("UNKNOWN MESSAGE FOUND %s", request));
                break;
        }
    }

    @GetMapping("/sudoku")
    @PreAuthorize("hasRole('USER')")
    public Tile[][] getSudoku(@CurrentUser UserPrincipal userPrincipal, @Param("gameId") int gameId) throws ApiException {
        User user = this.userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        Game game = this.gameManagerService.findGameById(gameId);
        if (game == null || game.getGamePlayer(user.getId()) == null) {
            throw new NotInGameException(game, user);
        }
        GamePlayer player = game.getGamePlayer(user.getId());
        return player.getSudoku().toPlayerGrid();
    }

    @MessageExceptionHandler
    @SendToUser("/game/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}