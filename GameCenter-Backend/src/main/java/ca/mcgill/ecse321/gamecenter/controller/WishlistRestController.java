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
public class WishlistRestController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping(value = "/wishlists/create")
    public WishlistResponseDto createWishlist(@RequestBody WishlistRequestDto request) {
        Wishlist createdWishlist = wishlistService.createWishlist(request.getClientId(), request.getGameId());
        return new WishlistResponseDto(createdWishlist);
    }

    @DeleteMapping(value = "/wishlists/remove")
    public ResponseEntity<Object> removeGameFromWishlist(@RequestParam int clientId, @RequestParam int gameId) {
        wishlistService.removeWishlist(clientId, gameId);
        return ResponseEntity.noContent().build();  // HTTP 204 No Content
    }

    @GetMapping(value = "/wishlists/{wishlistId}")
    public WishlistResponseDto findWishlistById(@PathVariable int wishlistId) {
        Wishlist wishlist = wishlistService.findWishlistById(wishlistId);
        return new WishlistResponseDto(wishlist);
    }

    @GetMapping(value = "/wishlists/client/{clientId}")
    public List<WishlistResponseDto> findWishlistsByClientId(@PathVariable int clientId) {
        List<Wishlist> wishlists = wishlistService.findWishlistsByClientId(clientId);
        return wishlists.stream()
                        .map(WishlistResponseDto::new)
                        .collect(Collectors.toList());
    }

    @GetMapping(value = "/wishlists/game/{gameId}")
    public List<WishlistResponseDto> findWishlistsByGameId(@PathVariable int gameId) {
        List<Wishlist> wishlists = wishlistService.findWishlistsByGameId(gameId);
        return wishlists.stream()
                        .map(WishlistResponseDto::new)
                        .collect(Collectors.toList());
    }

    @GetMapping(value = "/wishlists/client/{clientId}/game/{gameId}")
    public WishlistResponseDto findWishlistByClientAndGame(@PathVariable int clientId, @PathVariable int gameId) {
        Wishlist wishlist = wishlistService.findWishlistByClientIdAndGameId(clientId, gameId);
        return new WishlistResponseDto(wishlist);
    }
}
