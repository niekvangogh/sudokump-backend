package nl.niekvangogh.sudoku.service;

import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.Player;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;

public interface GameService {

    void onGameEnd(Game game);

    void onGameStart(Game game);

    void onPlayerJoin(Game game, Player player);

    void onPlayerDisconnect(Game game, Player player);

    void onPlayerSubmitTile(Game game, Player player, Tile tile, int value);

    void onPlayerAddPotentialTile(Game game, Player player, Tile tile, int value);

    void onPlayerRemovePotentialTile(Game game, Player player, Tile tile, int value);

}