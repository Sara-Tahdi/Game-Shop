package ca.mcgill.ecse321.gamecenter.repository;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.gamecenter.model.GameRequest;

public interface GameRequestRepository extends CrudRepository<GameRequest, Integer> {
    public GameRequest findGameRequestById(int id);
}
