package nl.niekvangogh.sudoku.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import nl.niekvangogh.sudoku.pojo.AuthProvider;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User extends AbstractBaseEntity {

    @NotBlank
    @Column(unique = true)
    @Size(min = 4, max = 32)
    @Getter
    @Setter
    private String name;

    @NotBlank
    @Column(unique = true)
    @Size(min = 1, max = 100)
    @Getter
    @Setter
    private String email;

    @Size(min = 8, max = 200)
    @Getter
    @Setter
    @JsonIgnore
    private String password;

    @Column()
    @Getter
    @Setter
    private int rating;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Setter
    @Getter
    private AuthProvider provider;

    private String providerId;
}
