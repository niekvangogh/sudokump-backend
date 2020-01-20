package nl.niekvangogh.sudoku.pojo;

import lombok.Getter;
import lombok.Setter;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;

@Getter
@Setter
public class GamePlayer {

    private final User user;

    private String sessionId;

    private Sudoku sudoku;

    private boolean ready;

    public GamePlayer(String sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
    }
}
