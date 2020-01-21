package nl.niekvangogh.sudoku.pojo;

import lombok.Getter;
import lombok.Setter;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;

@Getter
@Setter
public class GamePlayer {

    private final User user;

    private Sudoku sudoku;

    private boolean ready;

    public GamePlayer(User user) {
        this.user = user;
    }

    public void setSudoku(Sudoku sudoku) {
        this.sudoku = (Sudoku) sudoku.clone();
    }
}
