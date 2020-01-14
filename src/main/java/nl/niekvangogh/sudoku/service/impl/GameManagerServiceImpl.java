package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.entity.Game;
import nl.niekvangogh.sudoku.entity.GameDetails;
import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.game.GameState;
import nl.niekvangogh.sudoku.pojo.queue.QueueUpdate;
import nl.niekvangogh.sudoku.repository.GameRepository;
import nl.niekvangogh.sudoku.service.GameManagerService;
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

    private final Map<User, CompletableFuture<QueueUpdate>> queued = new HashMap<>();
    private List<Game> games = new ArrayList<>();

    @Override
    public CompletableFuture<QueueUpdate> queuePlayer(User player) {
        CompletableFuture<QueueUpdate> callback = new CompletableFuture<>();
        this.queued.put(player, callback);
        this.processQueue();
        return callback;
    }

    private void processQueue() {
        new Thread(() -> {
            synchronized (this.queued) {
                queued.forEach((user, future) -> {
                    Game game = this.findGame(user);
                    if (game == null) {
                        game = this.createGame(Ranking.BRONZE);
                    }
                    this.addPlayer(game, user);
                    future.complete(new QueueUpdate(game.getGameDetails().getId()));

                });
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
//        return this.games.stream().filter(game -> game.getGameDetails().getRanking().isIn(user.getRating())).findFirst().orElse(null);
        return this.games.stream().filter(game -> true).findFirst().orElse(null);
    }

    @Override
    public void startGame(Game game) {
        game.getGameDetails().setGameState(GameState.STARTED);
        this.gameService.onGameStart(game);
    }

    @Override
    public Game getGame(User user) {
        return this.games.stream().filter(game -> game.getGameDetails().getUsers().contains(user)).findFirst().orElse(null);
    }

    @Override
    public void addPlayer(Game game, User user) {
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
