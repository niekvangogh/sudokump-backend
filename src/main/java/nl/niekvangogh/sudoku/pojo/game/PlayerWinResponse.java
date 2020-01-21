package nl.niekvangogh.sudoku.pojo.game;

import lombok.Getter;
import nl.niekvangogh.sudoku.pojo.PublicUser;

import java.io.Serializable;

@Getter
public class PlayerWinResponse implements Serializable {

    private final String event;
    private PublicUser winner;

    public PlayerWinResponse(PublicUser winner) {
        this.event = "playerwin";
        this.winner = winner;
    }
}
