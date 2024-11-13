package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Game;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends CrudRepository<Game, Integer> {
    @Query("SELECT g FROM Game g WHERE g.id = :id")
    Optional<Game> findGameById(int id);
    Optional<Game> findGameByTitle(String title);
    Optional<Game> findGameByDescription(String description);

    @Query("SELECT g FROM Game g WHERE TYPE(g) = :type")
    Optional<List<Game>> findGameByGameType(@Param("type") Class<?> type);

    @Query("SELECT g FROM Game g WHERE g.category.id = :categoryId")
    Optional<List<Game>> findGamesByGameCategoryId(int categoryId);

    @Query("SELECT g FROM Game g WHERE g.category.category = :category")
    Optional<List<Game>> findGamesByGameCategory(String category);

    @Query("SELECT g FROM Game g WHERE g.price >= :minPrice AND g.price <= :maxPrice")
    Optional<List<Game>>findGamesByPriceRange(Float minPrice, Float maxPrice);

    @Query("SELECT g FROM Game g WHERE g.rating >= :minRating AND g.rating <= :maxRating")
    Optional<List<Game>>findGamesByRatingRange(Float minRating, Float maxRating);

    @Query("SELECT g FROM Game g WHERE g.isOffered = true")
    Optional<List<Game>> findAllAvailableGames();

    @Query("SELECT g FROM Game g WHERE g.isOffered = true AND g.category.category = :category")
    Optional<List<Game>> findAllAvailableGamesByGameCategory(String category);
    @Query("SELECT g FROM Game g WHERE g.isOffered = true AND g.price >= :minPrice AND g.price <= :maxPrice")
    Optional<List<Game>> findAllAvailableGamesByPriceRange(float minPrice, float maxPrice);

    @Query("SELECT g FROM Game g WHERE g.isOffered = true AND g.rating >= :minRating AND g.rating <= :maxRating")
    Optional<List<Game>> findAllAvailableGamesByRatingRange(float minRating, float maxRating);

    @Modifying
    @Transactional
    @Query("UPDATE Game g SET g.remainingQuantity = :remainingQuantity WHERE g.id = :gameId")
    void updateGameByQuantity(int gameId, int remainingQuantity);
}
