package nl.niekvangogh.sudoku.service;

import nl.niekvangogh.sudoku.pojo.sudoku.Sudoku;
import nl.niekvangogh.sudoku.pojo.sudoku.Tile;
import nl.niekvangogh.sudoku.service.impl.SudokuServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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

    @Test
    void getBox_givenCorrectCoordinates_ShouldReturnBox() {
        Sudoku sudoku = new Sudoku();

        int xPos = 4;
        int yPos = 6;

        Tile[] box = this.sudokuService.getBox(sudoku, xPos, yPos);

        int rowNr = yPos / 3;
        int colNr = xPos / 3;

        int startY = rowNr * 3;
        int startX = colNr * 3;

        long tileAmount = Arrays.stream(box).filter(tile -> {
            for (int x = startX; x < startX + 3; x++) {
                for (int y = startY; y < startY + 3; y++) {
                    if (tile.getXPos() == x && tile.getYPos() == y) {
                        return true;
                    }
                }
            }
            return false;
        }).count();

        assertEquals(9, tileAmount);
    }

    @Test
    void checkIfSafe_GivenUnSafeLocation_ShouldReturnFalse() {
        Sudoku sudoku = new Sudoku();
        this.sudokuService.fillDiagonal(sudoku);
        Tile filledTile = sudoku.getGrid()[3][0];
        Tile newTile = sudoku.getGrid()[4][0];

        assertFalse(this.sudokuService.checkIfSafe(sudoku, newTile, filledTile.getSolution()));
    }

    @Test
    void checkIfSafe_GivenSafeLocation_ShouldReturnTrue() {

    }

    @Test
    void isUnusedInBox_GivenEmptyBox_ShouldReturnTrue() {
        Sudoku sudoku = new Sudoku();
        Tile tile = sudoku.getGrid()[0][0];

        assertTrue(this.sudokuService.isUnusedInBox(sudoku, tile, 3));
    }

    @Test
    void isUnusedInBox_GivenUnUsedNumberAndAlmostFilledBox_ShouldReturnTrue() {
        Sudoku sudoku = new Sudoku();
        Tile[] box = this.sudokuService.getBox(sudoku, 0, 0);
        for (int i = 1; i < box.length; i++) {
            Tile tile = box[i];
            tile.setSolution(i);
        }

        assertTrue(this.sudokuService.isUnusedInBox(sudoku, box[0], 9));
    }

}