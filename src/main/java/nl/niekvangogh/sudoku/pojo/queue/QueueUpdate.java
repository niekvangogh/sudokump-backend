package nl.niekvangogh.sudoku.pojo.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.niekvangogh.sudoku.entity.Game;

@AllArgsConstructor
@Getter
public class QueueUpdate {

    private Game foundGame;
}
