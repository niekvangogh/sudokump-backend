package nl.niekvangogh.sudoku.pojo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
public class GameUpdateResponse implements Serializable {

    private final String event;

}
