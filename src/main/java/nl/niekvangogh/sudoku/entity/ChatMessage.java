package nl.niekvangogh.sudoku.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table
public class ChatMessage extends AbstractBaseEntity {

    @ManyToOne
    private User sender;

    @Getter
    private Date sentAt;

    @Getter
    private String content;

}
