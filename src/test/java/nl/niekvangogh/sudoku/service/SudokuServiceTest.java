package nl.niekvangogh.sudoku.service;

import nl.niekvangogh.sudoku.pojo.sudoku.Box;
import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;
import nl.niekvangogh.sudoku.service.impl.SudokuServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SudokuServiceTest {

    private final SudokuServiceImpl sudokuService;

    public SudokuServiceTest() {
        this.sudokuService = new SudokuServiceImpl();
    }

    @Test
    void generateSudoku__ShouldGenerateCompletedSudoku() {
        Sudoku sudoku = this.sudokuService.generateSudoku();
        for (Tile[] tiles : sudoku.getGrid()) {
            for (Tile tile : tiles) {
                assertEquals(0, tile.getSolution());
            }
        }
    }

    @Test
    void fillDiagonal_GivenEmptySudoku_ShouldFillDiagonalsWithNumbers() {
        Sudoku sudoku = new Sudoku();
        this.sudokuService.fillDiagonal(sudoku);

        for (int i = 0; i < 3; i++) {
            int x = (i + 1) * 3;
            int y = (i + 1) * 3;
            Tile[] box = this.sudokuService.getBox(sudoku, x, y);
            for (Tile tile : box) {
                assertTrue(tile.getSolution() != 0);
            }
        }
    }

    @Test
    void fillDiagonal_GivenEmptySudoku_ShouldFillWithSafeNumbers() {
        Sudoku sudoku = new Sudoku();
        this.sudokuService.fillDiagonal(sudoku);

        for (int i = 0; i < 3; i++) {
            int x = (i + 1) * 3;
            int y = (i + 1) * 3;
            Tile[] box = this.sudokuService.getBox(sudoku, x, y);
            for (Tile tile : box) {
                assertTrue(this.sudokuService.checkIfSafe(sudoku, tile, tile.getSolution()));
            }
        }
    }

    @Test
    void fillBox_givenEmptySudoku_ShouldFillWithSafeNumbers() {
        Sudoku sudoku = new Sudoku();
        Tile tile = this.sudokuService.getTile(sudoku, 2, 2);
        this.sudokuService.fillBox(sudoku, tile);

        Tile[] box = this.sudokuService.getBox(sudoku, tile.getXPos(), tile.getYPos());

        for (int i = 0; i < 9; i++) {
            Tile boxTile = box[0];
            assertTrue(this.sudokuService.checkIfSafe(sudoku, boxTile, boxTile.getSolution()));
        }
    }

}