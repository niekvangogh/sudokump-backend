package nl.niekvangogh.sudoku.pojo.sudoku;

import lombok.Getter;

import java.util.Random;

public class Sudoku {

    private final Random randomGenerator;

    @Getter
    private Tile[][] grid;

    public Sudoku(long seed) {
        this.randomGenerator = new Random(seed);
        this.grid = new Tile[9][9];
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                this.grid[x][y] = new Tile(x, y, 0);
            }
        }
    }

    public Sudoku() {
        this(new Random().nextLong());
    }

    public int getRandomGenerator(int num) {
        return (int) Math.floor((this.randomGenerator.nextDouble() * num + 1));
    }

}
