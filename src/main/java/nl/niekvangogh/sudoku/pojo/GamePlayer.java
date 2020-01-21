package nl.niekvangogh.sudoku.pojo;

import lombok.Getter;
import lombok.Setter;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;

@Getter
public class GamePlayer {

    private final User user;

    @Setter
    private Sudoku sudoku;

    @Setter
    private boolean ready;

    public GamePlayer(User user) {
        this.user = user;
    }
}
