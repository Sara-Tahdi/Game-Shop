package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Purchase;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.PurchaseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PurchaseTests {
    @Autowired
    PurchaseRepository purchaseRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        purchaseRepository.deleteAll();
        clientRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    void testPersistAndLoadPurchase() {
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

        Purchase purchase = new Purchase();
        purchase.setTotalPrice(game.getPrice());
        purchase.setCopies(1);
        purchase.setPurchaseDate(Date.valueOf(LocalDate.now()));
        purchase.setTrackingCode("38aojvq9d2");
        purchase = purchaseRepository.save(purchase);
        assertNotNull(purchase);

        purchase.setGame(game);

        purchase = purchaseRepository.save(purchase);
        assertNotNull(purchase);
        purchase.setClient(client);

        client = clientRepository.save(client);
        assertNotNull(client);

        Purchase purchaseFromDb = purchaseRepository.findPurchaseById(purchase.getId()).orElse(null);
        assertNotNull(purchaseFromDb);

        assertEquals(purchase.getId(), purchaseFromDb.getId());
        assertEquals(purchase.getPurchaseDate(), purchaseFromDb.getPurchaseDate());
        assertEquals(purchase.getTrackingCode(), purchaseFromDb.getTrackingCode());
        assertEquals(purchase.getTotalPrice(), purchaseFromDb.getTotalPrice());
        assertEquals(purchase.getCopies(), purchaseFromDb.getCopies());

        Game gameFromPurchaseFromDb = purchaseFromDb.getGame();
        assertEquals(game.getId(), gameFromPurchaseFromDb.getId());
    }
}
