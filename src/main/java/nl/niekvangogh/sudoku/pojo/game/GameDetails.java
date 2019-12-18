package nl.niekvangogh.sudoku.pojo.game;

import lombok.Getter;
import lombok.Setter;
import nl.niekvangogh.sudoku.entity.AbstractBaseEntity;
import nl.niekvangogh.sudoku.pojo.Ranking;
import nl.niekvangogh.sudoku.entity.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "games")
public class GameDetails extends AbstractBaseEntity {

    @OneToMany
    @Getter
    private List<User> users;

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