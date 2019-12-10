package nl.niekvangogh.sudoku.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table("games")
public class Game extends AbstractBaseEntity {

    @OneToMany
    private List<Player> players;
}
