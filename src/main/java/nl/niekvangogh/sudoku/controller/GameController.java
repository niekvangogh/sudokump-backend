package nl.niekvangogh.sudoku.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller("/game")
public class GameController {


    @MessageMapping("/play")
    public void play(SimpMessageHeaderAccessor ha) {

    }
}
