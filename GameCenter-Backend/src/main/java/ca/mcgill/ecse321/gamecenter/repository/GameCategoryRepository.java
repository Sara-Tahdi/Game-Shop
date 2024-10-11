package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import org.springframework.data.repository.CrudRepository;

public interface GameCategoryRepository extends CrudRepository<GameCategory, Integer> {
}
