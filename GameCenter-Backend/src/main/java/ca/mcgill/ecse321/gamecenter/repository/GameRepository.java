package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Game;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GameRepository extends CrudRepository<Game, Integer> {
    Optional<Game> findGameById(int id);
    Optional<Game> findGameByTitle(String title);
    Optional<Game> findGameByDescription(String description);

    @Modifying
    @Transactional
    @Query("UPDATE Game g SET g.remainingQuantity = :remainingQuantity WHERE g.id = :gameId")
    void updateGameByQuantity(int gameId, int remainingQuantity);
}
