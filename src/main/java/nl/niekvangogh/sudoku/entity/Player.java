package nl.niekvangogh.sudoku.entity;

import lombok.Getter;
import nl.niekvangogh.sudoku.pojo.User;

public class Player {

    @Getter
    private final User user;

    public Player(User user) {
        this.user = user;
    }

    // connectino
    //todo

}