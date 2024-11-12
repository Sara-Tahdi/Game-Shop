package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Promotion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository extends CrudRepository<Promotion, Integer> {
    Optional<Promotion> findPromotionById(int id);

    @Query("select p from Promotion p where p.game.id = :gameId")
    Optional<List<Promotion>> findPromotionsByGameId(@Param("gameId") int gameId);

    @Query("SELECT p FROM Promotion p WHERE TYPE(p) = :type")
    Optional<List<Promotion>> findPromotionByPromotionType(@Param("type") Class<?> type);
}
