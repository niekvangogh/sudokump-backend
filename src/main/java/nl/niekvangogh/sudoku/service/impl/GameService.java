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
    public synchronized void onGameStart(Game game) {
        if (game.getGameDetails().getGameState().equals(GameState.STARTED)) {
            return;
        }
        game.getGameDetails().setGameState(GameState.STARTED);

        Sudoku sudoku = this.sudokuService.generateSudoku(9);
        this.sudokuService.createPuzzle(sudoku, 40);
        game.setDefaultSudoku(sudoku);


        game.getUserMap().forEach((id, gamePlayer) -> {
            gamePlayer.setSudoku(new Sudoku(game.getDefaultSudoku()));
        });

        List<PublicUser> players = game.getUserMap().values().stream().map(gamePlayer -> new PublicUser(gamePlayer.getUser())).collect(Collectors.toList());
        for (GamePlayer gamePlayer : game.getUserMap().values()) {
            this.messageSendingService.convertAndSendToUser(gamePlayer.getUser().getName(), "/game/sudoku/start", new GameStartResponse(true, players));
        }
    }

    @Override
    public void onPlayerJoin(Game game, User user) {
        game.getUserMap().put(user.getId(), new GamePlayer(user));

        if (game.getUserMap().size() == 2) {
            game.getUserMap().forEach((id, gamePlayer) -> {
                this.messageSendingService.convertAndSendToUser(gamePlayer.getUser().getName(), "/game/queue/status", new QueueUpdateResponse(game.getGameDetails().getId()));
            });
        }
    }

    @Override
    public void onPlayerDisconnect(Game game, User user) {
        game.getUserMap().remove(user.getId());
    }

    @Override
    public synchronized void onPlayerReady(Game game, User user, boolean ready) {
        game.getGamePlayer(user.getId()).setReady(ready);

        if (!game.getGameDetails().getGameState().equals(GameState.NOT_STARTED)) {
            List<PublicUser> players = game.getUserMap().values().stream().map(gamePlayer -> new PublicUser(gamePlayer.getUser())).collect(Collectors.toList());

            this.messageSendingService.convertAndSendToUser(user.getName(), "/game/sudoku/start", new GameStartResponse(true, players));
            return;
        }

        if (game.getUserMap().size() == 2 && game.getUserMap().values().stream().allMatch(GamePlayer::isReady)) {
            if (game.getGameDetails().getGameState().equals(GameState.NOT_STARTED)) {
                this.onGameStart(game);

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
}
