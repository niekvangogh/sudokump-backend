package nl.niekvangogh.sudoku.pojo.game;

import lombok.Getter;

@Getter
public class SubmitPotentialGuess {

    private int x;
    private int y;
    private int guess;
}
