package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.entity.ChatMessage;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.lobby.Lobby;
import nl.niekvangogh.sudoku.service.LobbyService;

import java.util.ArrayList;
import java.util.List;

public class LobbyServiceImpl implements LobbyService {

    private List<Lobby> lobbies = new ArrayList<>();

    @Override
    public Lobby createLobby() {
        Lobby lobby = new Lobby();
        this.lobbies.add(lobby);
        return lobby;
    }

    @Override
    public Lobby findLobby(User user) {
        Lobby lobby = lobbies.get(0);
        if (lobby == null) {
            lobby = this.createLobby();
        }
        return lobby;
    }

    @Override
    public void joinLobby(Lobby lobby, User user) {
        lobby.getUsers().add(user);
    }

    @Override
    public void leaveLobby(Lobby lobby, User user) {
        lobby.getUsers().remove(user);
    }

    @Override
    public Lobby getLobby(User user) {
        return this.lobbies.stream().filter(lobby -> lobby.getUsers().contains(user)).findFirst().orElse(null);
    }

    @Override
    public void onChat(User user, ChatMessage message) {

    }
}
