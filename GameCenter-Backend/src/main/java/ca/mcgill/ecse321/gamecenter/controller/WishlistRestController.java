package ca.mcgill.ecse321.gamecenter.controller;
import ca.mcgill.ecse321.gamecenter.dto.Wishlist.*;
import ca.mcgill.ecse321.gamecenter.model.Wishlist;
import ca.mcgill.ecse321.gamecenter.service.WishlistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class WishlistRestController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping(value = "/wishlists/create")
    public ResponseEntity<?> createWishlist(@RequestBody WishlistRequestDto request) {
        try {
            Wishlist createdWishlist = wishlistService.createWishlist(request.getClientId(), request.getGameId());
            return ResponseEntity.ok().body(new WishlistResponseDto(createdWishlist));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/wishlists/remove")
    public ResponseEntity<?> removeGameFromWishlist(@RequestParam int clientId, @RequestParam int gameId) {
        try {
            wishlistService.removeWishlist(clientId, gameId);
            return ResponseEntity.noContent().build();  // HTTP 204 No Content
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/wishlists/{wishlistId}")
    public ResponseEntity<?> findWishlistById(@PathVariable int wishlistId) {
        try {
            Wishlist wishlist = wishlistService.findWishlistById(wishlistId);
            return ResponseEntity.ok().body(new WishlistResponseDto(wishlist));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/wishlists/client/{clientId}")
    public ResponseEntity<?> findWishlistsByClientId(@PathVariable int clientId) {
        try {
            List<Wishlist> wishlists = wishlistService.findWishlistsByClientId(clientId);
            return ResponseEntity.ok().body(wishlists.stream()
                    .map(WishlistResponseDto::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/wishlists/game/{gameId}")
    public ResponseEntity<?> findWishlistsByGameId(@PathVariable int gameId) {
        try {
            List<Wishlist> wishlists = wishlistService.findWishlistsByGameId(gameId);
            return ResponseEntity.ok().body(wishlists.stream()
                    .map(WishlistResponseDto::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/wishlists/client/{clientId}/game/{gameId}")
    public ResponseEntity<?> findWishlistByClientAndGame(@PathVariable int clientId, @PathVariable int gameId) {
        try {
            Wishlist wishlist = wishlistService.findWishlistByClientIdAndGameId(clientId, gameId);
            return ResponseEntity.ok().body(new WishlistResponseDto(wishlist));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
