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

    @Autowired
    private SudokuServiceImpl sudokuService;

    private final Map<User, CompletableFuture<QueueUpdate>> queued = new HashMap<>();
    private List<Game> games = new ArrayList<>();

    @Override
    public CompletableFuture<QueueUpdate> queuePlayer(User player) {
        CompletableFuture<QueueUpdate> callback = new CompletableFuture<>();
        this.queued.put(player, callback);
        this.processQueue(player);
        return callback;
    }

    private void processQueue(User user) {
        new Thread(() -> {
            Game game = this.findGame(user);
            this.addPlayer(game, user);
            if (game.getGameDetails().getUsers().size() == 1) {
                this.startGame(game);
            }
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
        return this.games.stream().filter(game -> game.getGameDetails().getUsers().stream().anyMatch(gameUser -> gameUser.getId() == user.getId())).findFirst().orElse(null);
    }

    @Override
    public void addPlayer(Game game, User user) {
        this.queued.get(user).complete(new QueueUpdate(game.getGameDetails().getId()));
        this.queued.remove(user);
        game.getGameDetails().getUsers().add(user);

        this.gameService.onPlayerJoin(game, user);
    }

    @Override
    public void removePlayer(Game game, User user) {
        game.getGameDetails().getUsers().remove(user);

        this.gameService.onPlayerDisconnect(game, user);
    }
}
