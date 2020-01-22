package nl.niekvangogh.sudoku.service;

import nl.niekvangogh.sudoku.pojo.Game;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;

public interface IGameService {

    void onGameEnd(Game game, User user);

    void onGameStart(Game game);

    void onPlayerJoin(Game game, User user);

    void onPlayerDisconnect(Game game, User user);

    void onPlayerReady(Game game, User user, boolean ready);

    void onPlayerSubmitTile(Game game, User user, Tile tile, int value);

    void onPlayerAddPotentialTile(Game game, User user, Tile tile, int value);

    void onPlayerRemovePotentialTile(Game game, User user, Tile tile, int value);

}