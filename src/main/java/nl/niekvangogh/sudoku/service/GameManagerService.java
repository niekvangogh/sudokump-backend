package nl.niekvangogh.sudoku.service;

import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.Player;
import nl.niekvangogh.sudoku.pojo.Ranking;

public interface GameManagerService {

    void queuePlayer(Player player);

    Game createGame(Ranking ranking);

    Game findGame(Player player);

    Game startGame(Game game);

    void addPlayer(Game game, Player player);

    void removePlayer(Game game, Player player);
}
