package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;


public interface GameCategoryRepository extends CrudRepository<GameCategory, Integer> {
    Optional<GameCategory> findGameCategoryById(int id);
    Optional<GameCategory> findGameCategoryByCategory(String category);
}
