package nl.niekvangogh.sudoku.pojo.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {

    private String email;
    private String username;
    private String password;
}