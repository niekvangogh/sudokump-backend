package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.Player;
import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.pojo.game.GameState;
import nl.niekvangogh.sudoku.service.GameManagerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameManagerServiceImpl implements GameManagerService {

    private List<Player> queued = new ArrayList<>();
    private List<Game> games = new ArrayList<>();

    @Override
    public void queuePlayer(Player player) {
        this.queued.add(player);
    }

    @Override
    public Game createGame(Ranking ranking) {
        Game game = new Game();
        game.getGameDetails().setRanking(ranking);
        game.getGameDetails().setSeed((long) (Math.random() * 1000));
        this.games.add(game);
        return game;
    }

    @Override
    public Game findGame(Player player) {
        // im going to make this so basic for now lolololol
        return this.games.stream().filter(game -> game.getGameDetails().getRanking().isIn(player.rating)).findFirst().orElse(null);
    }

    @Override
    public void startGame(Game game) {
        game.getGameDetails().setGameState(GameState.STARTED);
    }

    @Override
    public Game getGame(Player player) {
        return this.games.stream().filter(game -> game.getGameDetails().getPlayers().contains(player)).findFirst().orElse(null);
    }

    @Override
    public void addPlayer(Game game, Player player) {
        game.getGameDetails().getPlayers().add(player);
    }

    @Override
    public void removePlayer(Game game, Player player) {
        game.getGameDetails().getPlayers().remove(player);
    }
}
