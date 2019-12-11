package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;
import nl.niekvangogh.sudoku.service.SudokuService;

import java.util.Arrays;
import java.util.Random;

public class SudokuServiceImpl implements SudokuService {

    @Override
    public Sudoku generateSudoku() {
        Sudoku sudoku = new Sudoku();
        this.fillDiagonal(sudoku);
        this.fillSudoku(sudoku);
        return sudoku;
    }

    @Override
    public void fillDiagonal(Sudoku sudoku) {
        for (int i = 0; i < 9; i = i + 3) {
            System.out.println(i);
            this.fillBox(sudoku, this.getTile(sudoku, i, i));
        }
    }

    @Override
    public void fillBox(Sudoku sudoku, Tile tile) {
        int num;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                do {
                    num = sudoku.getRandomGenerator(9);
                }
                while (!this.isUnusedInBox(sudoku, tile, num));

                Tile[] tiles = this.getBox(sudoku, tile.getXPos(), tile.getYPos());
                // requires refactoring
                sudoku.getGrid()[tiles[0].getXPos() + i][tiles[0].getYPos() + j].setSolution(num);
            }
        }

    }

    @Override
    public Tile[] getBox(Sudoku sudoku, int xPos, int yPos) {
        int startX = (xPos / 3) * 3;
        int startY = (yPos / 3) * 3;

        Tile[] tiles = new Tile[9];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                tiles[(3 * y) + x] = this.getTile(sudoku, startX + x, startY + y);
            }
        }
        return tiles;
    }

    @Override
    public boolean checkIfSafe(Sudoku sudoku, Tile tile, int value) {
// lots of work cba
        return true;
    }

    @Override
    public boolean isUnusedInBox(Sudoku sudoku, Tile tile, int value) {
        Tile[] box = this.getBox(sudoku, tile.getXPos(), tile.getYPos());
        return Arrays.stream(box).noneMatch(boxTile -> boxTile.getSolution() == value);
    }

    @Override
    public boolean fillTile(Sudoku sudoku, Tile tile) {
        return false;
    }

    @Override
    public boolean fillSudoku(Sudoku sudoku) {
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
        return sudoku.getGrid()[x][y];
    }
}
