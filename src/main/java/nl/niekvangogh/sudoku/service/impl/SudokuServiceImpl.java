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
        Tile[] tiles = new Tile[9];
        for (int boxX = 0; boxX < 3; boxX++) {
            for (int boxY = 0; boxY < 3; boxY++) {
                Tile tile = sudoku.getGrid()[(xPos / 3 * 3) + boxX][(yPos / 3 * 3) + boxY];
                tiles[(boxX * 3) + boxY] = tile;
            }
        }
        return tiles;
    }

    @Override
    public boolean checkIfSafe(Sudoku sudoku, Tile tile, int value) {
        return (this.isUnusedInRow(sudoku, tile.getXPos(), value) &&
                this.isUnusedInCol(sudoku, tile.getYPos(), value) &&
                this.isUnusedInBox(sudoku, tile, value));
    }

    @Override
    public boolean isUnusedInBox(Sudoku sudoku, Tile tile, int value) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudoku.getGrid()[(tile.getXPos() / 3 * 3) + i][(tile.getYPos() / 3 * 3) + j].getSolution() == value) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isUnusedInCol(Sudoku sudoku, int yPos, int value) {
        for (int i = 0; i < 9; i++) {
            if (sudoku.getGrid()[i][yPos].getSolution() == value) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isUnusedInRow(Sudoku sudoku, int xPos, int value) {
        for (int j = 0; j < 9; j++) {
            if (sudoku.getGrid()[xPos][j].getSolution() == value) {
                return false;
            }
        }

        return true;
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
