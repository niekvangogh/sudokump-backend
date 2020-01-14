package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.GamePlayer;
import nl.niekvangogh.sudoku.pojo.game.GameReadyResponse;
import nl.niekvangogh.sudoku.pojo.queue.QueueUpdateResponse;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;
import nl.niekvangogh.sudoku.service.GameService;
import nl.niekvangogh.sudoku.service.SudokuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private SudokuServiceImpl sudokuService;

    @Autowired
    private SimpMessageSendingOperations messageSendingService;

    @Override
    public void onGameEnd(Game game) {

    }

    @Override
    public void onGameStart(Game game) {
        Sudoku sudoku = this.sudokuService.generateSudoku(9);
        game.setDefaultSudoku(sudoku);
        game.getUserMap().forEach((id, gamePlayer) -> gamePlayer.setSudoku(game.getDefaultSudoku()));
    }

    @Override
    public void onPlayerJoin(Game game, User user, String sessionId) {
        game.getUserMap().put(user.getId(), new GamePlayer(sessionId));
        this.messageSendingService.convertAndSendToUser(sessionId, "/game/queue/status", new QueueUpdateResponse(game.getGameDetails().getId()));
    }

    @Override
    public void onPlayerDisconnect(Game game, User user) {
        game.getUserMap().remove(user.getId());

    }

    @Override
    public void onPlayerReady(Game game, User user, boolean ready) {
        game.getGamePlayer(user.getId()).setReady(ready);

        if (game.getUserMap().values().stream().allMatch(GamePlayer::isReady)) {
            game.getUserMap().values().forEach(gamePlayer -> {
                this.messageSendingService.convertAndSendToUser(gamePlayer.getSessionId(), "/game/sudoku/start", new GameReadyResponse(true));
            });
        }
    }

    @Override
    public void onPlayerSubmitTile(Game game, User user, Tile tile, int value) {

    }

    @Override
    public void onPlayerAddPotentialTile(Game game, User user, Tile tile, int value) {

    }

    @Override
    public void onPlayerRemovePotentialTile(Game game, User user, Tile tile, int value) {

    }
}
