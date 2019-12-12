package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;
import nl.niekvangogh.sudoku.service.SudokuService;

public class SudokuServiceImpl implements SudokuService {

    @Override
    public Sudoku generateSudoku(int size) {
        Sudoku sudoku = new Sudoku(size);
        this.fillDiagonal(sudoku);
        this.fillSudoku(sudoku);
        return sudoku;
    }

    public Sudoku generateSudoku() {
        return this.generateSudoku(9);
    }

    @Override
    public void fillDiagonal(Sudoku sudoku) {
        for (int i = 0; i < sudoku.getSqrt(); i++) {
            this.fillBox(sudoku, this.getTile(sudoku, i * sudoku.getSqrt(), i * sudoku.getSqrt()));
        }
    }

    @Override
    public void fillBox(Sudoku sudoku, Tile tile) {
        Tile[] tiles = this.getBox(sudoku, tile.getXPos(), tile.getYPos());
        int num;
        for (int i = 0; i < sudoku.getSqrt(); i++) {
            for (int j = 0; j < sudoku.getSqrt(); j++) {
                do {
                    num = sudoku.getRandomGenerator(sudoku.getSize());
                }
                while (!this.isUnusedInBox(sudoku, tile, num));

                int x = tiles[0].getXPos() + i;
                int y = tiles[0].getYPos() + j;

                sudoku.getGrid()[x][y].setSolution(num);
            }
        }

    }

    @Override
    public Tile[] getBox(Sudoku sudoku, int xPos, int yPos) {
        Tile[] tiles = new Tile[sudoku.getSize()];
        for (int boxX = 0; boxX < sudoku.getSqrt(); boxX++) {
            for (int boxY = 0; boxY < sudoku.getSqrt(); boxY++) {
                Tile tile = sudoku.getGrid()[(xPos / sudoku.getSqrt() * sudoku.getSqrt()) + boxX][(yPos / sudoku.getSqrt() * sudoku.getSqrt()) + boxY];
                tiles[(boxX * sudoku.getSqrt()) + boxY] = tile;
            }
        }
        return tiles;
    }

    @Override
    public boolean checkIfSafe(Sudoku sudoku, Tile tile, int value) {
        return this.isUnusedInRow(sudoku, tile.getXPos(), value) &&
                this.isUnusedInCol(sudoku, tile.getYPos(), value) &&
                this.isUnusedInBox(sudoku, tile, value);
    }

    @Override
    public boolean isUnusedInBox(Sudoku sudoku, Tile tile, int value) {
        for (int i = 0; i < sudoku.getSqrt(); i++) {
            for (int j = 0; j < sudoku.getSqrt(); j++) {
                if (sudoku.getGrid()[(tile.getXPos() / sudoku.getSqrt() * sudoku.getSqrt()) + i][(tile.getYPos() / sudoku.getSqrt() * sudoku.getSqrt()) + j].getSolution() == value) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isUnusedInCol(Sudoku sudoku, int yPos, int value) {
        for (int i = 0; i < sudoku.getSize(); i++) {
            if (sudoku.getGrid()[i][yPos].getSolution() == value) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isUnusedInRow(Sudoku sudoku, int xPos, int value) {
        for (int j = 0; j < sudoku.getSize(); j++) {
            if (sudoku.getGrid()[xPos][j].getSolution() == value) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean fillTile(Sudoku sudoku, int x, int y) {
        if (y >= sudoku.getSize() && x < sudoku.getSize() - 1) {
            x = x + 1;
            y = 0;
        }

        if (x >= sudoku.getSize() && y >= sudoku.getSize()) {
            return true;
        }

        if (x < sudoku.getSqrt()) {
            if (y < sudoku.getSqrt())
                y = sudoku.getSqrt();
        } else if (x < sudoku.getSize() - sudoku.getSqrt()) {
            if (y == (x / sudoku.getSqrt()) * sudoku.getSqrt())
                y = y + sudoku.getSqrt();
        } else {
            if (y == sudoku.getSize() - sudoku.getSqrt()) {
                x = x + 1;
                y = 0;
                if (x >= sudoku.getSize())
                    return true;
            }
        }

        Tile tile = this.getTile(sudoku, x, y);

        for (int num = 1; num <= sudoku.getSize(); num++) {
            if (this.checkIfSafe(sudoku, tile, num)) {
                tile.setSolution(num);
                if (this.fillTile(sudoku, x, y + 1)) {
                    return true;
                }
                tile.setSolution(0);
            }
        }
        return false;
    }

    @Override
    public boolean fillSudoku(Sudoku sudoku) {
        return this.fillTile(sudoku, 0, sudoku.getSqrt());
    }

    @Override
    public void createPuzzle(Sudoku sudoku, Ranking ranking) {

    }

    @Override
    public void createPuzzle(Sudoku sudoku, int count) {
        while (count != 0) {
            int cellId = sudoku.getRandomGenerator(sudoku.getSize() * sudoku.getSize());

            int i = (cellId / sudoku.getSize());
            int j = cellId % 9;
            if (j != 0) {
                j = j - 1;
            }

            if (sudoku.getGrid()[i][j].getSolution() != 0) {
                count--;
                sudoku.getGrid()[i][j].setSolution(0);
            }
        }
    }

    @Override
    public Tile getTile(Sudoku sudoku, int x, int y) {
        return sudoku.getGrid()[x][y];
    }
}
