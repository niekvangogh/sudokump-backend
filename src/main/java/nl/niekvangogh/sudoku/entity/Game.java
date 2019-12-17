package nl.niekvangogh.sudoku.entity;

import lombok.Getter;
import nl.niekvangogh.sudoku.pojo.game.GameDetails;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;

import java.util.HashMap;
import java.util.Map;

public class Game {

    @Getter
    private GameDetails gameDetails = new GameDetails();

    @Getter
    private Map<Player, Sudoku> playerSudokuMap;

    public Game() {
        this.playerSudokuMap = new HashMap<>();
    }

}