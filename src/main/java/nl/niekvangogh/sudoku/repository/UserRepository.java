package nl.niekvangogh.sudoku.repository;

import nl.niekvangogh.sudoku.entity.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByName(String name);
}