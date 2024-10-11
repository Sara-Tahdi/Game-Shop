package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameTests {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private GameRequestRepository gameRequestRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach

    @AfterEach
    public void clear() {
        purchaseRepository.deleteAll();
        wishlistRepository.deleteAll();
        cartRepository.deleteAll();
        clientRepository.deleteAll();
        reviewRepository.deleteAll();
        gameRequestRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    void testPersistAndLoadGame(){
        Game game = new Game();
        game.setTitle("Super Mario Bros");
        game.setPrice(49.99f);
        game.setDescription("Wa-hoo!");
        game.setRating(4.9f);
        game.setRemainingQuantity(50);
        game.setIsOffered(true);
        game.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
        game = gameRepository.save(game);
        assertNotNull(game);
        assertTrue(game.isIsOffered());

        int id = game.getId();
        String title = game.getTitle();
        float price = game.getPrice();
        String description = game.getDescription();
        float rating = game.getRating();
        int remainingQuantity = game.getRemainingQuantity();
        boolean isOffered = game.isIsOffered();
        Game.GeneralFeeling publicOpinion = game.getPublicOpinion();

        Game gameFromDb = gameRepository.findGameById(id).orElse(null);
        assertNotNull(gameFromDb);

        assertEquals(id, gameFromDb.getId());
        assertEquals(title, gameFromDb.getTitle());
        assertEquals(price, gameFromDb.getPrice());
        assertEquals(description, gameFromDb.getDescription());
        assertEquals(rating, gameFromDb.getRating());
        assertEquals(remainingQuantity, gameFromDb.getRemainingQuantity());
        assertEquals(isOffered, gameFromDb.isIsOffered());
        assertEquals(publicOpinion, gameFromDb.getPublicOpinion());
    }

    @Test
    void testLoadAndFetchGameByTitle(){
        Game game = new Game();
        game.setTitle("Super Mario Bros");
        game.setPrice(49.99f);
        game.setDescription("Wa-hoo!");
        game.setRating(4.9f);
        game.setRemainingQuantity(50);
        game.setIsOffered(true);
        game.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
        game = gameRepository.save(game);
        assertNotNull(game);
        assertTrue(game.isIsOffered());

        // Get by title tests
        Game gameFromDb = gameRepository.findGameByTitle(game.getTitle()).orElse(null);
        assertNotNull(gameFromDb);

        assertTrue(gameFromDb.isIsOffered());
        assertEquals(game.getTitle(), gameFromDb.getTitle());
    }

    @Test
    void testLoadAndFetchGameByDescription(){
        Game game = new Game();
        game.setTitle("Super Mario Bros");
        game.setPrice(49.99f);
        game.setDescription("Wa-hoo!");
        game.setRating(4.9f);
        game.setRemainingQuantity(50);
        game.setIsOffered(true);
        game.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
        game = gameRepository.save(game);
        assertNotNull(game);
        assertTrue(game.isIsOffered());

        // Get by description tests
        Game gameFromDb = gameRepository.findGameByDescription(game.getDescription()).orElse(null);
        assertNotNull(gameFromDb);

        assertTrue(gameFromDb.isIsOffered());
        assertEquals(game.getDescription(), gameFromDb.getDescription());
    }

    @Test
    void testUpdateGameByQuantity(){
        Game game = new Game();
        game.setTitle("Super Mario Bros");
        game.setPrice(49.99f);
        game.setDescription("Wa-hoo!");
        game.setRating(4.9f);
        game.setRemainingQuantity(50);
        game.setIsOffered(true);
        game.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
        game = gameRepository.save(game);
        assertNotNull(game);
        assertTrue(game.isIsOffered());

        // Update quantity tests
        gameRepository.updateGameByQuantity(game.getId(), 40);
        Game gameFromDb = gameRepository.findGameById(game.getId()).orElse(null);
        assertNotNull(gameFromDb);

        assertTrue(gameFromDb.isIsOffered());
        assertNotEquals(game.getRemainingQuantity(), gameFromDb.getRemainingQuantity());
    }

//    @Test
//    void testLoadAndFetchGameByPurchase(){
//        Game game = new Game();
//        game.setTitle("Super Mario Bros");
//        game.setPrice(49.99f);
//        game.setDescription("Wa-hoo!");
//        game.setRating(4.9f);
//        game.setRemainingQuantity(50);
//        game.setIsOffered(true);
//        game.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
//        game = gameRepository.save(game);
//        assertNotNull(game);
//        assertTrue(game.isIsOffered());
//
//        Purchase purchase = new Purchase();
//        purchase.setGame(game);
//        purchase = purchaseRepository.save(purchase);
//
//        // Get by purchase tests
//        Game gameFromDb = gameRepository.findGameByPurchase(purchase).orElse(null);
//        assertNotNull(gameFromDb);
//
//        assertEquals(game, gameFromDb);
//        assertTrue(gameFromDb.isIsOffered());
//    }
//
//    @Test
//    void testLoadAndFetchGameByCart(){
//        Game game = new Game();
//        game.setTitle("Super Mario Bros");
//        game.setPrice(49.99f);
//        game.setDescription("Wa-hoo!");
//        game.setRating(4.9f);
//        game.setRemainingQuantity(50);
//        game.setIsOffered(true);
//        game.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
//        game = gameRepository.save(game);
//        assertNotNull(game);
//        assertTrue(game.isIsOffered());
//
//        Client client = new Client();
//        client.setEmail("client@mail.com");
//        client.setPassword("password");
//        client.setUsername("username");
//        client = clientRepository.save(client);
//
//        Cart cart = new Cart();
//        cart.setClient(client);
//        cart.setGame(game);
//        cart = cartRepository.save(cart);
//
//        // Get by cart tests
//        Game gameFromDb = gameRepository.findGameByCart(cart).orElse(null);
//        assertNotNull(gameFromDb);
//
//        assertEquals(game, gameFromDb);
//        assertTrue(gameFromDb.isIsOffered());
//    }
//
//    @Test
//    void testLoadAndFetchGameByGameRequest(){
//        Game game = new Game();
//        game.setTitle("Super Mario Bros");
//        game.setPrice(49.99f);
//        game.setDescription("Wa-hoo!");
//        game.setRating(4.9f);
//        game.setRemainingQuantity(50);
//        game.setIsOffered(true);
//        game.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
//        game = gameRepository.save(game);
//        assertNotNull(game);
//        assertTrue(game.isIsOffered());
//
//
//        GameRequest gameRequest = new GameRequest();
//        gameRequest.setGame(game);
//        gameRequest = gameRequestRepository.save(gameRequest);
//
//        // Get by game request tests
//        Game gameFromDb = gameRepository.findGameByGameRequest(gameRequest).orElse(null);
//        assertNotNull(gameFromDb);
//
//        assertEquals(game, gameFromDb);
//    }
//
//    @Test
//    void testLoadAndFetchGameByWishlist(){
//        Game game = new Game();
//        game.setTitle("Super Mario Bros");
//        game.setPrice(49.99f);
//        game.setDescription("Wa-hoo!");
//        game.setRating(4.9f);
//        game.setRemainingQuantity(50);
//        game.setIsOffered(true);
//        game.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
//        game = gameRepository.save(game);
//        assertNotNull(game);
//        assertTrue(game.isIsOffered());
//
//        Client client = new Client();
//        client.setEmail("client@mail.com");
//        client.setPassword("password");
//        client.setUsername("username");
//        client = clientRepository.save(client);
//
//        Wishlist wishlist = new Wishlist();
//        wishlist.setClient(client);
//        wishlist.setGame(game);
//        wishlist = wishlistRepository.save(wishlist);
//
//        // Get by wishlist tests
//        Game gameFromDb = gameRepository.findGameByWishlist(wishlist).orElse(null);
//        assertNotNull(gameFromDb);
//
//        assertEquals(game, gameFromDb);
//    }
//
//    @Test
//    void testLoadAndFetchByReview(){
//
//        Review review = new Review();
//        review = reviewRepository.save(review);
//
//        Game game = new Game();
//        game.setTitle("Super Mario Bros");
//        game.setPrice(49.99f);
//        game.setDescription("Wa-hoo!");
//        game.setRating(4.9f);
//        game.setRemainingQuantity(50);
//        game.setIsOffered(true);
//        game.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
//        game.addReview(review);
//        game = gameRepository.save(game);
//        assertNotNull(game);
//
//        Game gameFromDb = gameRepository.findGameByReviewId(review.getId()).orElse(null);
//        assertNotNull(gameFromDb);
//
//        assertEquals(game, gameFromDb);
//    }
}
