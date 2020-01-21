package nl.niekvangogh.sudoku.pojo.sudoku;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

public class Sudoku {

    private final Random randomGenerator;

    @Getter
    private final int size;

    @Getter
    private final int sqrt;

    @Getter
    private final long seed;

    @Setter
    @Getter
    private Tile[][] grid;

    public Sudoku(long seed, int size) {
        this.seed = seed;
        this.randomGenerator = new Random(seed);

        this.size = size;
        this.sqrt = (int) Math.sqrt(size);

        this.grid = new Tile[size][size];
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

    public Sudoku(Sudoku sudoku) {
        this.seed = sudoku.seed;
        this.randomGenerator = new Random(sudoku.seed);
        this.size = sudoku.getSize();
        this.sqrt = sudoku.getSqrt();
        this.grid = new Tile[this.size][this.size];
        for (int x = 0; x < sudoku.grid.length; x++) {
            Tile[] collection = sudoku.grid[x];
            this.grid[x] = new Tile[this.size];

            for (int y = 0; y < collection.length; y++) {
                Tile tile = collection[y];
                this.grid[x][y] = new Tile(tile);
            }
        }
    }

    public int getRandomGenerator(int num) {
        return (int) Math.floor((this.randomGenerator.nextDouble() * num + 1));
    }

    public Tile[][] toPlayerGrid() {
        Tile[][] emptyGrid = new Tile[size][size];

        for (int x = 0; x < this.getSize(); x++) {
            if (this.getSize() >= 0) {
                System.arraycopy(this.grid[x], 0, emptyGrid[x], 0, this.getSize());
            }
        }

        return emptyGrid;
    }
}
