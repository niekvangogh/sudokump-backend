package nl.niekvangogh.sudoku.entity;

import lombok.Getter;
import nl.niekvangogh.sudoku.pojo.GamePlayer;

import java.util.HashMap;
import java.util.Map;

public class Game {

    @Getter
    private GameDetails gameDetails = new GameDetails();

    @Getter
    private Map<User, GamePlayer> userMap;

    public Game() {
        this.userMap = new HashMap<>();
    }

}