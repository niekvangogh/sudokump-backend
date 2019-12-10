package nl.niekvangogh.sudoku.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table("players")
public class Player extends AbstractBaseEntity {

    @OneToOne
    private User user;

    public int rating;
}