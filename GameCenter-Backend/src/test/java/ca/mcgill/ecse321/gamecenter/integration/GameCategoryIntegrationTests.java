package ca.mcgill.ecse321.gamecenter.integration;

import ca.mcgill.ecse321.gamecenter.dto.GameCategoryDTO;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameCategoryIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    private final String BASE_URL = "/gameCategory";
    private final String VALID_CATEGORY_NAME = "Arcade";
    private int createdCategoryId;

    @BeforeEach
    public void setup() {
        gameCategoryRepository.deleteAll();  // Clear the database before each test
    }

    @Test
    @Order(1)
    public void testCreateGameCategory() {
        // Arrange
        GameCategoryDTO requestDto = new GameCategoryDTO();
        requestDto.setCategory(VALID_CATEGORY_NAME);

        // Act
        ResponseEntity<GameCategoryDTO> response = restTemplate.postForEntity(BASE_URL + "/create", requestDto, GameCategoryDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        GameCategoryDTO createdCategory = response.getBody();
        assertNotNull(createdCategory);
        assertEquals(VALID_CATEGORY_NAME, createdCategory.getCategory());
        assertTrue(createdCategory.getId() > 0);

        this.createdCategoryId = createdCategory.getId();
    }

    @Test
    @Order(2)
    public void testRetrieveGameCategoryById() {
        // First, create a category to retrieve
        testCreateGameCategory();

        // Act
        ResponseEntity<GameCategoryDTO> response = restTemplate.getForEntity(BASE_URL + "/" + createdCategoryId, GameCategoryDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        GameCategoryDTO retrievedCategory = response.getBody();
        assertNotNull(retrievedCategory);
        assertEquals(createdCategoryId, retrievedCategory.getId());
        assertEquals(VALID_CATEGORY_NAME, retrievedCategory.getCategory());
    }

    @Test
    @Order(3)
    public void testUpdateGameCategory() {
        // First, create a category to update
        testCreateGameCategory();

        // Arrange
        String updatedCategoryName = "Puzzle";
        GameCategoryDTO updateDto = new GameCategoryDTO();
        updateDto.setCategory(updatedCategoryName);

        // Act
        restTemplate.postForEntity(BASE_URL + "/" + createdCategoryId, updateDto, GameCategoryDTO.class);

        // Retrieve updated category to verify changes
        ResponseEntity<GameCategoryDTO> response = restTemplate.getForEntity(BASE_URL + "/" + createdCategoryId, GameCategoryDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        GameCategoryDTO updatedCategory = response.getBody();
        assertNotNull(updatedCategory);
        assertEquals(createdCategoryId, updatedCategory.getId());
        assertEquals(updatedCategoryName, updatedCategory.getCategory());
    }

    @Test
    @Order(4)
    public void testRetrieveAllGameCategories() {
        // First, create two categories
        testCreateGameCategory();

        GameCategoryDTO secondCategory = new GameCategoryDTO();
        secondCategory.setCategory("Sports");
        restTemplate.postForEntity(BASE_URL + "/create", secondCategory, GameCategoryDTO.class);

        // Act
        ResponseEntity<GameCategoryDTO[]> response = restTemplate.getForEntity(BASE_URL, GameCategoryDTO[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        GameCategoryDTO[] categories = response.getBody();
        assertNotNull(categories);
        assertEquals(2, categories.length);
    }
}
