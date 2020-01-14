package nl.niekvangogh.sudoku.entity;

import lombok.Getter;
import lombok.Setter;
import nl.niekvangogh.sudoku.pojo.GamePlayer;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

public class Game {

    @Getter
    private final GameDetails gameDetails;

    @Getter
    private Map<Long, GamePlayer> userMap;

    @Getter
    @Setter
    private Sudoku defaultSudoku;

    public Game(GameDetails gameDetails) {
        this.userMap = new HashMap<>();
        this.gameDetails = gameDetails;
    }

    public GamePlayer getGamePlayer(long userId) {
        return this.userMap.get(userId);
    }

}