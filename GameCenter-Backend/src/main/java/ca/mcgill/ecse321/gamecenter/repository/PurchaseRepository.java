package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Purchase;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {
    Optional<Purchase> findPurchaseById(int id);
}
