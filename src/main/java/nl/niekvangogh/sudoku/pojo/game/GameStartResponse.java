package nl.niekvangogh.sudoku.pojo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.niekvangogh.sudoku.pojo.PublicUser;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class GameStartResponse {

    private boolean ready;

    private List<PublicUser> players;
}
