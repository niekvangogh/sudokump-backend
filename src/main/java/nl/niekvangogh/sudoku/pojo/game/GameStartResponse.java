package nl.niekvangogh.sudoku.pojo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.niekvangogh.sudoku.pojo.PublicUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class GameStartResponse implements Serializable {

    private boolean ready;

    private List<PublicUser> players;
}
