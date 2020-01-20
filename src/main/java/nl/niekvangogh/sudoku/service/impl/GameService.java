package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.GamePlayer;
import nl.niekvangogh.sudoku.pojo.PublicUser;
import nl.niekvangogh.sudoku.pojo.game.GameStartResponse;
import nl.niekvangogh.sudoku.pojo.game.GameState;
import nl.niekvangogh.sudoku.pojo.queue.QueueUpdateResponse;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;
import nl.niekvangogh.sudoku.service.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService implements IGameService {

    @Autowired
    private SudokuService sudokuService;

    @Autowired
    private SimpMessageSendingOperations messageSendingService;

    @Override
    public void onGameEnd(Game game) {

    }

    @Override
    public void onGameStart(Game game) {
        Sudoku sudoku = this.sudokuService.generateSudoku(9);
        game.setDefaultSudoku(sudoku);

        this.sudokuService.createPuzzle(sudoku, 15);

        game.getUserMap().forEach((id, gamePlayer) -> gamePlayer.setSudoku(game.getDefaultSudoku()));
        game.getGameDetails().setGameState(GameState.STARTED);

        List<PublicUser> players = game.getUserMap().values().stream().map(gamePlayer -> new PublicUser(gamePlayer.getUser())).collect(Collectors.toList());
        for (GamePlayer gamePlayer : game.getUserMap().values()) {
            this.messageSendingService.convertAndSendToUser(gamePlayer.getSessionId(), "/game/sudoku/start", new GameStartResponse(true, players), this.createHeaders(gamePlayer.getSessionId()));
        }
    }

    @Override
    public void onPlayerJoin(Game game, User user, String sessionId) {
        game.getUserMap().put(user.getId(), new GamePlayer(sessionId, user));
        this.messageSendingService.convertAndSendToUser(sessionId, "/game/queue/status", new QueueUpdateResponse(game.getGameDetails().getId()), this.createHeaders(sessionId));
    }

    @Override
    public void onPlayerDisconnect(Game game, User user) {
        game.getUserMap().remove(user.getId());
    }

    @Override
    public void onPlayerReady(Game game, User user, boolean ready) {
        game.getGamePlayer(user.getId()).setReady(ready);

        if (game.getUserMap().values().stream().allMatch(GamePlayer::isReady)) {

            if (game.getGameDetails().getGameState().equals(GameState.NOT_STARTED)) {
                this.onGameStart(game);
            } else {
                List<PublicUser> players = game.getUserMap().values().stream().map(gamePlayer -> new PublicUser(gamePlayer.getUser())).collect(Collectors.toList());
                GamePlayer player = game.getGamePlayer(user.getId());
                this.messageSendingService.convertAndSendToUser(player.getSessionId(), "/game/sudoku/start", new GameStartResponse(true, players), this.createHeaders(player.getSessionId()));

            }
        }
    }

    @Override
    public void onPlayerSubmitTile(Game game, User user, Tile tile, int value) {
        tile.setGuess(value);
    }

    @Override
    public void onPlayerAddPotentialTile(Game game, User user, Tile tile, int value) {
        tile.getPotentialSolutions().add(value);
    }

    @Override
    public void onPlayerRemovePotentialTile(Game game, User user, Tile tile, int value) {
        tile.getPotentialSolutions().remove(value);

    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
