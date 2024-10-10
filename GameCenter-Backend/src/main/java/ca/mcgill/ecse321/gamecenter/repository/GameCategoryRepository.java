package ca.mcgill.ecse321.gamecenter.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;


public interface GameCategoryRepository extends CrudRepository<GameCategory, Integer> {
    public GameCategory findGameCategoryById(int id);
}
