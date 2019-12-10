package nl.niekvangogh.sudoku.entity;

import lombok.Getter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractBaseEntity {

    @Getter
    @Id
    private long id;

}