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
 * Controller for managing cart
 */
@RestController
public class CartRestController {

    @Autowired
    private CartService cartService;

    @PostMapping(value = "/carts/create")
    public CartResponseDto createCart(@RequestBody CartRequestDto request) {
        Cart createdCart = cartService.createCart(request.getClientId(), request.getGameId());
        return new CartResponseDto(createdCart);
    }

    @DeleteMapping(value = "/carts/remove")
    public ResponseEntity<Object> removeGameFromCart(@RequestParam int clientId, @RequestParam int gameId) {
        cartService.removeCart(clientId, gameId);
        return ResponseEntity.noContent().build();  // HTTP 204 No Content
    }

    @GetMapping(value = "/carts/{cartId}")
    public CartResponseDto findCartById(@PathVariable int cartId) {
        Cart cart = cartService.findCartById(cartId);
        return new CartResponseDto(cart);
    }

    @GetMapping(value = "/carts/client/{clientId}")
    public List<CartResponseDto> findCartsByClientId(@PathVariable int clientId) {
        List<Cart> carts = cartService.findCartsByClientId(clientId);
        return carts.stream()
                    .map(CartResponseDto::new)
                    .collect(Collectors.toList());
    }

    @GetMapping(value = "/carts/game/{gameId}")
    public List<CartResponseDto> findCartsByGameId(@PathVariable int gameId) {
        List<Cart> carts = cartService.findCartsByGameId(gameId);
        return carts.stream()
                    .map(CartResponseDto::new)
                    .collect(Collectors.toList());
    }

    @GetMapping(value = "/carts/client/{clientId}/game/{gameId}")
    public CartResponseDto findCartByClientAndGame(@PathVariable int clientId, @PathVariable int gameId) {
        Cart cart = cartService.findCartByClientIdAndGameId(gameId, clientId);
        return new CartResponseDto(cart);
    }
}
