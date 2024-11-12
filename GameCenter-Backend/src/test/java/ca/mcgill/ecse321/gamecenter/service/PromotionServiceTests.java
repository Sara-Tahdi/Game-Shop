package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.model.Promotion;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.PromotionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PromotionServiceTests {
    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private PromotionService promotionService;

    @Test
    void testCreatePromotion() {
        Float newPrice = 59.99F;
        Date startDate = Date.valueOf("2024-01-01");
        Date endDate = Date.valueOf("2024-12-31");
        Game game = new Game("Rayman Legends", 79.99F, "A fun platformer!", 4.5F, 20, true,
                Game.GeneralFeeling.POSITIVE, new GameCategory("Platformer"));
        game.setId(23);

        when(gameRepository.findGameById(game.getId())).thenReturn(Optional.of(game));

        Promotion expectedPromotion = new Promotion(newPrice, startDate, endDate, game);
        expectedPromotion.setId(1);
        when(promotionRepository.save(any(Promotion.class))).thenReturn(expectedPromotion);

        Promotion createdPromotion = promotionService.createPromotion(newPrice, startDate.toLocalDate(), endDate.toLocalDate(), game);

        assertNotNull(createdPromotion);
        assertEquals(1, createdPromotion.getId());
        assertEquals(newPrice, createdPromotion.getNewPrice());
        assertEquals(startDate, createdPromotion.getStartDate());
        assertEquals(endDate, createdPromotion.getEndDate());
        assertEquals(game.getId(), createdPromotion.getGame().getId());
    }

    @Test
    void testCreatePromotionInvalidPrice() {
        Date startDate = Date.valueOf("2024-01-01");
        Date endDate = Date.valueOf("2024-12-31");
        Game game = new Game();
        game.setId(23);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                promotionService.createPromotion(-10.0F, startDate.toLocalDate(), endDate.toLocalDate(), game));
        assertEquals("New price is not valid", e.getMessage());
    }

    @Test
    void testCreatePromotionInvalidDates() {
        Float newPrice = 59.99F;
        Date startDate = Date.valueOf("2024-12-31");
        Date endDate = Date.valueOf("2024-01-01");
        Game game = new Game();
        game.setId(23);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                promotionService.createPromotion(newPrice, startDate.toLocalDate(), endDate.toLocalDate(), game));
        assertEquals("Start and End Dates are not valid", e.getMessage());
    }

    @Test
    void testCreatePromotionNonExistentGame() {
        Float newPrice = 59.99F;
        Date startDate = Date.valueOf("2024-01-01");
        Date endDate = Date.valueOf("2024-12-31");
        Game game = new Game();
        game.setId(999);

        when(gameRepository.findGameById(999)).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                promotionService.createPromotion(newPrice, startDate.toLocalDate(), endDate.toLocalDate(), game));
        assertEquals("Game does not exist in the database", e.getMessage());
    }

    @Test
    void testGetPromotionById() {
        int id = 1;
        Float price = 59.99F;
        Date startDate = Date.valueOf("2024-01-01");
        Date endDate = Date.valueOf("2024-12-31");
        Game game = new Game("Rayman Legends", 79.99F, "A fun platformer!", 4.5F, 20, true,
                Game.GeneralFeeling.POSITIVE, new GameCategory("Platformer"));
        game.setId(23);

        Promotion promotion = new Promotion(price, startDate, endDate, game);
        promotion.setId(id);

        when(promotionRepository.findPromotionById(id)).thenReturn(Optional.of(promotion));

        Promotion foundPromotion = promotionService.getPromotionById(id);

        assertNotNull(foundPromotion);
        assertEquals(id, foundPromotion.getId());
        assertEquals(price, foundPromotion.getNewPrice());
        assertEquals(startDate, foundPromotion.getStartDate());
        assertEquals(endDate, foundPromotion.getEndDate());
        assertEquals(game.getId(), foundPromotion.getGame().getId());
    }

    @Test
    void testGetPromotionByIdFail() {
        when(promotionRepository.findPromotionById(999)).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                promotionService.getPromotionById(999));
        assertEquals("There is no Promotion with id: 999", e.getMessage());
    }

    @Test
    void testGetPromotionByGameId() {
        Game game = new Game("Rayman Legends", 79.99F, "A fun platformer!", 4.5F, 20, true,
                Game.GeneralFeeling.POSITIVE, new GameCategory("Platformer"));
        game.setId(23);

        List<Promotion> promotions = new ArrayList<>();
        promotions.add(new Promotion(59.99F, Date.valueOf("2024-01-01"),
                Date.valueOf("2024-06-30"), game));
        promotions.add(new Promotion(49.99F, Date.valueOf("2024-07-01"),
                Date.valueOf("2024-12-31"), game));

        when(promotionRepository.findPromotionsByGameId(23)).thenReturn(Optional.of(promotions));

        List<Promotion> foundPromotions = promotionService.getPromotionByGameId(23);

        assertNotNull(foundPromotions);
        assertEquals(2, foundPromotions.size());
        assertEquals(59.99F, foundPromotions.get(0).getNewPrice());
        assertEquals(49.99F, foundPromotions.get(1).getNewPrice());
    }

    @Test
    void testGetPromotionByGameIdFail() {
        when(promotionRepository.findPromotionsByGameId(1290483579)).thenReturn(Optional.empty());
        assertNull(promotionService.getPromotionByGameId(1290483579));
    }

    @Test
    void testUpdatePromotionSuccess() {
        int id = 23;
        float oldPrice = 79.99F;
        LocalDate oldStartDate = Date.valueOf("2024-01-01").toLocalDate();
        LocalDate oldEndDate = Date.valueOf("2024-12-31").toLocalDate();
        Game oldGame = new Game("Rayman Legends", 79.99F, "A fun platformer!", 4.5F, 20, true,
                Game.GeneralFeeling.POSITIVE, new GameCategory("Platformer"));
        oldGame.setId(23);

        Promotion originalPromotion = new Promotion(oldPrice, Date.valueOf(oldStartDate), Date.valueOf(oldEndDate), oldGame);
        originalPromotion.setId(id);

        Float newPrice = 69.99F;
        LocalDate newStartDate = Date.valueOf("2024-02-01").toLocalDate();
        LocalDate newEndDate = Date.valueOf("2024-11-30").toLocalDate();
        Game newGame = new Game("Mario Odyssey", 89.99F, "A 3D platformer!", 4.9F, 15, true,
                Game.GeneralFeeling.POSITIVE, new GameCategory("3D Platformer"));
        newGame.setId(24);

        when(promotionRepository.findPromotionById(id)).thenReturn(Optional.of(originalPromotion));
        when(gameRepository.findGameById(newGame.getId())).thenReturn(Optional.of(newGame));
        when(promotionRepository.save(any(Promotion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Promotion updatedPromotion = promotionService.updatePromotion(id, newPrice, newStartDate, newEndDate, newGame);

        assertNotNull(updatedPromotion);
        assertEquals(id, updatedPromotion.getId());
        assertEquals(newPrice, updatedPromotion.getNewPrice());
        assertEquals(newStartDate, updatedPromotion.getStartDate().toLocalDate());
        assertEquals(newEndDate, updatedPromotion.getEndDate().toLocalDate());
        assertEquals(newGame.getId(), updatedPromotion.getGame().getId());
    }

    @Test
    void testUpdatePromotionPartialUpdate() {
        int id = 23;
        float oldPrice = 79.99F;
        Date oldStartDate = Date.valueOf("2024-01-01");
        Date oldEndDate = Date.valueOf("2024-12-31");
        Game oldGame = new Game("Rayman Legends", 79.99F, "A fun platformer!", 4.5F, 20, true,
                Game.GeneralFeeling.POSITIVE, new GameCategory("Platformer"));
        oldGame.setId(23);

        Promotion promotion = new Promotion(oldPrice, oldStartDate, oldEndDate, oldGame);
        promotion.setId(id);

        when(promotionRepository.findPromotionById(id)).thenReturn(Optional.of(promotion));
        when(promotionRepository.save(any(Promotion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Promotion updatedPromotion = promotionService.updatePromotion(id, 69.99F, null, null, null);
        assertEquals(69.99F, updatedPromotion.getNewPrice());
        assertEquals(oldStartDate, updatedPromotion.getStartDate());
        assertEquals(oldEndDate, updatedPromotion.getEndDate());
        assertEquals(oldGame.getId(), updatedPromotion.getGame().getId());
    }

    @Test
    void testUpdatePromotionInvalidPrice() {
        int id = 23;
        Promotion promotion = new Promotion(79.99F, Date.valueOf("2024-01-01"),
                Date.valueOf("2024-12-31"), new Game());
        promotion.setId(id);

        when(promotionRepository.findPromotionById(id)).thenReturn(Optional.of(promotion));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                promotionService.updatePromotion(id, -69.99F, null, null, null));
        assertEquals("Price must be positive", e.getMessage());
    }

    @Test
    void testUpdatePromotionInvalidDates() {
        int id = 23;
        Promotion promotion = new Promotion(79.99F, Date.valueOf("2024-01-01"),
                Date.valueOf("2024-12-31"), new Game());
        promotion.setId(id);

        when(promotionRepository.findPromotionById(id)).thenReturn(Optional.of(promotion));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                promotionService.updatePromotion(id, null, Date.valueOf("2024-12-31").toLocalDate(),
                        Date.valueOf("2024-01-01").toLocalDate(), null));
        assertEquals("Start date must be before end date", e.getMessage());
    }

    @Test
    void testUpdatePromotionNonExistentGame() {
        int id = 23;
        Promotion promotion = new Promotion(79.99F, Date.valueOf("2024-01-01"),
                Date.valueOf("2024-12-31"), new Game());
        promotion.setId(id);

        Game newGame = new Game();
        newGame.setId(999);

        when(promotionRepository.findPromotionById(id)).thenReturn(Optional.of(promotion));
        when(gameRepository.findGameById(999)).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                promotionService.updatePromotion(id, null, null, null, newGame));
        assertEquals("Game not found with id: 999", e.getMessage());
    }
}