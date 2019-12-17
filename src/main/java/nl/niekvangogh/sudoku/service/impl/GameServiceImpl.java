package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.Player;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;
import nl.niekvangogh.sudoku.service.GameService;

public class GameServiceImpl implements GameService {

    @Override
    public void onGameEnd(Game game) {

    }

    @Override
    public void onGameStart(Game game) {
        Sudoku sudoku = new Sudoku(game.getGameDetails().getSeed(), 9);
        for (Player player : game.getGameDetails().getPlayers()) {
            game.getPlayerSudokuMap().put(player, sudoku);
        }

    }

    @Override
    public void onPlayerJoin(Game game, Player player) {

    }

    @Override
    public void onPlayerDisconnect(Game game, Player player) {

    }

    @Override
    public void onPlayerSubmitTile(Game game, Player player, Tile tile, int value) {

    }

    @Override
    public void onPlayerAddPotentialTile(Game game, Player player, Tile tile, int value) {

    }

    @Override
    public void onPlayerRemovePotentialTile(Game game, Player player, Tile tile, int value) {

    }
}
