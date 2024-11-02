package ca.mcgill.ecse321.gamecenter.service;
import ca.mcgill.ecse321.gamecenter.model.Cart;
import ca.mcgill.ecse321.gamecenter.model.Wishlist;
import ca.mcgill.ecse321.gamecenter.repository.CartRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import jakarta.transaction.Transactional;


@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

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

    public Cart findCartByClientIdAndGameId(int gameId, int clientId) {
        Cart cart = cartRepository.findCartByClientIdAndGameId(clientId,gameId).orElse(null);
        if (cart == null) {
            throw new IllegalArgumentException("There is no cart with Game ID: " + gameId + " and Client ID: " + clientId);
        }

        return cart;
    }
}