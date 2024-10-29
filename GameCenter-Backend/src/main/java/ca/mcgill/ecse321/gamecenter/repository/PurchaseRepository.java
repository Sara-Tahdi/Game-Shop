package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Purchase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {
    Optional<Purchase> findPurchaseById(int id);

    @Query("select p from Purchase p where p.client.id = :clientId")
    Optional<List<Purchase>> findPurchasesByClientId(@Param("clientId") int clientId);
}
