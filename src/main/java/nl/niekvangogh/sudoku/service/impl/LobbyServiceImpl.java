package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.entity.ChatMessage;
import nl.niekvangogh.sudoku.entity.Player;
import nl.niekvangogh.sudoku.pojo.lobby.Lobby;
import nl.niekvangogh.sudoku.service.LobbyService;

public class LobbyServiceImpl implements LobbyService {

    @Override
    public Lobby findLobby(Player player) {
        return null;
    }

    @Override
    public void joinLobby(Lobby lobby, Player player) {

    }

    @Override
    public void onChat(Player player, ChatMessage message) {

    }
}
