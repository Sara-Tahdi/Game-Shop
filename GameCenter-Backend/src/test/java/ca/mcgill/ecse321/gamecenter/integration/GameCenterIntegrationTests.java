package ca.mcgill.ecse321.gamecenter.integration;

import ca.mcgill.ecse321.gamecenter.dto.GameCenterDTO;
import ca.mcgill.ecse321.gamecenter.repository.GameCenterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
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

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameCenterIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GameCenterRepository gameCenterRepository;

    private final String VALID_NAME = "GameZone";
    private final Time VALID_OPEN = Time.valueOf("09:00:00");
    private final Time VALID_CLOSE = Time.valueOf("21:00:00");
    private final String VALID_STORE_POLICY = "No food allowed";

    @BeforeEach
    public void setup() {
        gameCenterRepository.deleteAll(); // Clear the database before each test
    }

    @AfterAll
    public void cleanup() {
        gameCenterRepository.deleteAll(); // Clear the database after all tests have completed
    }

    @Test
    @Order(1)
    public void testCreateOrUpdateGameCenter() {
        // Arrange
        GameCenterDTO gameCenterDTO = new GameCenterDTO();
        gameCenterDTO.setName(VALID_NAME);
        gameCenterDTO.setOpen(VALID_OPEN);
        gameCenterDTO.setClose(VALID_CLOSE);
        gameCenterDTO.setStorePolicy(VALID_STORE_POLICY);

        // Act
        ResponseEntity<GameCenterDTO> response = restTemplate.postForEntity("/gamecenter/createGameCenter", gameCenterDTO, GameCenterDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameCenterDTO createdGameCenterDTO = response.getBody();
        assertNotNull(createdGameCenterDTO);
        assertEquals(VALID_NAME, createdGameCenterDTO.getName());
        assertEquals(VALID_OPEN, createdGameCenterDTO.getOpen());
        assertEquals(VALID_CLOSE, createdGameCenterDTO.getClose());
        assertEquals(VALID_STORE_POLICY, createdGameCenterDTO.getStorePolicy());
    }
}
