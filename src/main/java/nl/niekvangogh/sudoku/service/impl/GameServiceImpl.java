package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.GamePlayer;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;
import nl.niekvangogh.sudoku.service.GameService;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    @Override
    public void onGameEnd(Game game) {

    }

    @Override
    public void onGameStart(Game game) {
        Sudoku sudoku = new Sudoku(game.getGameDetails().getSeed(), 9);
        for (User user : game.getGameDetails().getUsers()) {
            game.getUserMap().put(user, new GamePlayer(sudoku, null));
        }

    }

    @Override
    public void onPlayerJoin(Game game, User user) {

    }

    @Override
    public void onPlayerDisconnect(Game game, User user) {

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
