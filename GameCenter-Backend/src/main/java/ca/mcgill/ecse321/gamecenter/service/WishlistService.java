package ca.mcgill.ecse321.gamecenter.service;
import ca.mcgill.ecse321.gamecenter.model.Wishlist;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.repository.WishlistRepository;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import jakarta.transaction.Transactional;


@Service
public class WishlistService {
    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private GameRepository gameRepository;

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

            throw new IllegalArgumentException("There is no wishlist with ID: " + wishlistId);
        }

        //There is no way for me to know that the returned wishlist is the right one fetched with the provided ID
        return wishlist;
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
    
    public Wishlist addGameToWishlist(int clientId, int gameId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            throw new IllegalArgumentException("No client found with id: " + clientId);
        }
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            throw new IllegalArgumentException("No game found with id: " + gameId);
        }

        //Does a wishlist automatically have an ID created, or do i need to set it?
        Wishlist wishlist = new Wishlist();
        wishlist.setClient(client);
        wishlist.setGame(game);
        return wishlistRepository.save(wishlist);
    }

    public void removeGameFromWishlist(int clientId, int gameId) {
        Wishlist wishlistItem = wishlistRepository.findWishlistByClientIdAndGameId(clientId, gameId).orElse(null);
        if (wishlistItem == null) {
            throw new IllegalArgumentException("There is no wishlist with Game ID: " + gameId + " and Client ID: " + clientId);
        }

        //Again, would this even work? Because doesnt wishlistItem actually is just a wishlist and not an item?
        wishlistRepository.delete(wishlistItem);
    }

    //Should I also have an update method? 
    // I cant have a method to view the items in the wishlist or in the cart because I only have wishlist.getGame (outputs a single game), not wishlist.getGames
}