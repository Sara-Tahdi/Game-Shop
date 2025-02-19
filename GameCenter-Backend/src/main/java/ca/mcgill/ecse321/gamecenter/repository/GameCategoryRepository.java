package ca.mcgill.ecse321.gamecenter.repository;
import java.util.List;

import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface GameCategoryRepository extends CrudRepository<GameCategory, Integer> {
    Optional<GameCategory> findGameCategoryById(int id);
    Optional<GameCategory> findGameCategoryByCategory(String category);

    @Query("SELECT g FROM GameCategory g")
    Optional<List<GameCategory>> getAll();
}
