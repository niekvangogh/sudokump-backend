package nl.niekvangogh.sudoku;

import nl.niekvangogh.sudoku.pojo.sudoku.Tile;

public class SudokUtils {

    public static void printSudoku(Tile[][] grid) {
        for (Tile[] tiles : grid) {
            for (Tile tile : tiles) {
                System.out.print(tile.getSolution() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
