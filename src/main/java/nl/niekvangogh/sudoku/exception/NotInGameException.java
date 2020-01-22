package nl.niekvangogh.sudoku.exception;

import lombok.AllArgsConstructor;
import nl.niekvangogh.sudoku.pojo.Game;
import nl.niekvangogh.sudoku.entity.User;

@AllArgsConstructor
public class NotInGameException extends ApiException {

    private final Game game;
    private final User user;
}
