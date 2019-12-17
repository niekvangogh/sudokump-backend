package nl.niekvangogh.sudoku.service;

import nl.niekvangogh.sudoku.entity.ChatMessage;
import nl.niekvangogh.sudoku.entity.Player;
import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.pojo.lobby.Lobby;

public interface LobbyService {

    Lobby createLobby();

    Lobby findLobby(Player player);

    void joinLobby(Lobby lobby, Player player);

    void leaveLobby(Lobby lobby, Player player);

    Lobby getLobby(Player player);

    void onChat(Player player, ChatMessage message);

}