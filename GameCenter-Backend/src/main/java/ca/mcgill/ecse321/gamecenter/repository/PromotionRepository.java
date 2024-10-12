package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Promotion;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PromotionRepository extends CrudRepository<Promotion, Integer> {
    Optional<Promotion> findPromotionById(int id);
}
