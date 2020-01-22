package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.pojo.Game;
import nl.niekvangogh.sudoku.entity.GameDetails;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.pojo.game.GameState;
import nl.niekvangogh.sudoku.repository.GameRepository;
import nl.niekvangogh.sudoku.service.IGameManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class GameManagerService implements IGameManagerService {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    private final ArrayList<User> queued = new ArrayList<>();
    private List<Game> games = new ArrayList<>();

    @Override
    public void queuePlayer(User player) {
        this.queued.add(player);

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
        gameDetails.setSeed((long) (Math.random() * 100000));
        gameDetails = this.gameRepository.save(gameDetails);
        Game game = new Game(gameDetails);
        this.games.add(game);
        return game;
    }

    @Override
    public Game findGame(User user) {
        //todo persist
        return this.games.stream().filter(game -> game.getGameDetails().getGameState().equals(GameState.NOT_STARTED)).findFirst().orElse(this.createGame(Ranking.BRONZE));
    }

    @Override
    public Game findGameById(long id) {
        //todo use persistance
        return this.games.stream().filter(game -> game.getGameDetails().getId() == id).findFirst().orElse(this.createGame(Ranking.BRONZE));
    }

    @Override
    public void startGame(Game game) {
        game.getGameDetails().setGameState(GameState.STARTED);
        this.gameService.onGameStart(game);
        this.gameRepository.save(game.getGameDetails());
    }

    @Override
    public Game findGameByUser(User user) {
        return this.games.stream().filter(game -> game.getUserMap().keySet().stream().anyMatch(userId -> userId == user.getId())).findFirst().orElse(null);
    }

    @Override
    public void addPlayer(Game game, User user) {
        if (game.getGamePlayer(user.getId()) == null) {
            this.gameService.onPlayerJoin(game, user);
        }
        this.queued.remove(user);
    }

    @Override
    public void removePlayer(Game game, User user) {
        game.getGameDetails().getUsers().remove(user);

        this.gameService.onPlayerDisconnect(game, user);
    }

    @Override
    public void removePlayerFromAllGames(User user) {
        this.games.stream().filter(game -> game.getGamePlayer(user.getId()) != null).forEach(game -> this.gameService.onPlayerDisconnect(game, user));
    }

    @Override
    public boolean isInGame(User user) {
        return this.games.stream().anyMatch(game -> game.getUserMap().containsKey(user.getId()));
    }
}
