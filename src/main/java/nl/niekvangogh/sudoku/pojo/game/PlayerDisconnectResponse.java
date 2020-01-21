package nl.niekvangogh.sudoku.pojo.game;

import lombok.Getter;
import nl.niekvangogh.sudoku.pojo.PublicUser;

import java.io.Serializable;

@Getter
public class PlayerDisconnectResponse implements Serializable {

    private PublicUser disconnectedUser;
    private final String event;

    public PlayerDisconnectResponse(PublicUser disconnectedUser) {
        this.event = "playerdisconnect";
        this.disconnectedUser = disconnectedUser;
    }
}
