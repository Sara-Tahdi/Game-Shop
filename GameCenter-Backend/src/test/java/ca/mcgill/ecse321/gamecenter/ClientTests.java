package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.PurchaseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ClientTests {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;
    
    @BeforeEach
    @AfterEach
    public void clear() {
        clientRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadClient() {
        Client client = new Client();
        client.setEmail("progamer@hai.ca");
        client.setPassword("mariobros");
        client.setUsername("Bowser");

        client = clientRepository.save(client);
        assertNotNull(client);
        assertTrue(client.isIsActive());

        int id = client.getId();
        String username = client.getUsername();
        String email = client.getEmail();
        String password = client.getPassword();

        // Get by ID tests
        Client clientFromDb = clientRepository.findClientById(id).orElse(null);
        assertNotNull(clientFromDb);

        assertTrue(clientFromDb.getIsActive());
        assertEquals(id, clientFromDb.getId());
        assertEquals(username, clientFromDb.getUsername());
        assertEquals(email, clientFromDb.getEmail());
        assertEquals(password, clientFromDb.getPassword());

        // Get by username tests
        clientFromDb = clientRepository.findClientByUsername(username).orElse(null);
        assertNotNull(clientFromDb);

        assertTrue(clientFromDb.getIsActive());
        assertEquals(id, clientFromDb.getId());
        assertEquals(username, clientFromDb.getUsername());
        assertEquals(email, clientFromDb.getEmail());
        assertEquals(password, clientFromDb.getPassword());

        // Get by username tests
        clientFromDb = clientRepository.findClientByEmail(email).orElse(null);
        assertNotNull(clientFromDb);

        assertTrue(clientFromDb.getIsActive());
        assertEquals(id, clientFromDb.getId());
        assertEquals(username, clientFromDb.getUsername());
        assertEquals(email, clientFromDb.getEmail());
        assertEquals(password, clientFromDb.getPassword());
    }

    @Test
    public void testRemoveActivationFromClient() {
        Client client = new Client();
        client.setEmail("progamer@hai.ca");
        client.setPassword("mariobros");
        client.setUsername("Bowser");

        client = clientRepository.save(client);
        assertNotNull(client);
        assertTrue(client.isIsActive());

        String username = client.getUsername();

        clientRepository.updateClientByUsername(username);
        Client clientFromDb = clientRepository.findClientByUsername(username).orElse(null);
        assertNotNull(clientFromDb);

        assertFalse(clientFromDb.isIsActive());
    }

    @Test
    public void testGetByUserType() {
        Client client1 = new Client();
        client1.setEmail("progamer@hai.ca");
        client1.setPassword("mariobros");
        client1.setUsername("Bowser");
        client1 = clientRepository.save(client1);
        assertNotNull(client1);

        Client client2 = new Client();
        client2.setEmail("justin@mail.mail");
        client2.setPassword("JustinJus");
        client2.setUsername("IamJustin");
        client2 = clientRepository.save(client2);
        assertNotNull(client2);

        List<Client> client_list = clientRepository.findClientByUserType(Client.class).orElse(null);
        assertNotNull(client_list);
        assertEquals(2, client_list.size());

        for (Client user: client_list) {
            assertTrue(user instanceof Client);
        }
    }

    @Test
    public void testAddGameToWishlist() {
        Client client = new Client();
        client.setEmail("justin@mail.mail");
        client.setPassword("JustinJus");
        client.setUsername("IamJustin");

        Client client2 = new Client();
        client2.setEmail("hhaaha@ahaha.ca");
        client2.setPassword("secure");
        client2.setUsername("LOL");
        
        Game game = new Game();
        game.setTitle("Super Mario Bros");
        game.setIsOffered(true);
        game = gameRepository.save(game);

        Game game2 = new Game();
        game2.setTitle("Mari Kart");
        game2.setIsOffered(true);
        game2 = gameRepository.save(game2);

        client = clientRepository.save(client);
        client2 = clientRepository.save(client2);

        client.addWishlist(game);
        client2.addWishlist(game2);

        client = clientRepository.save(client);
        client2 = clientRepository.save(client2);

        Client clientFromDb = clientRepository.findClientById(client.getId()).orElse(null);
        List<Game> wishlist = clientFromDb.getWishlist();

        assertNotNull(wishlist);
        assertEquals(1, wishlist.size());
        assertEquals(game.getId(), wishlist.get(0).getId());
        assertEquals(game.getTitle(), wishlist.get(0).getTitle());

        Client clientFromDb2 = clientRepository.findClientById(client2.getId()).orElse(null);
        List<Game> wishlist2 = clientFromDb2.getWishlist();

        assertNotNull(wishlist2);
        assertEquals(1, wishlist2.size());
        assertEquals(game2.getId(), wishlist2.get(0).getId());
        assertEquals(game2.getTitle(), wishlist2.get(0).getTitle());

        assertNotEquals(wishlist.get(0).getId(), wishlist2.get(0).getId());
    }

    @Test
    public void testAddGameToCart() {
        Client client = new Client();
        client.setEmail("justin@mail.mail");
        client.setPassword("JustinJus");
        client.setUsername("IamJustin");

        Client client2 = new Client();
        client2.setEmail("hhaaha@ahaha.ca");
        client2.setPassword("secure");
        client2.setUsername("LOL");

        Game game = new Game();
        game.setTitle("Super Mario Bros");
        game.setIsOffered(true);
        game = gameRepository.save(game);

        Game game2 = new Game();
        game2.setTitle("Mari Kart");
        game2.setIsOffered(true);
        game2 = gameRepository.save(game2);

        client = clientRepository.save(client);
        client2 = clientRepository.save(client2);

        client.addCart(game);
        client2.addCart(game2);

        client = clientRepository.save(client);
        client2 = clientRepository.save(client2);

        Client clientFromDb = clientRepository.findClientById(client.getId()).orElse(null);
        List<Game> cart = clientFromDb.getCart();

        assertNotNull(cart);
        assertEquals(1, cart.size());
        assertEquals(game.getId(), cart.get(0).getId());
        assertEquals(game.getTitle(), cart.get(0).getTitle());

        Client clientFromDb2 = clientRepository.findClientById(client2.getId()).orElse(null);
        List<Game> cart2 = clientFromDb2.getCart();

        assertNotNull(cart2);
        assertEquals(1, cart2.size());
        assertEquals(game2.getId(), cart2.get(0).getId());
        assertEquals(game2.getTitle(), cart2.get(0).getTitle());

        assertNotEquals(cart.get(0).getId(), cart2.get(0).getId());
    }

    public void testPurchaseHistory() {
        Game game1 = new Game();
        game1.setIsOffered(true);
        game1.setTitle("Super Mario Bros");
        game1 = gameRepository.save(game1);

        Game game2 = new Game();
        game2.setIsOffered(true);
        game2.setTitle("Mario Kart");
        game2 = gameRepository.save(game2);

        Purchase purchase = new Purchase();
        purchase.setAssociatedGames(game1, game2);
        purchase = purchaseRepository.save(purchase);

        Client client = new Client();
        client.setEmail("progamer@hai.ca");
        client.setPassword("mariobros");
        client.setUsername("Bowser");

        client.addPurchaseHistory(purchase);
        client = clientRepository.save(client);

        Client clientFromDb = clientRepository.findClientById(client.getId()).orElse(null);
        assertNotNull(clientFromDb);

        List<Purchase> purchaseFromClient = clientFromDb.getPurchaseHistory();
        assertNotNull(purchaseFromClient);

        assertEquals(1, purchaseFromClient.size());
        assertEquals(purchase.getId(), purchaseFromClient.get(0).getId());

        List<Game> gameInPurchaseFromClient = purchaseFromClient.get(0).getAssociatedGames();
        assertNotNull(gameInPurchaseFromClient);

        assertEquals(2, gameInPurchaseFromClient.size());
        assertEquals(game1.getId(), gameInPurchaseFromClient.get(0).getId());
        assertEquals(game2.getId(), gameInPurchaseFromClient.get(0).getId());
    }
}
