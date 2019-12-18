package nl.niekvangogh.sudoku.entity;

import lombok.Getter;
import lombok.Setter;
import nl.niekvangogh.sudoku.entity.AbstractBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User extends AbstractBaseEntity {

    @NotBlank
    @Column(unique = true)
    @Size(min = 4, max = 32)
    @Getter
    @Setter
    private String username;

    @NotBlank
    @Column(unique = true)
    @Size(min = 1, max = 100)
    @Getter
    @Setter
    private String email;

    @Size(min = 8, max = 200)
    @Getter
    @Setter
    private String password;

    @Column()
    @Getter
    @Setter
    private int rating;


}
