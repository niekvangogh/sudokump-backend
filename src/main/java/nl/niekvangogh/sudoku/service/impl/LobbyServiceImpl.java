package nl.niekvangogh.sudoku.service.impl;

import nl.niekvangogh.sudoku.entity.ChatMessage;
import nl.niekvangogh.sudoku.entity.Player;
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
    public Lobby findLobby(Player player) {
        Lobby lobby = lobbies.get(0);
        if (lobby == null) {
            lobby = this.createLobby();
        }
        return lobby;
    }

    @Override
    public void joinLobby(Lobby lobby, Player player) {
        lobby.getPlayers().add(player);
    }

    @Override
    public void leaveLobby(Lobby lobby, Player player) {
        lobby.getPlayers().remove(player);
    }

    @Override
    public Lobby getLobby(Player player) {
        return this.lobbies.stream().filter(lobby -> lobby.getPlayers().contains(player)).findFirst().orElse(null);
    }

    @Override
    public void onChat(Player player, ChatMessage message) {

    }
}
