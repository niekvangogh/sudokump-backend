package nl.niekvangogh.sudoku.pojo.sudoku;

import lombok.Getter;

import java.util.Random;

public class Sudoku {

    private final Random randomGenerator;

    @Getter
    private final int size;

    @Getter
    private final int sqrt;

    @Getter
    private Tile[][] grid;

    public Sudoku(long seed, int size) {
        this.randomGenerator = new Random(seed);
        this.grid = new Tile[size][size];

        this.size = size;
        this.sqrt = (int) Math.sqrt(size);

        for (int x = 0; x < this.getSize(); x++) {
            for (int y = 0; y < this.getSize(); y++) {
                this.grid[x][y] = new Tile(x, y, 0);
            }
        }
    }

    public Sudoku() {
        this(new Random().nextLong(), 9);
    }

    public Sudoku(int size) {
        this(new Random().nextLong(), size);
    }

    public int getRandomGenerator(int num) {
        return (int) Math.floor((this.randomGenerator.nextDouble() * num + 1));
    }

    public int[][] toPlayerGrid() {
        int[][] emptyGrid = new int[size][size];

        for (int x = 0; x < this.getSize(); x++) {
            for (int y = 0; y < this.getSize(); y++) {
                emptyGrid[x][y] = this.grid[x][y].getSolution();
            }
        }

        return emptyGrid;
    }

}
