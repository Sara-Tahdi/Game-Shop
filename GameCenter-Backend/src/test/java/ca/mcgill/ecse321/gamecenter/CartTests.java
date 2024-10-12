package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.Cart;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.repository.CartRepository;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CartTests {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        cartRepository.deleteAll();
        clientRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadCart() {
        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        assertNotNull(cart);

        Cart cartFromDb = cartRepository.findCartById(cart.getId()).orElse(null);
        assertNotNull(cartFromDb);

        assertEquals(cart.getId(), cartFromDb.getId());
    }

    @Test
    public void testLoadAndFetchCartByClientAndGame() {
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

        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        assertNotNull(cart);

        cart.setClient(client);
        cart.setGame(game);

        cart = cartRepository.save(cart);

        Cart cartFromDb = cartRepository.findCartByClientIdAndGameId(client.getId(), game.getId()).orElse(null);
        assertNotNull(cartFromDb);

        assertEquals(cart.getId(), cartFromDb.getId());
        assertEquals(client.getId(), cartFromDb.getClient().getId());
        assertEquals(game.getId(), cartFromDb.getGame().getId());
    }

    @Test
    public void testLoadAndFetchCartsByClient() {
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

        Cart cart1 = new Cart(game1, client1);
        Cart cart2 = new Cart(game2, client1);
        Cart cart3 = new Cart(game1, client2);

        cart1 = cartRepository.save(cart1);
        cart2 = cartRepository.save(cart2);
        cart3 = cartRepository.save(cart3);

        List<Cart> cartsClient1 = cartRepository.findCartsByClientId(client1.getId()).orElse(null);
        List<Cart> cartsClient2 = cartRepository.findCartsByClientId(client2.getId()).orElse(null);
        assertNotNull(cartsClient1);
        assertNotNull(cartsClient2);

        assertEquals(2, cartsClient1.size());
        assertEquals(cart1.getId(), cartsClient1.get(0).getId());
        assertEquals(cart2.getId(), cartsClient1.get(1).getId());
        assertEquals(client1.getId(), cartsClient1.get(0).getClient().getId());
        assertEquals(client1.getId(), cartsClient1.get(1).getClient().getId());

        assertEquals(1, cartsClient2.size());
        assertEquals(cart3.getId(), cartsClient2.getFirst().getId());
        assertEquals(client2.getId(), cartsClient2.getFirst().getClient().getId());
    }

    @Test
    public void testLoadAndFetchCartsByGame() {
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

        Cart cart1 = new Cart(game1, client1);
        Cart cart2 = new Cart(game2, client1);
        Cart cart3 = new Cart(game1, client2);

        cart1 = cartRepository.save(cart1);
        cart2 = cartRepository.save(cart2);
        cart3 = cartRepository.save(cart3);

        List<Cart> cartsGame1 = cartRepository.findCartsByGameId(game1.getId()).orElse(null);
        List<Cart> cartsGame2 = cartRepository.findCartsByGameId(game2.getId()).orElse(null);
        assertNotNull(cartsGame1);
        assertNotNull(cartsGame2);

        assertEquals(2, cartsGame1.size());
        assertEquals(cart1.getId(), cartsGame1.get(0).getId());
        assertEquals(cart3.getId(), cartsGame1.get(1).getId());
        assertEquals(game1.getId(), cartsGame1.get(0).getGame().getId());
        assertEquals(game1.getId(), cartsGame1.get(1).getGame().getId());

        assertEquals(1, cartsGame2.size());
        assertEquals(cart2.getId(), cartsGame2.getFirst().getId());
        assertEquals(game2.getId(), cartsGame2.getFirst().getGame().getId());
    }
}
