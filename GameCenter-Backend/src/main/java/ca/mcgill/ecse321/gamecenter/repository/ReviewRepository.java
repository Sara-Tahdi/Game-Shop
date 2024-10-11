package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface ReviewRepository extends CrudRepository<Review, Integer> {

}
