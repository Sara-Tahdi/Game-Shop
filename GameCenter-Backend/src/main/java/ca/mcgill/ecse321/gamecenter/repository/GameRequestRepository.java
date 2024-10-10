package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.GameRequest;

import org.springframework.data.repository.CrudRepository;

public interface GameRequestRepository extends CrudRepository<GameRequest, Integer> {

}
