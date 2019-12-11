package nl.niekvangogh.sudoku.pojo.sudoku;

import lombok.Getter;

public class Sudoku {

    @Getter
    private Tile[][] grid;

    public Sudoku() {
        this.grid = new Tile[9][9];
    }
}
