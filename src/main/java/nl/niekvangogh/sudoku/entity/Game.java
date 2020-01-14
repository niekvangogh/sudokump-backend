package nl.niekvangogh.sudoku.entity;

import lombok.Getter;
import nl.niekvangogh.sudoku.pojo.GamePlayer;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

public class Game {

    @Getter
    private final GameDetails gameDetails;

    @Getter
    private Map<User, GamePlayer> userMap;

    @Getter
    private Sudoku sudoku;

    public Game(GameDetails gameDetails) {
        this.userMap = new HashMap<>();
        this.gameDetails = gameDetails;
        this.sudoku = new Sudoku(this.gameDetails.getSeed(), 9);
    }

}