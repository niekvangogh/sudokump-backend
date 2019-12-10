package nl.niekvangogh.sudoku.service;

import nl.niekvangogh.sudoku.entity.Player;
import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.pojo.lobby.Lobby;

public interface LobbyService {

    Lobby findLobby(Player player);

    void joinLobby(Lobby lobby, Player player);

}