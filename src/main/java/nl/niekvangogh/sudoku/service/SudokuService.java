package nl.niekvangogh.sudoku.service;

import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.pojo.sudoku.Box;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;

public interface SudokuService {

    Sudoku createSudoku(Ranking ranking);

    Box getBox(Sudoku sudoku, Tile tile);

    Box getBox(Sudoku sudoku, int x, int y);

}
