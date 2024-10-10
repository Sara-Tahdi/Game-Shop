package ca.mcgill.ecse321.gamecenter.repository;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameRequest;
import ca.mcgill.ecse321.gamecenter.model.Purchase;
import ca.mcgill.ecse321.gamecenter.model.Wishlist;
import ca.mcgill.ecse321.gamecenter.model.Cart;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Integer> {
    Optional<Game> findGameById(int id);
    Optional<Game> findGameByTitle(String title);
    Optional<Game> findGameByDescription(String description);
    Optional<Game> updateGamebyQuantity(int remainingQuantity);
    Optional<Game> findGameByPurchase(Purchase purchase);
    Optional<Game> findGameByCart(Cart cart);
    Optional<Game> findGameByGameRequest(GameRequest gameRequest);
    Optional<Game> findGameByWishlist(Wishlist wishlist);

}
