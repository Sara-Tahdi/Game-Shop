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
@CrossOrigin(origins = "http://localhost:3000")
public class CartRestController {

    @Autowired
    private CartService cartService;

    @PostMapping(value = "/carts/create")
    public ResponseEntity<?> createCart(@RequestBody CartRequestDto request) {
        try {
            Cart createdCart = cartService.createCart(request.getClientId(), request.getGameId());
            return ResponseEntity.ok().body(new CartResponseDto(createdCart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/carts/remove")
    public ResponseEntity<?> removeGameFromCart(@RequestParam int clientId, @RequestParam int gameId) {
        try {
            cartService.removeCart(clientId, gameId);
            return ResponseEntity.noContent().build();  // HTTP 204 No Content
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/carts/{cartId}")
    public ResponseEntity<?> findCartById(@PathVariable int cartId) {
        try {
            Cart cart = cartService.findCartById(cartId);
            return ResponseEntity.ok().body(new CartResponseDto(cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/carts/client/{clientId}")
    public ResponseEntity<?> findCartsByClientId(@PathVariable int clientId) {
        try {
            List<Cart> carts = cartService.findCartsByClientId(clientId);
            return ResponseEntity.ok().body(carts.stream()
                    .map(CartResponseDto::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/carts/game/{gameId}")
    public ResponseEntity<?> findCartsByGameId(@PathVariable int gameId) {
        try {
            List<Cart> carts = cartService.findCartsByGameId(gameId);
            return ResponseEntity.ok().body(carts.stream()
                    .map(CartResponseDto::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/carts/client/{clientId}/game/{gameId}")
    public ResponseEntity<?> findCartByClientAndGame(@PathVariable int clientId, @PathVariable int gameId) {
        try {
            Cart cart = cartService.findCartByClientIdAndGameId(gameId, clientId);
            return ResponseEntity.ok().body(new CartResponseDto(cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
