package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.Wishlist;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.WishlistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WishlistTests {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private WishlistRepository wishlistRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        wishlistRepository.deleteAll();
        clientRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadWishlist() {
        Wishlist wishlist = new Wishlist();
        wishlist = wishlistRepository.save(wishlist);
        assertNotNull(wishlist);

        Wishlist wishlistFromDb = wishlistRepository.findWishlistById(wishlist.getId()).orElse(null);
        assertNotNull(wishlistFromDb);

        assertEquals(wishlist.getId(), wishlistFromDb.getId());
    }

    @Test
    public void testLoadAndFetchWishlistByClientAndGame() {
        Client client = new Client();
        client.setEmail("progamer@hai.ca");
        client.setPassword("mariobros");
        client.setUsername("Bowser");
        client = clientRepository.save(client);
        assertNotNull(client);

        Game game = new Game();
        game.setPrice(Float.parseFloat("13.99"));
        game.setRemainingQuantity(10);
        game = gameRepository.save(game);
        assertNotNull(game);

        Wishlist wishlist = new Wishlist();
        wishlist = wishlistRepository.save(wishlist);
        assertNotNull(wishlist);

        wishlist.setClient(client);
        wishlist.setGame(game);

        wishlist = wishlistRepository.save(wishlist);

        Wishlist wishlistFromDb = wishlistRepository.findWishlistByClientIdAndGameId(client.getId(), game.getId()).orElse(null);
        assertNotNull(wishlistFromDb);

        assertEquals(wishlist.getId(), wishlistFromDb.getId());
        assertEquals(client.getId(), wishlistFromDb.getClient().getId());
        assertEquals(game.getId(), wishlistFromDb.getGame().getId());
    }

    @Test
    public void testLoadAndFetchWishlistsByClient() {
        Game game1 = new Game();
        game1.setPrice(Float.parseFloat("13.99"));
        game1.setRemainingQuantity(10);
        game1 = gameRepository.save(game1);
        assertNotNull(game1);

        Game game2 = new Game();
        game2.setPrice(Float.parseFloat("13.99"));
        game2.setRemainingQuantity(10);
        game2 = gameRepository.save(game2);
        assertNotNull(game2);

        Client client1 = new Client();
        client1.setEmail("progamer@hai.ca");
        client1.setPassword("mariobros");
        client1.setUsername("Bowser");
        client1 = clientRepository.save(client1);
        assertNotNull(client1);

        Client client2 = new Client();
        client2.setEmail("progamer@hai.ca");
        client2.setPassword("mariobros");
        client2.setUsername("Bowser");
        client2 = clientRepository.save(client2);
        assertNotNull(client2);

        Wishlist wishlist1 = new Wishlist(game1, client1);
        Wishlist wishlist2 = new Wishlist(game2, client1);
        Wishlist wishlist3 = new Wishlist(game1, client2);

        wishlist1 = wishlistRepository.save(wishlist1);
        wishlist2 = wishlistRepository.save(wishlist2);
        wishlist3 = wishlistRepository.save(wishlist3);

        List<Wishlist> wishlistsClient1 = wishlistRepository.findWishlistsByClientId(client1.getId()).orElse(null);
        List<Wishlist> wishlistsClient2 = wishlistRepository.findWishlistsByClientId(client2.getId()).orElse(null);
        assertNotNull(wishlistsClient1);
        assertNotNull(wishlistsClient2);

        assertEquals(2, wishlistsClient1.size());
        assertEquals(wishlist1.getId(), wishlistsClient1.get(0).getId());
        assertEquals(wishlist2.getId(), wishlistsClient1.get(1).getId());
        assertEquals(client1.getId(), wishlistsClient1.get(0).getClient().getId());
        assertEquals(client1.getId(), wishlistsClient1.get(1).getClient().getId());

        assertEquals(1, wishlistsClient2.size());
        assertEquals(wishlist3.getId(), wishlistsClient2.getFirst().getId());
        assertEquals(client2.getId(), wishlistsClient2.getFirst().getClient().getId());
    }

    @Test
    public void testLoadAndFetchWishlistsByGame() {
        Game game1 = new Game();
        game1.setPrice(Float.parseFloat("13.99"));
        game1.setRemainingQuantity(10);
        game1 = gameRepository.save(game1);
        assertNotNull(game1);

        Game game2 = new Game();
        game2.setPrice(Float.parseFloat("13.99"));
        game2.setRemainingQuantity(10);
        game2 = gameRepository.save(game2);
        assertNotNull(game2);

        Client client1 = new Client();
        client1.setEmail("progamer@hai.ca");
        client1.setPassword("mariobros");
        client1.setUsername("Bowser");
        client1 = clientRepository.save(client1);
        assertNotNull(client1);

        Client client2 = new Client();
        client2.setEmail("progamer@hai.ca");
        client2.setPassword("mariobros");
        client2.setUsername("Bowser");
        client2 = clientRepository.save(client2);
        assertNotNull(client2);

        Wishlist wishlist1 = new Wishlist(game1, client1);
        Wishlist wishlist2 = new Wishlist(game2, client1);
        Wishlist wishlist3 = new Wishlist(game1, client2);

        wishlist1 = wishlistRepository.save(wishlist1);
        wishlist2 = wishlistRepository.save(wishlist2);
        wishlist3 = wishlistRepository.save(wishlist3);

        List<Wishlist> wishlistsGame1 = wishlistRepository.findWishlistsByGameId(game1.getId()).orElse(null);
        List<Wishlist> wishlistsGame2 = wishlistRepository.findWishlistsByGameId(game2.getId()).orElse(null);
        assertNotNull(wishlistsGame1);
        assertNotNull(wishlistsGame2);

        assertEquals(2, wishlistsGame1.size());
        assertEquals(wishlist1.getId(), wishlistsGame1.get(0).getId());
        assertEquals(wishlist3.getId(), wishlistsGame1.get(1).getId());
        assertEquals(game1.getId(), wishlistsGame1.get(0).getGame().getId());
        assertEquals(game1.getId(), wishlistsGame1.get(1).getGame().getId());

        assertEquals(1, wishlistsGame2.size());
        assertEquals(wishlist2.getId(), wishlistsGame2.getFirst().getId());
        assertEquals(game2.getId(), wishlistsGame2.getFirst().getGame().getId());
    }
}
