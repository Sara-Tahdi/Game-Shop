package ca.mcgill.ecse321.gamecenter.integration;

import ca.mcgill.ecse321.gamecenter.dto.Promotion.PromotionRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Promotion.PromotionResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.PromotionRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    private final LocalDate VALID_START_DATE = LocalDate.of(2024, 1, 1);
    private final LocalDate VALID_END_DATE = LocalDate.of(2024, 12, 31);
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
                savedGame.getId()
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
    public void testGetPromotionById() {
        PromotionRequestDTO promotion = new PromotionRequestDTO(VALID_NEW_PRICE, VALID_START_DATE,
                VALID_END_DATE, savedGame.getId());
        ResponseEntity<PromotionResponseDTO> createResponse = client.postForEntity("/promotions/create",
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
    @Order(3)
    public void testGetPromotionsByGameId() {
        PromotionRequestDTO promotionRequest = new PromotionRequestDTO(VALID_NEW_PRICE, VALID_START_DATE,
                VALID_END_DATE, savedGame.getId());
        client.postForEntity("/promotions/create", promotionRequest, PromotionResponseDTO.class);

        ResponseEntity<PromotionResponseDTO[]> response = client.getForEntity(
                "/promotions/game/" + savedGame.getId(),
                PromotionResponseDTO[].class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PromotionResponseDTO[] promotions = response.getBody();
        assertNotNull(promotions);
        assertTrue(promotions.length > 0);
        for (PromotionResponseDTO promotion : promotions) {
            assertEquals(savedGame.getId(), promotion.getGame().getId());
        }
    }

    @Test
    @Order(4)
    public void testUpdatePromotionWithPartialData() {
        PromotionRequestDTO createRequest = new PromotionRequestDTO(
                VALID_NEW_PRICE,
                VALID_START_DATE,
                VALID_END_DATE,
                savedGame.getId()
        );

        ResponseEntity<PromotionResponseDTO> createResponse = client.postForEntity(
                "/promotions/create",
                createRequest,
                PromotionResponseDTO.class
        );

        int promotionId = createResponse.getBody().getId();

        // Update only the price
        Float newPrice = 49.99F;
        PromotionRequestDTO updateRequest = new PromotionRequestDTO(
                newPrice,
                VALID_START_DATE,
                VALID_END_DATE,
                savedGame.getId()
        );

        HttpEntity<PromotionRequestDTO> entity = new HttpEntity<>(updateRequest);

        ResponseEntity<PromotionResponseDTO> updateResponse = client.exchange(
                "/promotions/update/" + promotionId,
                HttpMethod.PUT,
                entity,
                PromotionResponseDTO.class
        );

        assertNotNull(updateResponse);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        PromotionResponseDTO updatedPromotion = updateResponse.getBody();
        assertNotNull(updatedPromotion);

        assertEquals(newPrice, updatedPromotion.getNewPrice());
        assertEquals(VALID_START_DATE, updatedPromotion.getStartDate());
        assertEquals(VALID_END_DATE, updatedPromotion.getEndDate());
        assertEquals(savedGame.getId(), updatedPromotion.getGame().getId());
    }

    @Test
    @Order(5)
    public void testGetAllPromotions() {
        PromotionRequestDTO firstPromotion = new PromotionRequestDTO(
                VALID_NEW_PRICE,
                VALID_START_DATE,
                VALID_END_DATE,
                savedGame.getId()
        );

        PromotionRequestDTO secondPromotion = new PromotionRequestDTO(
                39.99F,
                VALID_START_DATE.plusDays(1),
                VALID_END_DATE.plusDays(1),
                savedGame.getId()
        );

        client.postForEntity("/promotions/create", firstPromotion, PromotionResponseDTO.class);
        client.postForEntity("/promotions/create", secondPromotion, PromotionResponseDTO.class);

        ResponseEntity<PromotionResponseDTO[]> response = client.getForEntity(
                "/promotions",
                PromotionResponseDTO[].class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        PromotionResponseDTO[] promotions = response.getBody();
        assertNotNull(promotions);
        assertTrue(promotions.length >= 2, "Should have at least the two promotions we just created");

        boolean foundFirstPromotion = false;
        boolean foundSecondPromotion = false;

        for (PromotionResponseDTO promotion : promotions) {
            if (promotion.getNewPrice() == VALID_NEW_PRICE
                    && promotion.getStartDate().equals(VALID_START_DATE)
                    && promotion.getEndDate().equals(VALID_END_DATE)) {
                foundFirstPromotion = true;
            }
            if (promotion.getNewPrice() == 39.99F
                    && promotion.getStartDate().equals(VALID_START_DATE.plusDays(1))
                    && promotion.getEndDate().equals(VALID_END_DATE.plusDays(1))) {
                foundSecondPromotion = true;
            }
        }

        assertTrue(foundFirstPromotion, "First promotion should be in the list");
        assertTrue(foundSecondPromotion, "Second promotion should be in the list");
    }

}
