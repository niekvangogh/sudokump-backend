package nl.niekvangogh.sudoku.pojo;

import lombok.AllArgsConstructor;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;

@AllArgsConstructor
public class GamePlayer {

    private final Sudoku sudoku;
    private final String connectionId;

}
