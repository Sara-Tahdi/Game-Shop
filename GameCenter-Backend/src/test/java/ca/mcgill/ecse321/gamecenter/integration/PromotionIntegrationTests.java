package ca.mcgill.ecse321.gamecenter.integration;

import ca.mcgill.ecse321.gamecenter.dto.AppUsers.EmployeeResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Promotion.PromotionRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Promotion.PromotionResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.model.Owner;
import ca.mcgill.ecse321.gamecenter.model.Promotion;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.PromotionRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PromotionIntegrationTests {
    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @Autowired
    private TestRestTemplate client;

    private final float VALID_NEW_PRICE = 59.99F;
    private final Date VALID_START_DATE = Date.valueOf("2024-01-01");
    private final Date VALID_END_DATE = Date.valueOf("2024-12-31");
    private Game VALID_GAME;
    private final Date INVALID_START_DATE = Date.valueOf("2024-12-31");
    private final Date INVALID_END_DATE = Date.valueOf("2024-01-01");
    private final int INVALID_ID = 0;
    private int validId;
    private Game savedGame;
    private GameCategory savedCategory;

    @BeforeAll
    public void setup() {
        GameCategory category = new GameCategory("Platformer");
        savedCategory = gameCategoryRepository.save(category);

        Game game = new Game(
                "Rayman Legends",
                79.99F,
                "A fun platformer!",
                4.5F,
                20,
                true,
                Game.GeneralFeeling.POSITIVE,
                savedCategory
        );
        savedGame = gameRepository.save(game);
        VALID_GAME = savedGame;
    }

    @AfterAll
    public void cleanup() {
        promotionRepository.deleteAll();
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateValidPromotion() {
        PromotionRequestDTO promotion = new PromotionRequestDTO(
                VALID_NEW_PRICE,
                VALID_START_DATE,
                VALID_END_DATE,
                savedGame
        );

        ResponseEntity<PromotionResponseDTO> response = client.postForEntity(
                "/promotions/create",
                promotion,
                PromotionResponseDTO.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PromotionResponseDTO body = response.getBody();
        assertNotNull(body);
        assertTrue(body.getId() > 0, "The ID should be positive");
        assertEquals(VALID_START_DATE, body.getStartDate());
        assertEquals(VALID_END_DATE, body.getEndDate());
    }


    @Test
    @Order(2)
    public void testCreatePromotionInvalidDates() {
        PromotionRequestDTO promotion = new PromotionRequestDTO(VALID_NEW_PRICE, INVALID_START_DATE,
                INVALID_END_DATE, VALID_GAME);

        ResponseEntity<String> response = client.postForEntity("/promotions", promotion, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Start and End Dates are not valid"));
    }

    @Test
    @Order(3)
    public void testCreatePromotionInvalidPrice() {
        PromotionRequestDTO promotion = new PromotionRequestDTO(-10.0F, VALID_START_DATE,
                VALID_END_DATE, VALID_GAME);

        ResponseEntity<String> response = client.postForEntity("/promotions", promotion, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Price is not valid"));
    }

    @Test
    @Order(4)
    public void testGetPromotionById() {
        PromotionRequestDTO promotion = new PromotionRequestDTO(VALID_NEW_PRICE, VALID_START_DATE,
                VALID_END_DATE, VALID_GAME);
        ResponseEntity<PromotionResponseDTO> createResponse = client.postForEntity("/promotions",
                promotion, PromotionResponseDTO.class);
        validId = Objects.requireNonNull(createResponse.getBody()).getId();

        ResponseEntity<PromotionResponseDTO> response = client.getForEntity("/promotions/" + validId,
                PromotionResponseDTO.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PromotionResponseDTO retrievedPromotion = response.getBody();
        assertNotNull(retrievedPromotion);
        assertEquals(validId, retrievedPromotion.getId());
        assertEquals(VALID_NEW_PRICE, retrievedPromotion.getNewPrice());
    }

    @Test
    @Order(5)
    public void testGetPromotionByInvalidId() {
        ResponseEntity<String> response = client.getForEntity("/promotions/" + INVALID_ID, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("There is no Promotion with id: " + INVALID_ID));
    }

    @Test
    @Order(6)
    public void testGetPromotionsByGameId() {
        ResponseEntity<PromotionResponseDTO[]> response = client.getForEntity(
                "/promotions/game/" + VALID_GAME.getId(), PromotionResponseDTO[].class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PromotionResponseDTO[] promotions = response.getBody();
        assertNotNull(promotions);
        assertTrue(promotions.length > 0);
        for (PromotionResponseDTO promotion : promotions) {
            assertEquals(VALID_GAME.getId(), promotion.getGame().getId());
        }
    }

    @Test
    @Order(7)
    public void testUpdatePromotionSuccess() {
        float newPrice = 49.99F;
        Date newStartDate = Date.valueOf(LocalDate.now().plusDays(1));
        Date newEndDate = Date.valueOf(LocalDate.now().plusMonths(1));

        ResponseEntity<PromotionResponseDTO> response = client.exchange(
                "/promotions/update/" + validId + "?newPrice=" + newPrice +
                        "&newStartDate=" + newStartDate + "&newEndDate=" + newEndDate,
                HttpMethod.PUT,
                null,
                PromotionResponseDTO.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PromotionResponseDTO updatedPromotion = response.getBody();
        assertNotNull(updatedPromotion);
        assertEquals(newPrice, updatedPromotion.getNewPrice());
        assertEquals(newStartDate, updatedPromotion.getStartDate());
        assertEquals(newEndDate, updatedPromotion.getEndDate());
    }

    @Test
    @Order(8)
    public void testUpdatePromotionInvalidDates() {
        ResponseEntity<String> response = client.exchange(
                "/promotions/update/" + validId + "?newStartDate=" + INVALID_START_DATE +
                        "&newEndDate=" + INVALID_END_DATE,
                HttpMethod.PUT,
                null,
                String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Start and End Dates are not valid"));
    }

    @Test
    @Order(9)
    public void testUpdatePromotionInvalidPrice() {
        ResponseEntity<String> response = client.exchange(
                "/promotions/update/" + validId + "?newPrice=-10.0",
                HttpMethod.PUT,
                null,
                String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Price is not valid"));
    }

    @Test
    @Order(10)
    public void testUpdatePromotionInvalidId() {
        ResponseEntity<String> response = client.exchange(
                "/promotions/update/" + INVALID_ID + "?newPrice=" + VALID_NEW_PRICE,
                HttpMethod.PUT,
                null,
                String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("There is no Promotion with id: " + INVALID_ID));
    }

}
