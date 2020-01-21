package nl.niekvangogh.sudoku.pojo.game;

import nl.niekvangogh.sudoku.pojo.PublicUser;

public class PlayerWinResponse extends GameUpdateResponse {

    private PublicUser winner;

    public PlayerWinResponse(PublicUser winner) {
        super("playerwin");
        this.winner = winner;
    }
}
