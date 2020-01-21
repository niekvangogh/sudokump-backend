package nl.niekvangogh.sudoku.pojo.game;

import nl.niekvangogh.sudoku.pojo.PublicUser;

public class PlayerDisconnectResponse extends GameUpdateResponse {

    private PublicUser disconnectedUser;

    public PlayerDisconnectResponse(PublicUser disconnectedUser) {
        super("playerdisconnect");
        this.disconnectedUser = disconnectedUser;
    }
}
