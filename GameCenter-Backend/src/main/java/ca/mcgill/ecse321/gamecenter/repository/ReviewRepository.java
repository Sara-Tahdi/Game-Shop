package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    Optional<Review> findReviewById(int id);

    @Query("select r from Review r where r.game.id = :gameId")
    Optional<List<Review>> findReviewsByGameId(@Param("gameId") int gameId);
}
