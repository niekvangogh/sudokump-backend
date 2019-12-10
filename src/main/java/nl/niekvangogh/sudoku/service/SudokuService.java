package nl.niekvangogh.sudoku.service;

import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.pojo.sudoku.Box;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;

public interface SudokuService {

    /**
     * This method generates a filled Sudoku
     *
     * @return a solved Sudoku
     */
    Sudoku generateSudoku();

    /**
     * Fills the diagonal of a sudoku, with random numbers which makes it solvable for the algorithm
     *
     * @param sudoku the Sudoku which needs to be filled
     */
    void fillDiagonal(Sudoku sudoku);


    /**
     * Fills the entire box in a Sudoku
     *
     * @param sudoku The Sudoku which needs a box to be filled
     * @param tile   A tile indicates the box which is going to be filled
     */
    void fillBox(Sudoku sudoku, Tile tile);


    /**
     * Checks if the tile provided can be filled with a number.
     *
     * @param sudoku The Sudoku
     * @param tile   The tile which is going to be checked in the Sudoku
     * @param value  The value which is going to be filled in the tile
     */
    void checkIfSafe(Sudoku sudoku, Tile tile, int value);

    /**
     * Checks if the value is already somewhere in the box
     *
     * @param sudoku The Sudoku
     * @param tile   A tile which indicates what box needs checking
     * @param value  The value which is going to be checked
     * @return a boolean indicating if it exists or not
     */
    boolean isUnusedInBox(Sudoku sudoku, Tile tile, int value);

    /**
     * Recursive function which fills the remaining tiles in the Sudoku
     *
     * @param sudoku The Sudoku which is going to be filled
     * @param tile   The tile which is going to be filled
     * @return a boolean, if true, it has been finished, if false, it hasn't
     */
    boolean fillRemainingTiles(Sudoku sudoku, Tile tile);

    /**
     * Creates the actual puzzle, removing N amount of tiles depending on the ranking
     *
     * @param sudoku  The Sudoku which is going to change
     * @param ranking The ranking/level of the puzzle
     */
    void createPuzzle(Sudoku sudoku, Ranking ranking);

    /**
     * Creates the actual puzzle, removing N amount of tiles depending on the amount of amount
     *
     * @param sudoku The Sudoku which is going to change
     * @param amount The amount of tiles which are going to be removed.
     */
    void createPuzzle(Sudoku sudoku, int amount);

}
