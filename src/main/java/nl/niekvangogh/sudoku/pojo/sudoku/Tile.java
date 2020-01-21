package nl.niekvangogh.sudoku.pojo.sudoku;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Tile {

    @Getter
    private int xPos;

    @Getter
    private int yPos;

    @Getter
    @Setter
    private int solution = 0;

    @Setter
    @Getter
    private int guess = 0;

    @Getter
    private List<Integer> potentialSolutions;


    public Tile(int xPos, int yPos, int solution) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.solution = solution;

        this.potentialSolutions = new ArrayList<>();
    }

    public Tile(Tile tile) {
        this.xPos = tile.xPos;
        this.yPos = tile.yPos;
        this.solution = tile.solution;
        this.guess = tile.guess;
        this.potentialSolutions = new ArrayList<>(tile.potentialSolutions);
    }

    public int getDisplay() {
        if (this.solution != 0) {
            return this.solution;
        }
        return this.guess;
    }
}
