package nl.niekvangogh.sudoku.pojo.lobby;

import lombok.Getter;
import nl.niekvangogh.sudoku.entity.ChatMessage;
import nl.niekvangogh.sudoku.entity.Player;

import java.util.List;

public class Lobby {

    @Getter
    private String instance;

    @Getter
    private List<Player> players;

    @Getter
    private List<ChatMessage> messages;

}
