package nl.niekvangogh.sudoku.controller;

import com.google.gson.Gson;
import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.queue.QueueEnteredResponse;
import nl.niekvangogh.sudoku.service.impl.GameManagerServiceImpl;
import nl.niekvangogh.sudoku.service.impl.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller()
public class GameController {

    @Autowired
    private SimpMessageSendingOperations messageSendingService;

    @Autowired
    private GameManagerServiceImpl gameManagerService;

    @MessageMapping("/game/queue/start")
    public void startQueue(Message<Object> message, @Payload String payload, Principal principal, SimpMessageHeaderAccessor accessor) throws Exception {
        this.gameManagerService.queuePlayer(new User());

        this.messageSendingService.convertAndSendToUser(accessor.getSessionId(), "/game/queue/status", new QueueEnteredResponse(true), this.createHeaders(accessor.getSessionId()));
    }

    @MessageMapping("/game/queue/cancel")
    public void cancelQueue() {
//        this.gameManagerService.cancelQueue(player);
    }

    @MessageExceptionHandler
    @SendToUser("/game/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }


    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}