package nl.niekvangogh.sudoku.entity;

import lombok.Getter;
import nl.niekvangogh.sudoku.pojo.GamePlayer;

import java.util.HashMap;
import java.util.Map;

public class Game {

    @Getter
    private final GameDetails gameDetails;

    @Getter
    private Map<User, GamePlayer> userMap;

    public Game(GameDetails gameDetails) {
        this.userMap = new HashMap<>();
        this.gameDetails = gameDetails;
    }

}