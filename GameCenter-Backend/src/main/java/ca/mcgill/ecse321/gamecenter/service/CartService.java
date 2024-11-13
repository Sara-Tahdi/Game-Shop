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
        // Fetch client and game
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            throw new IllegalArgumentException("No client found with client id: " + clientId);
        }

        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            throw new IllegalArgumentException("No game found with game id: " + gameId);
        }

        // Check if the game is already in the cart for the client
        Cart existingCart = cartRepository.findCartByClientIdAndGameId(clientId, gameId).orElse(null);
        if (existingCart != null) {
            throw new IllegalArgumentException("Game already exists in the cart for this client.");
        }

        // Create and save cart
        Cart cart = new Cart();
        cart.setClient(client);
        cart.setGame(game);

        // Persist
        clientRepository.save(client);
        gameRepository.save(game);
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

    public Cart findCartByClientIdAndGameId(int gameId, int clientId) {
        Cart cart = cartRepository.findCartByClientIdAndGameId(clientId,gameId).orElse(null);
        if (cart == null) {
            throw new IllegalArgumentException("There is no cart with Game ID: " + gameId + " and Client ID: " + clientId);
        }

        return cart;
    }

    public void removeCart(int clientId, int gameId) {
        // Fetch the cart item
        Cart cartItem = cartRepository.findCartByClientIdAndGameId(clientId, gameId).orElse(null);
        if (cartItem == null) {
            throw new IllegalArgumentException("There is no cart with Game ID: " + gameId + " and Client ID: " + clientId);
        }
    
        // Delete the cart item
        cartRepository.delete(cartItem);
    }

    //Should I add an "update" method
    //I should probably add a view all items in cart method
}