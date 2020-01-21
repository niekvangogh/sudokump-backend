package nl.niekvangogh.sudoku.pojo.game;

import lombok.Getter;

@Getter
public class UpdateTileRequest {

    private int x;
    private int y;
    private int number;
    private String method;
}
