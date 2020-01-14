package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.GameDetails;
import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.game.GameState;
import nl.niekvangogh.sudoku.pojo.queue.QueueUpdate;
import nl.niekvangogh.sudoku.repository.GameRepository;
import nl.niekvangogh.sudoku.service.GameManagerService;
import nl.niekvangogh.sudoku.service.SudokuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CompletableFuture;

@Service
public class GameManagerServiceImpl implements GameManagerService {

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private GameRepository gameRepository;

    private final Map<User, String> queued = new HashMap<>();
    private List<Game> games = new ArrayList<>();

    @Override
    public void queuePlayer(User player, String sessionId) {
        this.queued.put(player, sessionId);
        this.processQueue(player);
    }

    private void processQueue(User user) {
        new Thread(() -> {
            Game game = this.findGame(user);
            this.addPlayer(game, user);
        }).start();
    }

    @Override
    public void cancelQueue(User user) {
        this.queued.remove(user);
    }

    @Override
    public Game createGame(Ranking ranking) {
        GameDetails gameDetails = new GameDetails();
        gameDetails.setRanking(ranking);
        gameDetails.setSeed((long) (Math.random() * 1000));
        gameDetails = this.gameRepository.save(gameDetails);
        Game game = new Game(gameDetails);
        this.games.add(game);
        return game;
    }

    @Override
    public Game findGame(User user) {
        return this.games.stream().filter(game -> game.getGameDetails().getGameState().equals(GameState.NOT_STARTED)).findFirst().orElse(this.createGame(Ranking.BRONZE));
    }

    @Override
    public void startGame(Game game) {
        game.getGameDetails().setGameState(GameState.STARTED);
        this.gameRepository.save(game.getGameDetails());
        this.gameService.onGameStart(game);
    }

    @Override
    public Game getGame(User user) {
        return this.games.stream().filter(game -> game.getUserMap().keySet().stream().anyMatch(userId -> userId == user.getId())).findFirst().orElse(null);
    }

    @Override
    public void addPlayer(Game game, User user) {
        this.gameService.onPlayerJoin(game, user, this.queued.get(user));
        this.queued.remove(user);
    }

    @Override
    public void removePlayer(Game game, User user) {
        game.getGameDetails().getUsers().remove(user);

        this.gameService.onPlayerDisconnect(game, user);
    }
}
