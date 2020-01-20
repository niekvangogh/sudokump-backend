package nl.niekvangogh.sudoku.pojo;

import lombok.Getter;
import nl.niekvangogh.sudoku.entity.User;

import java.io.Serializable;

@Getter
public class PublicUser implements Serializable {

    private final String username;
    private final long id;
    private final int rating;

    public PublicUser(User user) {
        this.username = user.getName();
        this.id = user.getId();
        this.rating = user.getRating();
    }
}
