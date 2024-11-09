package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.Cart.CartRequestDto;
import ca.mcgill.ecse321.gamecenter.dto.Cart.CartResponseDto;
import ca.mcgill.ecse321.gamecenter.model.Cart;
import ca.mcgill.ecse321.gamecenter.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing shopping carts.
 */
@RestController
public class CartRestController {

    @Autowired
    private CartService cartService;

    /**
     * Create a new cart entry for a client with a specific game.
     *
     * @param request The DTO containing the client and game IDs.
     * @return The created cart.
     */
    @PostMapping(value = "/carts/create")
    public CartResponseDto createCart(@RequestBody CartRequestDto request) {
        Cart createdCart = cartService.createCart(request.getClientId(), request.getGameId());
        return new CartResponseDto(createdCart);
    }

    /**
     * Remove a game from a client's cart.
     *
     * @param clientId The client ID whose cart the game should be removed from.
     * @param gameId The game ID to remove from the cart.
     * @return HTTP status indicating the removal success.
     */
    @DeleteMapping(value = "/carts/remove")
    public ResponseEntity<Object> removeGameFromCart(@RequestParam int clientId, @RequestParam int gameId) {
        cartService.removeCart(clientId, gameId);
        return ResponseEntity.noContent().build();  // HTTP 204 No Content
    }

    /**
     * Find a cart by its ID.
     *
     * @param cartId The cart ID to search for.
     * @return The cart with the given ID.
     */
    @GetMapping(value = "/carts/{cartId}")
    public CartResponseDto findCartById(@PathVariable int cartId) {
        Cart cart = cartService.findCartById(cartId);
        return new CartResponseDto(cart);
    }

    /**
     * Find all carts for a specific client.
     *
     * @param clientId The client ID whose carts are to be retrieved.
     * @return A list of all carts for the specified client.
     */
    @GetMapping(value = "/carts/client/{clientId}")
    public List<CartResponseDto> findCartsByClientId(@PathVariable int clientId) {
        List<Cart> carts = cartService.findCartsByClientId(clientId);
        return carts.stream()
                    .map(CartResponseDto::new)
                    .collect(Collectors.toList());
    }

    /**
     * Find all carts that contain a specific game.
     *
     * @param gameId The game ID whose carts are to be retrieved.
     * @return A list of all carts that contain the specified game.
     */
    @GetMapping(value = "/carts/game/{gameId}")
    public List<CartResponseDto> findCartsByGameId(@PathVariable int gameId) {
        List<Cart> carts = cartService.findCartsByGameId(gameId);
        return carts.stream()
                    .map(CartResponseDto::new)
                    .collect(Collectors.toList());
    }

    /**
     * Find a specific cart for a client and a game.
     *
     * @param clientId The client ID.
     * @param gameId The game ID.
     * @return The cart if it exists, else a 404 error.
     */
    @GetMapping(value = "/carts/client/{clientId}/game/{gameId}")
    public CartResponseDto findCartByClientAndGame(@PathVariable int clientId, @PathVariable int gameId) {
        Cart cart = cartService.findCartByClientIdAndGameId(gameId, clientId);
        return new CartResponseDto(cart);
    }
}
