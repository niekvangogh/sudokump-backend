package nl.niekvangogh.sudoku.service;

import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.Player;
import nl.niekvangogh.sudoku.pojo.Ranking;

public interface GameManagerService {

    /**
     * Queues the player for a game, this process might take a while because it wants to find the best match for all the players
     *
     * @param player the player which is going to be queued
     */
    void queuePlayer(Player player);

    /**
     * this method creates a game, including the sudoku
     *
     * @param ranking the difficulty of the sudoku
     * @return the newly created game
     */
    Game createGame(Ranking ranking);

    /**
     * This method will be used while looping in queuePlayer, it searches to all unstarted games and looks for the best fit with the same level of players
     *
     * @param player the player which is queuing for a game
     * @return returns a game if it has been found, it will *never* return null
     */
    Game findGame(Player player);

    /**
     * Starts the game
     *
     * @param game the game which is going to be started
     */
    void startGame(Game game);

    /**
     * @param player the player which is going to be looked for in games
     * @return the game where the player is in, null if it couldn't find the player
     */
    Game getGame(Player player);

    /**
     * Adds a player to the game, this is the last step of the process of finding a game
     *
     * @param game   the game the player is going to be added to
     * @param player the player which is going to be added
     */
    void addPlayer(Game game, Player player);

    /**
     * Forces a player to be removed from a game
     *
     * @param game   the game the player can be in
     * @param player the player which is going to be removed
     */
    void removePlayer(Game game, Player player);
}
