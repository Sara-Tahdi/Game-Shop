package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.PaymentInfoRepository;
import ca.mcgill.ecse321.gamecenter.repository.PurchaseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
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
    private PaymentInfoRepository paymentInfoRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;
    
    @BeforeEach
    @AfterEach
    public void clear() {
        clientRepository.deleteAll();
        gameRepository.deleteAll();
        paymentInfoRepository.deleteAll();
        purchaseRepository.deleteAll();
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
            assertInstanceOf(Client.class, user);
        }
    }

    @Test
    public void testAddAndGetPaymentInfo() {
        Client client = new Client();
        client.setEmail("progamer@hai.ca");
        client.setPassword("mariobros");
        client.setUsername("Bowser");
        client = clientRepository.save(client);
        assertNotNull(client);

        PaymentInfo masterCard = new PaymentInfo();
        masterCard.setCardNumber("1622249210243832");
        masterCard.setCvv(942);
        masterCard.setExpiryMonth(11);
        masterCard.setExpiryYear(2026);
        masterCard = paymentInfoRepository.save(masterCard);
        assertNotNull(masterCard);

        PaymentInfo visa = new PaymentInfo();
        visa.setCardNumber("7295710471937431");
        visa.setCvv(135);
        visa.setExpiryMonth(4);
        visa.setExpiryYear(2027);
        visa = paymentInfoRepository.save(visa);
        assertNotNull(visa);

        client.addPaymentInformation(masterCard);

        client = clientRepository.save(client);

        Client clientFromDb = clientRepository.findClientById(client.getId()).orElse(null);
        assertNotNull(clientFromDb);
        List<PaymentInfo> infos = clientFromDb.getPaymentInformations();
        assertNotNull(infos);

        assertEquals(masterCard.getId(), infos.getFirst().getId());
        assertNotEquals(visa.getId(), infos.getFirst().getId());
    }

    @Test
    public void testAddAndGetPurchaseHistory() {
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
        purchase.setTrackingCode(3513531);
        purchase = purchaseRepository.save(purchase);
        assertNotNull(purchase);

        purchase.setGame(game);

        purchase = purchaseRepository.save(purchase);
        assertNotNull(purchase);

        client.addPurchaseHistory(purchase);

        client = clientRepository.save(client);
        assertNotNull(client);

        Client clientFromDb = clientRepository.findClientById(client.getId()).orElse(null);
        assertNotNull(clientFromDb);

        List<Purchase> purchaseFromClientFromDb = clientFromDb.getPurchaseHistory();

        assertEquals(1, purchaseFromClientFromDb.size());
        assertEquals(purchase.getId(), purchaseFromClientFromDb.getFirst().getId());

        Game gameFromPurchaseFromClientFromDb = purchaseFromClientFromDb.getFirst().getGame();

        assertEquals(game.getId(), gameFromPurchaseFromClientFromDb.getId());
    }
}
