package nl.niekvangogh.sudoku.service;

import nl.niekvangogh.sudoku.entity.ChatMessage;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.lobby.Lobby;

public interface ILobbyService {

    Lobby createLobby();

    Lobby findLobby(User user);

    void joinLobby(Lobby lobby,User user);

    void leaveLobby(Lobby lobby, User user);

    Lobby getLobby(User user);

    void onChat(User user, ChatMessage message);

}