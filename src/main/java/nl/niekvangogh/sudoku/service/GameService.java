package nl.niekvangogh.sudoku.service;

import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;

public interface GameService {

    void onGameEnd(Game game);

    void onGameStart(Game game);

    void onPlayerJoin(Game game, User user);

    void onPlayerDisconnect(Game game, User user);

    void onPlayerSubmitTile(Game game, User user, Tile tile, int value);

    void onPlayerAddPotentialTile(Game game, User user, Tile tile, int value);

    void onPlayerRemovePotentialTile(Game game, User user, Tile tile, int value);

}