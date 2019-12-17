package nl.niekvangogh.sudoku.entity;

import lombok.Getter;
import lombok.Setter;
import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.pojo.game.GameState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "games")
public class Game extends AbstractBaseEntity {

    @OneToMany
    @Getter
    private List<Player> players;

    @Column()
    @Setter
    @Getter
    private long seed;

    @Column()
    @Setter
    @Getter
    private Ranking ranking;

    @Column()
    @Setter
    @Getter
    private GameState gameState = GameState.NOT_STARTED;
}
