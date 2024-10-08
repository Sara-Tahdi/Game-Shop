package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
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

        Game game = new Game();
        game.setTitle("Super Mario Bros");
        game.setIsOffered(true);
        client.addWishlist(game);

        client = clientRepository.save(client);

        Client clientFromDb = clientRepository.findClientById(client.getId()).orElse(null);

        List<Game> wishlist = clientFromDb.getWishlist();

        assertNotNull(wishlist);
        assertEquals(1, wishlist.size());
        assertEquals(game.getId(), wishlist.get(0).getId());
        assertEquals(game.getTitle(), wishlist.get(0).getTitle());
    }
}
