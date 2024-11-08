package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.model.Purchase;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.PurchaseRepository;
import ca.mcgill.ecse321.gamecenter.utilities.Round;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PurchaseServiceTests {
    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameCategoryRepository gameCategoryRepository;

    @InjectMocks
    private PurchaseService purchaseService;
    @InjectMocks
    private AppUserService appUserService;
    @InjectMocks
    private GameService gameService;
    @InjectMocks
    private GameCategoryService gameCategoryService;

    @Test
    public void testCreateValidPurchase() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        c.setId(21);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));
        Client createdClient = appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String category = "Adventure";
        GameCategory gameCategory = new GameCategory(category);
        when(gameCategoryRepository.save(any(GameCategory.class))).thenReturn(gameCategory);
        GameCategory savedCategory = gameCategoryService.createGameCategory(category);

        String title = "Minecraft";
        float price = 26.99f;
        String description = "block game";
        Game.GeneralFeeling generalFeeling = Game.GeneralFeeling.VERYPOSITIVE;
        Game g = new Game(title, price, description, 0, 0, false, generalFeeling, savedCategory);
        g.setId(513);
        g.setRemainingQuantity(10);
        when(gameRepository.save(any(Game.class))).thenReturn(g);
        when(gameRepository.findGameById(g.getId())).thenReturn(Optional.of(g));
        Game createdGame = gameService.createGame(title, price, description, generalFeeling, gameCategory);

        int copies = 2;
        float total = Round.round(copies * g.getPrice());
        String trackingCode = "a3451n14m2";
        Date date = Date.valueOf(LocalDate.now());
        Purchase p = new Purchase(total, copies, trackingCode, date, g, c);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(p);
        Purchase createdPurchase = purchaseService.createPurchase(c.getId(), g.getId(), 2);

        assertEquals(createdClient.getId(), createdPurchase.getClient().getId());
        assertEquals(createdGame.getId(), createdPurchase.getGame().getId());
        assertEquals(total, createdPurchase.getTotalPrice());
        assertEquals(8, createdPurchase.getGame().getRemainingQuantity());
    }

    @Test
    public void testCreatePurchaseNoClient() {
        Client c = new Client();
        c.setId(1);

        String category = "Adventure";
        GameCategory gameCategory = new GameCategory(category);
        when(gameCategoryRepository.save(any(GameCategory.class))).thenReturn(gameCategory);
        GameCategory savedCategory = gameCategoryService.createGameCategory(category);

        String title = "Minecraft";
        float price = 26.99f;
        String description = "block game";
        Game.GeneralFeeling generalFeeling = Game.GeneralFeeling.VERYPOSITIVE;
        Game g = new Game(title, price, description, 0, 0, false, generalFeeling, savedCategory);
        g.setId(513);
        g.setRemainingQuantity(10);
        when(gameRepository.save(any(Game.class))).thenReturn(g);
        when(gameRepository.findGameById(g.getId())).thenReturn(Optional.of(g));
        gameService.createGame(title, price, description, generalFeeling, gameCategory);

        int copies = 2;

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                purchaseService.createPurchase(c.getId(), g.getId(), copies));
        assertEquals("There is no Client with id: " + c.getId(), e.getMessage());
    }

    @Test
    public void testCreatePurchaseInvalidNoGame() {
        Client c = new Client();
        c.setId(34);
        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));

        Game g = new Game();
        g.setId(2);

        int copies = 34;

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                purchaseService.createPurchase(c.getId(), g.getId(), copies));
        assertEquals("There is no Game with id: " + g.getId(), e.getMessage());
    }

    @Test
    public void testCreatePurchaseInvalidTooManyCopies() {
        Client c = new Client();
        c.setId(34);
        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));

        Game g = new Game();
        g.setRemainingQuantity(3);
        g.setId(2);
        when(gameRepository.findGameById(g.getId())).thenReturn(Optional.of(g));

        int tooManyCopies = 1351;

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                purchaseService.createPurchase(c.getId(), g.getId(), tooManyCopies));
        assertEquals("Attempting to purchase more copies than available, copies left: " + g.getRemainingQuantity(), e.getMessage());
    }

    @Test
    public void testReturnPurchaseValid() {
        Client c = new Client();
        c.setId(53);

        Game g = new Game();
        g.setId(81);
        g.setRemainingQuantity(813);

        Purchase p = new Purchase();
        p.setId(31);
        p.setPurchaseDate(Date.valueOf(LocalDate.now().minusDays(3)));
        when(purchaseRepository.findPurchaseById(p.getId())).thenReturn(Optional.of(p));
        Purchase refundedPurchase = new Purchase();
        refundedPurchase.setId(p.getId());
        refundedPurchase.setPurchaseDate(p.getPurchaseDate());
        String refundReason = "I don't like this game anymore!";
        refundedPurchase.setRefundReason(refundReason);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(refundedPurchase);

        Purchase updated = purchaseService.returnGame(p.getId(), refundReason);

        // check if properly returned
        assertEquals(refundReason, updated.getRefundReason());
    }

    @Test
    public void testRefundPurchaseInvalidNoPurchase() {
        String refundReason = "I don't like this game anymore!";

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                purchaseService.returnGame(3, refundReason));
        assertEquals("There is no Purchase with id: " + 3, e.getMessage());
    }

    @Test
    public void testRefundPurchaseInvalidTooLate() {
        Purchase p = new Purchase();
        p.setId(51);
        p.setPurchaseDate(Date.valueOf(LocalDate.now().minusDays(8)));
        when(purchaseRepository.findPurchaseById(p.getId())).thenReturn(Optional.of(p));

        String refundReason = "I don't like this game anymore!";

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                purchaseService.returnGame(p.getId(), refundReason));
        assertEquals("Refund request DENIED!! Refund period is over.", e.getMessage());
    }

    @Test
    public void testGetClientPurchaseHistoryValid() {
        Client c = new Client();
        c.setId(53);
        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));

        Game g1 = new Game();
        g1.setId(3);
        g1.setPrice(13.99f);
        g1.setRemainingQuantity(31);
        when(gameRepository.findGameById(g1.getId())).thenReturn(Optional.of(g1));

        Game g2 = new Game();
        g2.setId(9);
        g2.setPrice(8.99f);
        g2.setRemainingQuantity(7);
        when(gameRepository.findGameById(g2.getId())).thenReturn(Optional.of(g2));

        int copies1 = 3;
        float total1 = Round.round(g1.getPrice() * copies1);
        String trackingNumber1 = "a3451n14m2";
        Purchase p1 = new Purchase(total1, copies1, trackingNumber1, Date.valueOf(LocalDate.now()), g1, c);
        p1.setId(7);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(p1);
        purchaseService.createPurchase(c.getId(), g1.getId(), copies1);

        int copies2 = 2;
        float total2 = Round.round(g2.getPrice() * copies2);
        String trackingNumber2 = "38aojvq9d2";
        Purchase p2 = new Purchase(total2, copies2, trackingNumber2, Date.valueOf(LocalDate.now().minusDays(100)), g2, c);
        p2.setId(47381);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(p2);
        purchaseService.createPurchase(c.getId(), g2.getId(), copies2);

        when(purchaseRepository.findPurchasesByClientId(c.getId())).thenReturn(Optional.of(List.of(p1, p2)));
        List<Purchase> purchases = purchaseService.getClientPurchaseHistory(c.getId());
        assertNotNull(purchases);

        assertEquals(2, purchases.size());
        assertTrue(purchases.getFirst().getPurchaseDate().toLocalDate().isAfter(purchases.getLast().getPurchaseDate().toLocalDate()));
        assertEquals(p1.getId(), purchases.getFirst().getId());
        assertEquals(p2.getId(), purchases.getLast().getId());
    }

    @Test
    public void testGetClientPurchaseHistoryNoClient() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                purchaseService.getClientPurchaseHistory(3));
        assertEquals("There is no Client with id: " + 3, e.getMessage());
    }

    @Test
    public void testGetClientPurchaseHistoryNoPurchases() {
        Client c = new Client();
        c.setId(4314);

        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                purchaseService.getClientPurchaseHistory(c.getId()));
        assertEquals("Client with id " + c.getId() + " has no purchases", e.getMessage());
    }

    @Test
    public void testGetClientPurchaseLast90Days() {
        Client c = new Client();
        c.setId(53);
        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));

        Game g1 = new Game();
        g1.setId(3);
        g1.setPrice(13.99f);
        g1.setRemainingQuantity(31);
        when(gameRepository.findGameById(g1.getId())).thenReturn(Optional.of(g1));

        Game g2 = new Game();
        g2.setId(9);
        g2.setPrice(8.99f);
        g2.setRemainingQuantity(7);
        when(gameRepository.findGameById(g2.getId())).thenReturn(Optional.of(g2));

        int copies1 = 3;
        float total1 = Round.round(g1.getPrice() * copies1);
        String trackingNumber1 = "a3451n14m2";
        Purchase p1 = new Purchase(total1, copies1, trackingNumber1, Date.valueOf(LocalDate.now()), g1, c);
        p1.setId(7);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(p1);
        purchaseService.createPurchase(c.getId(), g1.getId(), copies1);

        int copies2 = 2;
        float total2 = Round.round(g2.getPrice() * copies2);
        String trackingNumber2 = "38aojvq9d2";
        Purchase p2 = new Purchase(total2, copies2, trackingNumber2, Date.valueOf(LocalDate.now().minusDays(100)), g2, c);
        p2.setId(47381);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(p2);
        purchaseService.createPurchase(c.getId(), g2.getId(), copies2);

        when(purchaseRepository.findPurchasesByClientId(c.getId())).thenReturn(Optional.of(List.of(p1, p2)));
        List<Purchase> purchases = purchaseService.getClientPurchaseHistory90Days(c.getId());
        assertNotNull(purchases);

        assertEquals(1, purchases.size());
        assertEquals(p1.getId(), purchases.getFirst().getId());
    }
}
