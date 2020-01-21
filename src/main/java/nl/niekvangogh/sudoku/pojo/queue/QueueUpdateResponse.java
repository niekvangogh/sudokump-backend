package nl.niekvangogh.sudoku.pojo.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class QueueUpdateResponse implements Serializable {

    private long gameId;
}
