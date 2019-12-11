package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;
import nl.niekvangogh.sudoku.service.SudokuService;

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
        for (int i = 0; i < 3; i++) {
            this.fillBox(sudoku, this.getTile(sudoku, i * 3, i * 3));
        }
    }

    @Override
    public void fillBox(Sudoku sudoku, Tile tile) {
        Tile[] tiles = this.getBox(sudoku, tile.getXPos(), tile.getYPos());
        int num;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                do {
                    num = sudoku.getRandomGenerator(9);
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
        return this.isUnusedInRow(sudoku, tile.getXPos(), value) &&
                this.isUnusedInCol(sudoku, tile.getYPos(), value) &&
                this.isUnusedInBox(sudoku, tile, value);
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
    public boolean fillTile(Sudoku sudoku, int x, int y) {
        if (y >= 9 && x < 9 - 1) {
            x = x + 1;
            y = 0;
        }

        if (x >= 9 && y >= 9) {
            return true;
        }

        if (x < 3) {
            if (y < 3)
                y = 3;
        } else if (x < 9 - 3) {
            if (y == (x / 3) * 3)
                y = y + 3;
        } else {
            if (y == 9 - 3) {
                x = x + 1;
                y = 0;
                if (x >= 9)
                    return true;
            }
        }

        Tile tile = this.getTile(sudoku, x, y);

        for (int num = 1; num <= 9; num++) {
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
        return this.fillTile(sudoku, 0, 3);
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
