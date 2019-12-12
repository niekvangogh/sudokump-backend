package nl.niekvangogh.sudoku.service;

import nl.niekvangogh.sudoku.SudokUtils;
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
    void canGenerateSudokuOf81By81() {
        Sudoku sudoku = this.sudokuService.generateSudoku(16);
        this.sudokuService.createPuzzle(sudoku, 10);
        SudokUtils.printSudoku(sudoku.getGrid());
    }

    @Test
    void generateSudoku__ShouldGenerateCompletedSudoku() {
        Sudoku sudoku = this.sudokuService.generateSudoku();
        for (Tile[] tiles : sudoku.getGrid()) {
            for (Tile tile : tiles) {
                assertNotEquals(0, tile.getSolution());
            }
        }
    }

    @Test
    void fillDiagonal_GivenEmptySudoku_ShouldFillDiagonalsWithNumbers() {
        Sudoku sudoku = new Sudoku();
        this.sudokuService.fillDiagonal(sudoku);

        for (int i = 0; i < sudoku.getSqrt(); i++) {
            int x = i * sudoku.getSqrt();
            int y = i * sudoku.getSqrt();
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
        SudokUtils.printSudoku(sudoku.getGrid());

        for (int i = 0; i < sudoku.getSqrt(); i++) {
            Tile[] box = this.sudokuService.getBox(sudoku, i * sudoku.getSqrt(), i * sudoku.getSqrt());
            for (Tile tile : box) {
                int current = tile.getSolution();
                tile.setSolution(0);

                assertTrue(this.sudokuService.checkIfSafe(sudoku, tile, current));

                tile.setSolution(current);
            }
        }
    }

    @Test
    void getTile_GivenCorrectCoordinates_ShouldGetTile() {
        Sudoku sudoku = new Sudoku();
        Tile tile = this.sudokuService.getTile(sudoku, 0, 0);

        assertEquals(sudoku.getGrid()[0][0], tile);
    }

    @Test
    void fillBox_givenEmptySudoku_ShouldFillWithSafeNumbers() {
        Sudoku sudoku = new Sudoku();
        Tile tile = this.sudokuService.getTile(sudoku, 2, 2);
        this.sudokuService.fillBox(sudoku, tile);

        Tile[] box = this.sudokuService.getBox(sudoku, tile.getXPos(), tile.getYPos());

        for (int i = 0; i < sudoku.getSize(); i++) {
            Tile boxTile = box[0];
            assertFalse(this.sudokuService.isUnusedInBox(sudoku, boxTile, boxTile.getSolution()));
            assertFalse(this.sudokuService.isUnusedInCol(sudoku, boxTile.getYPos(), boxTile.getSolution()));
            assertFalse(this.sudokuService.isUnusedInRow(sudoku, boxTile.getXPos(), boxTile.getSolution()));

        }
    }

    @Test
    void getBox_givenCorrectCoordinates_ShouldReturnBox() {
        Sudoku sudoku = new Sudoku();

        int xPos = 4;
        int yPos = 6;

        Tile[] box = this.sudokuService.getBox(sudoku, xPos, yPos);

        int startY = (yPos / sudoku.getSqrt()) * sudoku.getSqrt();
        int startX = (xPos / sudoku.getSqrt()) * sudoku.getSqrt();

        long tileAmount = Arrays.stream(box).filter(tile -> {
            for (int x = startX; x < startX + sudoku.getSqrt(); x++) {
                for (int y = startY; y < startY + sudoku.getSqrt(); y++) {
                    if (tile.getXPos() == x && tile.getYPos() == y) {
                        return true;
                    }
                }
            }
            return false;
        }).count();

        assertEquals(sudoku.getSize(), tileAmount);
    }

    @Test
    void checkIfSafe_GivenUnSafeLocation_ShouldReturnFalse() {
        Sudoku sudoku = new Sudoku();
        this.sudokuService.fillDiagonal(sudoku);
        Tile filledTile = sudoku.getGrid()[sudoku.getSqrt()][0];
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

        assertTrue(this.sudokuService.isUnusedInBox(sudoku, tile, sudoku.getSqrt()));
    }

    @Test
    void isUnusedInBox_GivenUnUsedNumberAndAlmostFilledBox_ShouldReturnTrue() {
        Sudoku sudoku = new Sudoku();
        Tile[] box = this.sudokuService.getBox(sudoku, 0, 0);
        for (int i = 1; i < box.length; i++) {
            Tile tile = box[i];
            tile.setSolution(i);
        }

        assertTrue(this.sudokuService.isUnusedInBox(sudoku, box[0], sudoku.getSize()));
    }

    @Test
    void isUnusedInBox_GivenUsedNumberAndAlmostFilledBox_ShouldReturnFalse() {
        Sudoku sudoku = new Sudoku();
        Tile[] box = this.sudokuService.getBox(sudoku, 0, 0);


        for (int i = 0; i < box.length; i++) {
            Tile tile = box[i];
            tile.setSolution(i + 1);
        }

        assertFalse(this.sudokuService.isUnusedInBox(sudoku, box[0], box[4].getSolution()));
    }

    @Test
    void fillTile_GivenFillableTile_ShouldReturnTrue() {
        Sudoku sudoku = new Sudoku();
        this.sudokuService.fillDiagonal(sudoku);

        Tile[][] grid = sudoku.getGrid();

        assertTrue(this.sudokuService.fillTile(sudoku, 4, 1));
    }

    @Test
    void fillTile_GivenFillableTile_ShouldFillWithSafeNumbers() {
        Sudoku sudoku = new Sudoku();
        this.sudokuService.fillDiagonal(sudoku);

        Tile[][] grid = sudoku.getGrid();
        Tile tile = grid[4][1];
        if (this.sudokuService.fillTile(sudoku, 4, 1)) {
            int current = tile.getSolution();
            tile.setSolution(0);

            assertTrue(this.sudokuService.checkIfSafe(sudoku, tile, current));

            tile.setSolution(current);
        }
    }

    @Test
    void fillSudoku_GivenDiagonalSudoku_ShouldCompletelyFill() {
        Sudoku sudoku = new Sudoku();
        this.sudokuService.fillDiagonal(sudoku);

        Tile[][] grid = sudoku.getGrid();

        this.sudokuService.fillSudoku(sudoku);

        for (Tile[] tiles : grid) {
            for (Tile tile : tiles) {
                assertTrue(tile.getSolution() != 0);
            }
        }
    }

    @Test
    void fillSudoku_GivenDiagonalSudoku_AllTilesShouldBeSafe() {
        Sudoku sudoku = new Sudoku();
        this.sudokuService.fillDiagonal(sudoku);
        this.sudokuService.fillSudoku(sudoku);

        Tile[][] grid = sudoku.getGrid();

        for (Tile[] tiles : grid) {
            for (Tile tile : tiles) {
                int current = tile.getSolution();
                tile.setSolution(0);

                assertTrue(this.sudokuService.checkIfSafe(sudoku, tile, current));

                tile.setSolution(current);
            }
        }
    }

}