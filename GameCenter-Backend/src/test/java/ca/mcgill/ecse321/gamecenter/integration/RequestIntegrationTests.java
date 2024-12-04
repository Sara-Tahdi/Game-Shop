package ca.mcgill.ecse321.gamecenter.integration;

import ca.mcgill.ecse321.gamecenter.dto.Requests.RequestResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Requests.GameRequestRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Requests.GameRequestResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Requests.UserRequestRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Requests.UserRequestResponseDTO;

import ca.mcgill.ecse321.gamecenter.model.Owner;
import ca.mcgill.ecse321.gamecenter.model.Employee;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Game.GeneralFeeling;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;

import ca.mcgill.ecse321.gamecenter.repository.RequestRepository;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RequestIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private GameCategoryRepository gameCategoryRepository;
    @Autowired
    private GameRepository gameRepository;

    private static final String VALID_OWNER_USERNAME = "JoeBiden";
    private static final String VALID_OWNER_EMAIL = "chocochoclatechip@cookie.com";
    private static final String VALID_OWNER_PASSWORD = "MINNESOTAAA";

    private static final String VALID_EMPLOYEE_USERNAME = "KleenexBox";
    private static final String VALID_EMPLOYEE_EMAIL = "ilovedrake@ovo.com";
    private static final String VALID_EMPLOYEE_PASSWORD = "DrakeyPie";

    private static final String VALID_CLIENT_USERNAME = "GamerNoob";
    private static final String VALID_CLIENT_EMAIL = "fortnite4life@gmail.com";
    private static final String VALID_CLIENT_PASSWORD = "flosslikeaboss";
    private static final String VALID_CLIENT_PHONENUMBER = "5143913851";
    private static final String VALID_CLIENT_DELIVERYADDRESS = "123 John Street";

    private static final String VALID_GAME_CATEGORY_1 = "Action";
    private static final String VALID_GAME_CATEGORY_2 = "Adventure";

    private static final String VALID_GAME_TITLE_1 = "Fortnite";
    private static final float VALID_GAME_PRICE_1 = 39.99f;
    private static final String VALID_GAME_DESCRIPTION_1 = "Battle Royale";
    private static final float VALID_GAME_RATING_1 = 4.5f;
    private static final int VALID_GAME_REMAINING_QUANTITY_1 = 100;
    private static final boolean VALID_GAME_IS_OFFERED_1 = false;
    private static final GeneralFeeling VALID_GAME_PUBLIC_OPINION_1 = GeneralFeeling.POSITIVE;

    private static final String VALID_GAME_TITLE_2 = "Minecraft";
    private static final float VALID_GAME_PRICE_2 = 29.99f;
    private static final String VALID_GAME_DESCRIPTION_2 = "Sandbox";
    private static final float VALID_GAME_RATING_2 = 4.8f;
    private static final int VALID_GAME_REMAINING_QUANTITY_2 = 50;
    private static final boolean VALID_GAME_IS_OFFERED_2 = true;
    private static final GeneralFeeling VALID_GAME_PUBLIC_OPINION_2 = GeneralFeeling.VERYPOSITIVE;

    private static final String VALID_GAME_REQUEST_REASON_1 = "I want to play this game, so would our customers.";
    private static final String VALID_GAME_REQUEST_REASON_2 = "No one is buying this game, this should go!";
    private static final String VALID_USER_REQUEST_REASON = "This user leaves rude comments!";

    @BeforeAll
    public void testSetup() {
        requestRepository.deleteAll();
        appUserRepository.deleteAll();
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();

        // Create app users
        appUserRepository.save(new Owner(VALID_OWNER_EMAIL, VALID_OWNER_USERNAME, VALID_OWNER_PASSWORD));
        appUserRepository.save(new Employee(VALID_EMPLOYEE_EMAIL, VALID_EMPLOYEE_USERNAME, VALID_EMPLOYEE_PASSWORD));
        appUserRepository.save(new Client(VALID_CLIENT_EMAIL, VALID_CLIENT_USERNAME, VALID_CLIENT_PASSWORD, VALID_CLIENT_PHONENUMBER, VALID_CLIENT_DELIVERYADDRESS));

        // Create games and game categories
        gameRepository.save(new Game(VALID_GAME_TITLE_1, VALID_GAME_PRICE_1, VALID_GAME_DESCRIPTION_1, VALID_GAME_RATING_1, VALID_GAME_REMAINING_QUANTITY_1, VALID_GAME_IS_OFFERED_1, VALID_GAME_PUBLIC_OPINION_1, gameCategoryRepository.save(new GameCategory(VALID_GAME_CATEGORY_1)), ""));
        gameRepository.save(new Game(VALID_GAME_TITLE_2, VALID_GAME_PRICE_2, VALID_GAME_DESCRIPTION_2, VALID_GAME_RATING_2, VALID_GAME_REMAINING_QUANTITY_2, VALID_GAME_IS_OFFERED_2, VALID_GAME_PUBLIC_OPINION_2, gameCategoryRepository.save(new GameCategory(VALID_GAME_CATEGORY_2)), ""));
    }

    @AfterAll
    public void clearDatabaseAfterAll() {
        requestRepository.deleteAll();
        appUserRepository.deleteAll();
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateAddGameRequest() {
        // Arrange
        GameRequestRequestDTO request = new GameRequestRequestDTO(VALID_GAME_REQUEST_REASON_1, VALID_EMPLOYEE_USERNAME, VALID_GAME_TITLE_1);

        // Act
        ResponseEntity<GameRequestResponseDTO> response = client.postForEntity("/requests/game/add", request, GameRequestResponseDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameRequestResponseDTO requestResponse = response.getBody();
        assertNotNull(requestResponse);
        assertEquals(VALID_GAME_REQUEST_REASON_1, requestResponse.getReason());
        assertEquals(VALID_GAME_TITLE_1, requestResponse.getGame().getTitle());
        assertEquals("ADD", requestResponse.getRequestType());
        assertNotNull(requestResponse.getCreatedRequest().getId());
        assertTrue(requestResponse.getCreatedRequest().getId() > 0, "Response should have a positive ID.");
        assertEquals(VALID_EMPLOYEE_USERNAME, requestResponse.getCreatedRequest().getUsername());
        assertEquals(VALID_EMPLOYEE_EMAIL, requestResponse.getCreatedRequest().getEmail());
        assertNotNull(requestResponse.getGame().getId());
        assertTrue(requestResponse.getGame().getId() > 0, "Game ID should be positive.");
        assertNotNull(requestResponse.getId());
        assertTrue(requestResponse.getId() > 0, "Response should have a positive ID.");
        assertEquals("PENDING", requestResponse.getStatus());
    }

    @Test
    @Order(2)
    public void testCreateRemoveGameRequest() {
        // Arrange
        GameRequestRequestDTO request = new GameRequestRequestDTO(VALID_GAME_REQUEST_REASON_2, VALID_EMPLOYEE_USERNAME, VALID_GAME_TITLE_2);

        // Act
        ResponseEntity<GameRequestResponseDTO> response = client.postForEntity("/requests/game/remove", request, GameRequestResponseDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameRequestResponseDTO requestResponse = response.getBody();
        assertNotNull(requestResponse);
        assertEquals(VALID_GAME_REQUEST_REASON_2, requestResponse.getReason());
        assertEquals(VALID_GAME_TITLE_2, requestResponse.getGame().getTitle());
        assertEquals("REMOVE", requestResponse.getRequestType());
        assertNotNull(requestResponse.getCreatedRequest().getId());
        assertTrue(requestResponse.getCreatedRequest().getId() > 0, "Response should have a positive ID.");
        assertEquals(VALID_EMPLOYEE_USERNAME, requestResponse.getCreatedRequest().getUsername());
        assertEquals(VALID_EMPLOYEE_EMAIL, requestResponse.getCreatedRequest().getEmail());
        assertNotNull(requestResponse.getGame().getId());
        assertTrue(requestResponse.getGame().getId() > 0, "Game ID should be positive.");
        assertNotNull(requestResponse.getId());
        assertTrue(requestResponse.getId() > 0, "Response should have a positive ID.");
        assertEquals("PENDING", requestResponse.getStatus());
    }

    @Test
    @Order(3)
    public void testCreateUserRequest() {
        // Arrange
        UserRequestRequestDTO request = new UserRequestRequestDTO(VALID_USER_REQUEST_REASON, VALID_EMPLOYEE_USERNAME, VALID_CLIENT_USERNAME);

        // Act
        ResponseEntity<UserRequestResponseDTO> response = client.postForEntity("/requests/user/flag", request, UserRequestResponseDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserRequestResponseDTO requestResponse = response.getBody();
        assertNotNull(requestResponse);
        assertEquals(VALID_USER_REQUEST_REASON, requestResponse.getReason());
        assertEquals(VALID_EMPLOYEE_USERNAME, requestResponse.getCreatedRequest().getUsername());
        assertEquals(VALID_EMPLOYEE_EMAIL, requestResponse.getCreatedRequest().getEmail());
        assertNotNull(requestResponse.getCreatedRequest().getId());
        assertTrue(requestResponse.getCreatedRequest().getId() > 0, "Request creator should have a positive ID.");
        assertEquals(VALID_CLIENT_USERNAME, requestResponse.getUserFacingJudgement().getUsername());
        assertEquals(VALID_CLIENT_EMAIL, requestResponse.getUserFacingJudgement().getEmail());
        assertNotNull(requestResponse.getUserFacingJudgement().getId());
        assertTrue(requestResponse.getUserFacingJudgement().getId() > 0, "User facing judgement should have a positive ID.");
        assertNotNull(requestResponse.getId());
        assertTrue(requestResponse.getId() > 0, "Response should have a positive ID.");
        assertEquals("PENDING", requestResponse.getStatus());
    }

    @Test
    @Order(4)
    public void testGetAllRequests() {
         // Act
         ResponseEntity<RequestResponseDTO[]> response = client.getForEntity("/requests", RequestResponseDTO[].class);
 
         // Assert
         assertNotNull(response);
         assertEquals(HttpStatus.OK, response.getStatusCode());
         List<RequestResponseDTO> body = Arrays.stream(response.getBody()).toList();
         assertNotNull(body);
         assertEquals(3, body.size());
    }

    @Test
    @Order(5)
    public void testGetGameRequests() {
        // Act
        ResponseEntity<GameRequestResponseDTO[]> response = client.getForEntity("/requests/game", GameRequestResponseDTO[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<GameRequestResponseDTO> body = Arrays.stream(response.getBody()).toList();
        assertNotNull(body);
        assertEquals(2, body.size());
    }

    @Test
    @Order(6)
    public void testGetUserRequests() {
        // Act
        ResponseEntity<UserRequestResponseDTO[]> response = client.getForEntity("/requests/user", UserRequestResponseDTO[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<UserRequestResponseDTO> body = Arrays.stream(response.getBody()).toList();
        assertNotNull(body);
        assertEquals(1, body.size());
    }

    @Test
    @Order(7)
    public void testGetRequestsByStatus() {
        // Act
        ResponseEntity<RequestResponseDTO[]> response = client.getForEntity("/requests/status/PENDING", RequestResponseDTO[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<RequestResponseDTO> body = Arrays.stream(response.getBody()).toList();
        assertNotNull(body);
        assertEquals(3, body.size());
    }

    @Test
    @Order(8)
    public void testGetRequestByCreatedRequestUsername() {
        // Act
        ResponseEntity<RequestResponseDTO[]> response = client.getForEntity("/requests/creator/username/" + VALID_EMPLOYEE_USERNAME, RequestResponseDTO[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<RequestResponseDTO> body = Arrays.stream(response.getBody()).toList();
        assertNotNull(body);
        assertEquals(3, body.size());
    }

    @Test
    @Order(9)
    public void testGetRequestByCreatedRequestEmail() {
        // Act
        ResponseEntity<RequestResponseDTO[]> response = client.getForEntity("/requests/creator/email/" + VALID_EMPLOYEE_EMAIL, RequestResponseDTO[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<RequestResponseDTO> body = Arrays.stream(response.getBody()).toList();
        assertNotNull(body);
        assertEquals(3, body.size());
    }

    @Test
    @Order(10)
    public void testGetRequestsByCreatedRequestId() {
        // Arrange
        ResponseEntity<RequestResponseDTO[]> response = client.getForEntity("/requests", RequestResponseDTO[].class);

        // Act
        ResponseEntity<RequestResponseDTO[]> response2 = client.getForEntity("/requests/creator/id/" + response.getBody()[0].getCreatedRequest().getId(), RequestResponseDTO[].class);

        // Assert
        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        List<RequestResponseDTO> body = Arrays.stream(response2.getBody()).toList();
        assertNotNull(body);
        assertEquals(3, body.size());
    }

    @Test
    @Order(11)
    public void testGetRequestById() {
        // Arrange
        ResponseEntity<RequestResponseDTO[]> response = client.getForEntity("/requests", RequestResponseDTO[].class);

        // Act
        ResponseEntity<RequestResponseDTO> response2 = client.getForEntity("/requests/id/" + response.getBody()[0].getId(), RequestResponseDTO.class);

        // Assert
        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        RequestResponseDTO body = response2.getBody();
        assertNotNull(body);
        assertEquals(response.getBody()[0].getId(), body.getId());
    }

    @Test
    @Order(12)
    public void testGetRequestsByGameTitle() {
        // Act
        ResponseEntity<GameRequestResponseDTO[]> response = client.getForEntity("/requests/game/title/" + VALID_GAME_TITLE_1, GameRequestResponseDTO[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<GameRequestResponseDTO> body = Arrays.stream(response.getBody()).toList();
        assertNotNull(body);
        assertEquals(1, body.size());
    }

    @Test
    @Order(13)
    public void testGetRequestsByGameId() {
        // Arrange
        ResponseEntity<GameRequestResponseDTO[]> response = client.getForEntity("/requests/game", GameRequestResponseDTO[].class);

        // Act
        ResponseEntity<GameRequestResponseDTO[]> response2 = client.getForEntity("/requests/game/id/" + response.getBody()[0].getGame().getId(), GameRequestResponseDTO[].class);

        // Assert
        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        List<GameRequestResponseDTO> body = Arrays.stream(response2.getBody()).toList();
        assertNotNull(body);
        assertEquals(1, body.size());
    }

    @Test
    @Order(14)
    public void testGetRequestsByUserFacingJudgementEmail() {
        // Act
        ResponseEntity<UserRequestResponseDTO[]> response = client.getForEntity("/requests/user/email/" + VALID_CLIENT_EMAIL, UserRequestResponseDTO[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<UserRequestResponseDTO> body = Arrays.stream(response.getBody()).toList();
        assertNotNull(body);
        assertEquals(1, body.size());
    }

    @Test
    @Order(15)
    public void testGetRequestsByUserFacingJudgementId() {
        // Arrange
        ResponseEntity<UserRequestResponseDTO[]> response = client.getForEntity("/requests/user", UserRequestResponseDTO[].class);

        // Act
        ResponseEntity<UserRequestResponseDTO[]> response2 = client.getForEntity("/requests/user/id/" + response.getBody()[0].getUserFacingJudgement().getId(), UserRequestResponseDTO[].class);

        // Assert
        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        List<UserRequestResponseDTO> body = Arrays.stream(response2.getBody()).toList();
        assertNotNull(body);
        assertEquals(1, body.size());
    }

    @Test
    @Order(16)
    public void testGetRequestsByUserFacingJudgementUsername() {
        // Act
        ResponseEntity<UserRequestResponseDTO[]> response = client.getForEntity("/requests/user/username/" + VALID_CLIENT_USERNAME, UserRequestResponseDTO[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<UserRequestResponseDTO> body = Arrays.stream(response.getBody()).toList();
        assertNotNull(body);
        assertEquals(1, body.size());
    }

    @Test
    @Order(18)
    public void testApproveRequest() {
        // Arrange
        ResponseEntity<GameRequestResponseDTO[]> response = client.getForEntity("/requests/game", GameRequestResponseDTO[].class);

        String url = "/requests/approve/" + response.getBody()[1].getId();

        // Act
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);
        ResponseEntity<RequestResponseDTO> response2 = client.exchange(url, HttpMethod.PUT, requestEntity, RequestResponseDTO.class);

        // Assert
        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        RequestResponseDTO body = response2.getBody();
        assertNotNull(body);
        assertEquals("APPROVED", body.getStatus());
    }

    @Test
    @Order(19)
    public void testDenyRequest() {
        // Arrange
        ResponseEntity<RequestResponseDTO[]> response = client.getForEntity("/requests", RequestResponseDTO[].class);

        String url = "/requests/deny/" + response.getBody()[0].getId();

        // Act
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);
        ResponseEntity<RequestResponseDTO> response2 = client.exchange(url, HttpMethod.PUT, requestEntity, RequestResponseDTO.class);

        // Assert
        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        RequestResponseDTO body = response2.getBody();
        assertNotNull(body);
        assertEquals("DENIED", body.getStatus());
    }
}
