package nl.niekvangogh.sudoku.pojo.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
public class QueueEnteredResponse {

    private boolean entered;
}
