package nl.niekvangogh.sudoku.controller;

import nl.niekvangogh.sudoku.entity.User;
import nl.niekvangogh.sudoku.pojo.queue.QueueUpdate;
import nl.niekvangogh.sudoku.pojo.queue.QueueUpdateResponse;
import nl.niekvangogh.sudoku.repository.UserRepository;
import nl.niekvangogh.sudoku.service.impl.GameManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Controller()
public class GameController {

    @Autowired
    private SimpMessageSendingOperations messageSendingService;

    @Autowired
    private GameManagerServiceImpl gameManagerService;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/game/queue/start")
    public void startQueue(Message<Object> message, @Payload String payload, Principal principal, SimpMessageHeaderAccessor accessor) {
        Optional<User> optionalUser = this.userRepository.findByEmail(principal.getName());
        if (!optionalUser.isPresent()) {
            return;
        }
        User user = optionalUser.get();


        CompletableFuture<QueueUpdate> future = this.gameManagerService.queuePlayer(user);

        future.whenComplete((queueUpdate, throwable) -> {
            this.messageSendingService.convertAndSendToUser(accessor.getSessionId(), "/game/queue/status", new QueueUpdateResponse(queueUpdate.getGameId()), this.createHeaders(accessor.getSessionId()));
        });

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