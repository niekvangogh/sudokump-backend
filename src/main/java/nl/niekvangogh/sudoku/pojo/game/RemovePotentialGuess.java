package nl.niekvangogh.sudoku.pojo.game;


import lombok.Getter;

@Getter
public class RemovePotentialGuess {

    private int x;
    private int y;
    private int guess;
}
