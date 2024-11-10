package ca.mcgill.ecse321.gamecenter.integration;

import ca.mcgill.ecse321.gamecenter.dto.Game.GameRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Game.GameResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Game.GameUpdateRequestDTO;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameIntegrationTests {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @Autowired
    private TestRestTemplate client;

    private GameCategory savedCategory;
    private Game savedGame;

    private final String VALID_TITLE = "Rayman Legends";
    private final float VALID_PRICE = 79.99F;
    private final String VALID_DESCRIPTION = "A fun platformer!";
    private final float VALID_RATING = 4.5F;
    private final int VALID_QUANTITY = 20;
    private final boolean VALID_IS_OFFERED = true;
    private final Game.GeneralFeeling VALID_OPINION = Game.GeneralFeeling.POSITIVE;
    private final String VALID_CATEGORY = "Platformer";

    @BeforeAll
    public void setupTestData() {
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();

        GameCategory category = new GameCategory();
        category.setCategory(VALID_CATEGORY);
        savedCategory = gameCategoryRepository.save(category);

    }

    @AfterEach
    public void clearDatabase() {
        gameRepository.deleteAll();
    }

    @AfterAll
    public void clearCategoryDatabase(){
        gameCategoryRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateGame() {
        GameRequestDTO gameRequest = new GameRequestDTO(
                VALID_TITLE,
                VALID_PRICE,
                VALID_DESCRIPTION,
                VALID_RATING,
                VALID_QUANTITY,
                VALID_IS_OFFERED,
                VALID_OPINION,
                savedCategory.getId()
        );

        ResponseEntity<GameResponseDTO> response = client.postForEntity(
                "/games/create",
                gameRequest,
                GameResponseDTO.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDTO body = response.getBody();
        assertNotNull(body);
        assertTrue(body.getId() > 0);
        assertEquals(VALID_TITLE, body.getTitle());
        assertEquals(VALID_PRICE, body.getPrice());
        assertEquals(VALID_DESCRIPTION, body.getDescription());
        assertEquals(VALID_RATING, body.getRating());
        assertEquals(VALID_QUANTITY, body.getRemainingQuantity());
        assertEquals(VALID_IS_OFFERED, body.getIsOffered());
        assertEquals(VALID_OPINION, body.getPublicOpinion());
        assertEquals(savedCategory.getId(), body.getCategory().getId());
    }

    @Test
    @Order(2)
    public void testGetGameById() {

        Game g = new Game();
        g.setTitle(VALID_TITLE);
        g.setPrice(VALID_PRICE);
        g.setDescription(VALID_DESCRIPTION);
        g.setRating(VALID_RATING);
        g.setRemainingQuantity(VALID_QUANTITY);
        g.setIsOffered(VALID_IS_OFFERED);
        g.setPublicOpinion(VALID_OPINION);
        g.setCategory(savedCategory);
        savedGame = gameRepository.save(g);

        ResponseEntity<GameResponseDTO> response = client.getForEntity(
                "/games/id/" + savedGame.getId(),
                GameResponseDTO.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDTO game = response.getBody();
        assertNotNull(game);
        assertEquals(savedGame.getId(), game.getId());
        assertEquals(VALID_TITLE, game.getTitle());
        assertEquals(VALID_PRICE, game.getPrice());
    }

    @Test
    @Order(3)
    public void testGetGameByTitle() {

        Game g = new Game();
        g.setTitle(VALID_TITLE);
        g.setPrice(VALID_PRICE);
        g.setDescription(VALID_DESCRIPTION);
        g.setRating(VALID_RATING);
        g.setRemainingQuantity(VALID_QUANTITY);
        g.setIsOffered(VALID_IS_OFFERED);
        g.setPublicOpinion(VALID_OPINION);
        g.setCategory(savedCategory);
        savedGame = gameRepository.save(g);

        ResponseEntity<GameResponseDTO> response = client.getForEntity(
                "/games/title/" + VALID_TITLE,
                GameResponseDTO.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDTO game = response.getBody();
        assertNotNull(game);
        assertEquals(VALID_TITLE, game.getTitle());
    }

    @Test
    @Order(4)
    public void testUpdateGame() {

        Game g = new Game();
        g.setTitle(VALID_TITLE);
        g.setPrice(VALID_PRICE);
        g.setDescription(VALID_DESCRIPTION);
        g.setRating(VALID_RATING);
        g.setRemainingQuantity(VALID_QUANTITY);
        g.setIsOffered(VALID_IS_OFFERED);
        g.setPublicOpinion(VALID_OPINION);
        g.setCategory(savedCategory);
        savedGame = gameRepository.save(g);

        Float newPrice = 69.99F;
        String newDescription = "Updated description";

        GameUpdateRequestDTO update = new GameUpdateRequestDTO(
                g.getTitle(),
                newPrice,
                newDescription,
                g.getRating(),
                g.getRemainingQuantity(),
                g.getIsOffered(),
                g.getCategory().getId()
        );

        HttpEntity<GameUpdateRequestDTO> entity = new HttpEntity<>(update);

        ResponseEntity<GameResponseDTO> response = client.exchange(
                "/games/update/" + savedGame.getId(),
                HttpMethod.PUT,
                entity,
                GameResponseDTO.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDTO updatedGame = response.getBody();
        assertNotNull(updatedGame);
        assertEquals(newPrice, updatedGame.getPrice());
        assertEquals(newDescription, updatedGame.getDescription());
        // Original values should remain unchanged
        assertEquals(VALID_TITLE, updatedGame.getTitle());
        assertEquals(VALID_RATING, updatedGame.getRating());
    }

    @Test
    @Order(5)
    public void testGetGamesByCategory() {

        Game g = new Game();
        g.setTitle(VALID_TITLE);
        g.setPrice(VALID_PRICE);
        g.setDescription(VALID_DESCRIPTION);
        g.setRating(VALID_RATING);
        g.setRemainingQuantity(VALID_QUANTITY);
        g.setIsOffered(VALID_IS_OFFERED);
        g.setPublicOpinion(VALID_OPINION);
        g.setCategory(savedCategory);
        savedGame = gameRepository.save(g);

        ResponseEntity<GameResponseDTO[]> response = client.getForEntity(
                "/games/category/" + savedCategory.getId(),
                GameResponseDTO[].class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDTO[] games = response.getBody();
        assertNotNull(games);
        assertTrue(games.length > 0);
        for (GameResponseDTO game : games) {
            assertEquals(savedCategory.getId(), game.getCategory().getId());
        }
    }

    @Test
    @Order(6)
    public void testGetAllAvailableGames() {
        Game g = new Game();
        g.setTitle(VALID_TITLE);
        g.setPrice(VALID_PRICE);
        g.setDescription(VALID_DESCRIPTION);
        g.setRating(VALID_RATING);
        g.setRemainingQuantity(VALID_QUANTITY);
        g.setIsOffered(VALID_IS_OFFERED);
        g.setPublicOpinion(VALID_OPINION);
        g.setCategory(savedCategory);
        savedGame = gameRepository.save(g);

        ResponseEntity<GameResponseDTO[]> response = client.getForEntity(
                "/games/available",
                GameResponseDTO[].class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDTO[] games = response.getBody();
        assertNotNull(games);
        assertTrue(games.length > 0);
        for (GameResponseDTO game : games) {
            assertTrue(game.getIsOffered());
        }
    }

    @Test
    @Order(7)
    public void testGetAvailableGamesByPriceRange() {

        Game g = new Game();
        g.setTitle(VALID_TITLE);
        g.setPrice(VALID_PRICE);
        g.setDescription(VALID_DESCRIPTION);
        g.setRating(VALID_RATING);
        g.setRemainingQuantity(VALID_QUANTITY);
        g.setIsOffered(VALID_IS_OFFERED);
        g.setPublicOpinion(VALID_OPINION);
        g.setCategory(savedCategory);
        savedGame = gameRepository.save(g);
        float minPrice = 70.0F;
        float maxPrice = 90.0F;

        ResponseEntity<GameResponseDTO[]> response = client.getForEntity(
                String.format("/games/available/price?minPrice=%f&maxPrice=%f", minPrice, maxPrice),
                GameResponseDTO[].class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDTO[] games = response.getBody();
        assertNotNull(games);
        for (GameResponseDTO game : games) {
            assertTrue(game.getIsOffered());
            assertTrue(game.getPrice() >= minPrice && game.getPrice() <= maxPrice);
        }
    }

    @Test
    @Order(8)
    public void testGetAvailableGamesByRatingRange() {

        Game g = new Game();
        g.setTitle(VALID_TITLE);
        g.setPrice(VALID_PRICE);
        g.setDescription(VALID_DESCRIPTION);
        g.setRating(VALID_RATING);
        g.setRemainingQuantity(VALID_QUANTITY);
        g.setIsOffered(VALID_IS_OFFERED);
        g.setPublicOpinion(VALID_OPINION);
        g.setCategory(savedCategory);
        savedGame = gameRepository.save(g);

        float minRating = 4.0F;
        float maxRating = 5.0F;

        ResponseEntity<GameResponseDTO[]> response = client.getForEntity(
                String.format("/games/available/rating?minRating=%f&maxRating=%f", minRating, maxRating),
                GameResponseDTO[].class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDTO[] games = response.getBody();
        assertNotNull(games);
        for (GameResponseDTO game : games) {
            assertTrue(game.getIsOffered());
            assertTrue(game.getRating() >= minRating && game.getRating() <= maxRating);
        }
    }

    @Test
    @Order(9)
    public void testMakeGameOfferedAndNotOffered() {

        Game g = new Game();
        g.setTitle(VALID_TITLE);
        g.setPrice(VALID_PRICE);
        g.setDescription(VALID_DESCRIPTION);
        g.setRating(VALID_RATING);
        g.setRemainingQuantity(VALID_QUANTITY);
        g.setIsOffered(VALID_IS_OFFERED);
        g.setPublicOpinion(VALID_OPINION);
        g.setCategory(savedCategory);
        savedGame = gameRepository.save(g);

        ResponseEntity<GameResponseDTO> response1 = client.exchange(
                "/games/" + savedGame.getId() + "/unoffer",
                HttpMethod.PUT,
                null,
                GameResponseDTO.class
        );

        assertNotNull(response1);
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertFalse(response1.getBody().getIsOffered());

        ResponseEntity<GameResponseDTO> response2 = client.exchange(
                "/games/" + savedGame.getId() + "/offer",
                HttpMethod.PUT,
                null,
                GameResponseDTO.class
        );

        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertTrue(response2.getBody().getIsOffered());
    }
}
