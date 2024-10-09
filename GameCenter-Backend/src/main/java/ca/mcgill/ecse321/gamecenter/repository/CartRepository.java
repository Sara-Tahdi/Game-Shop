package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Integer> {
    Optional<Cart> findCartById(int id);

    Optional<Cart> findCartByClientIdAndGameId(int clientId, int gameId);

    Optional<List<Cart>> findCartsByClientId(int clientId);

    Optional<List<Cart>> findCartsByGameId(int gameId);
}
