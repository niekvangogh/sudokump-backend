package nl.niekvangogh.sudoku.pojo.sudoku;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Tile {

    @Getter
    private int xPos;

    @Getter
    private int yPos;

    public Tile(int xPos, int yPos, int solution) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.solution = solution;

        this.potentialSolutions = new ArrayList<>();
    }

    @Getter
    private int solution;

    @Getter
    private int guess;

    @Getter
    private List<Integer> potentialSolutions;
}
