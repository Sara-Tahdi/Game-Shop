package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Wishlist;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {
    Optional<Wishlist> findWishlistById(int id);

    Optional<Wishlist> findWishlistByClientIdAndGameId(int clientId, int gameId);

    Optional<List<Wishlist>> findWishlistsByClientId(int clientId);

    Optional<List<Wishlist>> findWishlistsByGameId(int gameId);
}
