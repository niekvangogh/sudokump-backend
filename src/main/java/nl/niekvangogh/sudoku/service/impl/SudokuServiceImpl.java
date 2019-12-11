package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;
import nl.niekvangogh.sudoku.service.SudokuService;

public class SudokuServiceImpl implements SudokuService {

    @Override
    public Sudoku generateSudoku() {
        return null;
    }

    @Override
    public void fillDiagonal(Sudoku sudoku) {

    }

    @Override
    public void fillBox(Sudoku sudoku, Tile tile) {

    }

    @Override
    public Tile[] getBox(Sudoku sudoku, int x, int y) {
        return new Tile[0];
    }

    @Override
    public boolean checkIfSafe(Sudoku sudoku, Tile tile, int value) {
        return false;
    }

    @Override
    public boolean isUnusedInBox(Sudoku sudoku, Tile tile, int value) {
        return false;
    }

    @Override
    public boolean fillTile(Sudoku sudoku, Tile tile) {
        return false;
    }

    @Override
    public void createPuzzle(Sudoku sudoku, Ranking ranking) {

    }

    @Override
    public void createPuzzle(Sudoku sudoku, int amount) {

    }

    @Override
    public Tile getTile(Sudoku sudoku, int x, int y) {
        return null;
    }
}
