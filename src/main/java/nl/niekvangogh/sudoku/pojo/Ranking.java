package nl.niekvangogh.sudoku.pojo;

import lombok.Getter;

public enum Ranking {

    BRONZE(0),
    SILVER(1500),
    GOLD(2000),
    PLATINUM(2500),
    DIAMOND(3000),
    MASTERS(3500),
    GRANDMASTER(4000);

    @Getter
    private final int minimum;

    Ranking(int minimum) {
        this.minimum = minimum;
    }

    public boolean isIn(int rating) {
        return true;

    }
}
