package ca.mcgill.ecse321.gamecenter.service;
import ca.mcgill.ecse321.gamecenter.model.Cart;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.repository.CartRepository;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import jakarta.transaction.Transactional;


@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private GameRepository gameRepository;

    @Transactional

    public Cart createCart(int clientId, int gameId) {
        Cart cart = new Cart();
        return cartRepository.save(cart);
    }

    public Cart findCartById(int cartId) {
        Cart cart = cartRepository.findCartById(cartId).orElse(null);
        if (cart == null) {
            throw new IllegalArgumentException("There is no cart with Cart ID: " + cartId);
        }
        return cart;
    }

    public List<Cart> findCartsByClientId(int clientId) {
        List<Cart> carts = cartRepository.findCartsByClientId(clientId).orElse(null);
        if (carts == null) {
            throw new IllegalArgumentException("There is no carts with Client ID: " + clientId);
        }
        return carts;
    }

    public List<Cart> findCartsByGameId(int gameId) {
        List<Cart> carts = cartRepository.findCartsByGameId(gameId).orElse(null);
        if (carts == null) {
            throw new IllegalArgumentException("There is no carts with Game ID: " + gameId);
        }
        return carts;
    }

    //Shouldnt this find a cart item and not a cart?
    public Cart findCartByClientIdAndGameId(int gameId, int clientId) {
        Cart cart = cartRepository.findCartByClientIdAndGameId(clientId,gameId).orElse(null);
        if (cart == null) {
            throw new IllegalArgumentException("There is no cart with Game ID: " + gameId + " and Client ID: " + clientId);
        }

        return cart;
    }

    public Cart addGameToCart(int clientId, int gameId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            throw new IllegalArgumentException("No client found with id: " + clientId);
        }
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            throw new IllegalArgumentException("No game found with id: " + gameId);
        }

        Cart cart = new Cart();
        cart.setClient(client);
        cart.setGame(game);
        return cartRepository.save(cart);
    }

    // Wait I dont get it its 1 cart per game? Whats the point of the cart? When I do cart.getGame, i only get 1 game. Why do clients have multiple carts?

    // I dont really know if this would work, because i cant find a game by its id in the cart
    // public void removeGameFromCart(int cartId) {
    //     Cart cart = cartRepository.findById(cartId).orElse(null);
    //     if (cart == null) {
    //         throw new IllegalArgumentException("No cart found with id: " + cartId);
    //     }

    //      Actually this for sure wouldnt work because im deleting a cart. Are we deleting carts? Or every client has a cart
    //     cartRepository.deleteById(cart.getGame().getId());
    // }
}