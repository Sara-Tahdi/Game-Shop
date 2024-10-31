package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.model.Purchase;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.PurchaseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress, 0);
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
        float total = purchaseService.round(copies * g.getPrice());
        int trackingCode = 3513;
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
        // Create empty Client --> no id => can't find in DB
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
        Game createdGame = gameService.createGame(title, price, description, generalFeeling, gameCategory);

        int copies = 2;
        float total = purchaseService.round(copies * g.getPrice());
        int trackingCode = 3513;
        Date date = Date.valueOf(LocalDate.now());
        Purchase p = new Purchase(total, copies, trackingCode, date, g, c);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                purchaseService.createPurchase(c.getId(), g.getId(), copies));
        assertEquals("There is no Client with id: " + c.getId(), e.getMessage());
    }
}
