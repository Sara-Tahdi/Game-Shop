package ca.mcgill.ecse321.gamecenter.service;
import ca.mcgill.ecse321.gamecenter.model.Wishlist;
import ca.mcgill.ecse321.gamecenter.repository.WishlistRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import jakarta.transaction.Transactional;


@Service
public class WishlistService {
    @Autowired
    private WishlistRepository wishlistRepository;

    @Transactional

    public Wishlist createWishlist(int clientId, int gameId) {
        Wishlist wishlist = new Wishlist();

        //Do I have to set a client who the wishlist belongs to

        return wishlistRepository.save(wishlist);
    }

    public Wishlist findWishlistById(int wishlistId) {

        Wishlist wishlist = wishlistRepository.findById(wishlistId).orElse(null);
        if (wishlist == null) {

            //Aren't we supposed to add something like HttpStatus.NOT_FOUND,String.format

            throw new IllegalArgumentException("There is no wishlist with ID %d.");
        }

        //There is no way for me to know that the returned wishlist is the right one fetched with the provided ID
        return wishlistRepository.save(wishlist);
    }

    public List<Wishlist> findWishlistsByClientId(int clientId) {

        List<Wishlist> wishlists = wishlistRepository.findWishlistsByClientId(clientId).orElse(null);
        if (wishlists == null) {
            throw new IllegalArgumentException("There is no wishlist with Client ID: " + clientId);
        }
        return wishlists;
    }

    public List<Wishlist> findWishlistsByGameId(int gameId) {
        List<Wishlist> wishlists = wishlistRepository.findWishlistsByGameId(gameId).orElse(null);
        if (wishlists == null) {
            throw new IllegalArgumentException("There is no wishlist with Game ID: " + gameId);
        }

        return wishlists;
    }

    public Wishlist findWishlistByClientIdAndGameId(int gameId, int clientId) {
        Wishlist wishlist = wishlistRepository.findWishlistByClientIdAndGameId(clientId,gameId).orElse(null);
        if (wishlist == null) {
            throw new IllegalArgumentException("There is no wishlist with Game ID: " + gameId + " and Client ID: " + clientId);
        }

        return wishlist;
    }


}