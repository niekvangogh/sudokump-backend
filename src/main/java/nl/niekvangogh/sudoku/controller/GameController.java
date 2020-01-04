package nl.niekvangogh.sudoku.controller;

import com.google.gson.Gson;
import nl.niekvangogh.sudoku.service.impl.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller()
public class GameController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private Gson gson = new Gson();

    @MessageMapping("/game")
    @SendToUser("/queue/reply")
    public String processMessageFromClient(
            @Payload String message,
            Principal principal) throws Exception {
        System.out.println(" kasjfasdlfsdklfal;fjklasd;jkl ");
        return gson
                .fromJson(message, Map.class)
                .get("name").toString();
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}