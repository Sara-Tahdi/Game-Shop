package ca.mcgill.ecse321.gamecenter.integration;

import ca.mcgill.ecse321.gamecenter.dto.GameCategory.GameCategoryRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.GameCategory.GameCategoryResponseDTO;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
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

    @AfterAll
    public void cleanup() {
        gameCategoryRepository.deleteAll();  // Clear the database after all tests have completed
    }

    @Test
    @Order(1)
    public void testCreateGameCategory() {
        // Arrange
        GameCategoryRequestDTO requestDto = new GameCategoryRequestDTO();
        requestDto.setCategory(VALID_CATEGORY_NAME);

        // Act
        ResponseEntity<GameCategoryResponseDTO> response = restTemplate.postForEntity(BASE_URL + "/create", requestDto, GameCategoryResponseDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        GameCategoryResponseDTO createdCategory = response.getBody();
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
        ResponseEntity<GameCategoryResponseDTO> response = restTemplate.getForEntity(BASE_URL + "/" + createdCategoryId, GameCategoryResponseDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        GameCategoryResponseDTO retrievedCategory = response.getBody();
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
        GameCategoryRequestDTO updateDto = new GameCategoryRequestDTO();
        updateDto.setCategory(updatedCategoryName);

        // Act
        restTemplate.postForEntity(BASE_URL + "/" + createdCategoryId, updateDto, GameCategoryResponseDTO.class);

        // Retrieve updated category to verify changes
        ResponseEntity<GameCategoryResponseDTO> response = restTemplate.exchange(BASE_URL + "/" + createdCategoryId, HttpMethod.PUT, new HttpEntity<>(updateDto), GameCategoryResponseDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        GameCategoryResponseDTO updatedCategory = response.getBody();
        assertNotNull(updatedCategory);
        assertEquals(createdCategoryId, updatedCategory.getId());
        assertEquals(updatedCategoryName, updatedCategory.getCategory());
    }

    @Test
    @Order(4)
    public void testRetrieveAllGameCategories() {
        // First, create two categories
        testCreateGameCategory();

        GameCategoryRequestDTO secondCategory = new GameCategoryRequestDTO();
        secondCategory.setCategory("Sports");
        restTemplate.postForEntity(BASE_URL + "/create", secondCategory, GameCategoryResponseDTO.class);

        // Act
        ResponseEntity<GameCategoryResponseDTO[]> response = restTemplate.getForEntity(BASE_URL, GameCategoryResponseDTO[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        GameCategoryResponseDTO[] categories = response.getBody();
        assertNotNull(categories);
        assertEquals(2, categories.length);
    }
}
