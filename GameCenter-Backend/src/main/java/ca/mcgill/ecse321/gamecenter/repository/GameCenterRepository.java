package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.GameCenter;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GameCenterRepository extends CrudRepository<GameCenter, String> {
    Optional<GameCenter> findGameCenterByName(String name);
}
