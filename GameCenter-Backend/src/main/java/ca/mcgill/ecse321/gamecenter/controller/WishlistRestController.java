package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.Wishlist.*;
import ca.mcgill.ecse321.gamecenter.model.Wishlist;
import ca.mcgill.ecse321.gamecenter.service.WishlistService;
import ca.mcgill.ecse321.gamecenter.dto.AppUsers.*;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.service.AppUserService;
import ca.mcgill.ecse321.gamecenter.dto.Game.*;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.service.GameCategoryService;
import ca.mcgill.ecse321.gamecenter.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing wishlists.
 */
@RestController
public class WishlistRestController {

    @Autowired
    private WishlistService wishlistService;
    private AppUserService appUserService;
    private GameService gameService;

    @PostMapping(value = "/wishlists/create")
    public WishlistResponseDto createWishlist(@RequestBody WishlistRequestDto request) {
        Wishlist createdWishlist = wishlistService.createWishlist(request.getClientId(), request.getGameId());
        return new WishlistResponseDto(createdWishlist);
    }

    /**
     * Remove a game from a client's wishlist.
     *
     * @param clientId The client ID from whose wishlist the game should be removed.
     * @param gameId The game ID to remove from the wishlist.
     * @return A response indicating the game was removed.
     */
    @DeleteMapping(value = "/wishlists/remove")
    public ResponseEntity<Object> removeGameFromWishlist(@RequestParam int clientId, @RequestParam int gameId) {
        wishlistService.removeWishlist(clientId, gameId);
        return ResponseEntity.noContent().build();  // HTTP 204 No Content
    }

    /**
     * Find a wishlist by its ID.
     *
     * @param wishlistId The wishlist ID to search for.
     * @return The wishlist with the given ID.
     */
    @GetMapping(value = "/wishlists/{wishlistId}")
    public WishlistResponseDto findWishlistById(@PathVariable int wishlistId) {
        Wishlist wishlist = wishlistService.findWishlistById(wishlistId);
        return new WishlistResponseDto(wishlist);
    }

    /**
     * Find all wishlists for a specific client.
     *
     * @param clientId The client ID whose wishlists are to be retrieved.
     * @return A list of all wishlists for the specified client.
     */
    @GetMapping(value = "/wishlists/client/{clientId}")
    public List<WishlistResponseDto> findWishlistsByClientId(@PathVariable int clientId) {
        List<Wishlist> wishlists = wishlistService.findWishlistsByClientId(clientId);
        return wishlists.stream()
                        .map(WishlistResponseDto::new)
                        .collect(Collectors.toList());
    }

    /**
     * Find all wishlists for a specific game.
     *
     * @param gameId The game ID whose wishlists are to be retrieved.
     * @return A list of all wishlists that contain the specified game.
     */
    @GetMapping(value = "/wishlists/game/{gameId}")
    public List<WishlistResponseDto> findWishlistsByGameId(@PathVariable int gameId) {
        List<Wishlist> wishlists = wishlistService.findWishlistsByGameId(gameId);
        return wishlists.stream()
                        .map(WishlistResponseDto::new)
                        .collect(Collectors.toList());
    }

    /**
     * Find a wishlist by a client and game.
     *
     * @param clientId The client ID.
     * @param gameId The game ID.
     * @return The wishlist if it exists, else a 404 error.
     */
    @GetMapping(value = "/wishlists/client/{clientId}/game/{gameId}")
    public WishlistResponseDto findWishlistByClientAndGame(@PathVariable int clientId, @PathVariable int gameId) {
        Wishlist wishlist = wishlistService.findWishlistByClientIdAndGameId(clientId, gameId);
        return new WishlistResponseDto(wishlist);
    }
}
