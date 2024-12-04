package ca.mcgill.ecse321.gamecenter.integration;

import ca.mcgill.ecse321.gamecenter.dto.Cart.*;
import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartIntegrationTests {

    @Autowired
    private TestRestTemplate client;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    private static final String VALID_OWNER_USERNAME = "JoeBiden";
    private static final String VALID_OWNER_EMAIL = "chocochoclatechip@cookie.com";
    private static final String VALID_OWNER_PASSWORD = "MINNESOTAAA";
    private static final String VALID_CLIENT_USERNAME = "GamerNoob";
    private static final String VALID_CLIENT_EMAIL = "fortnite4life@gmail.com";
    private static final String VALID_CLIENT_PASSWORD = "flosslikeaboss";
    private static final String VALID_CLIENT_PHONENUMBER = "5143913851";
    private static final String VALID_CLIENT_DELIVERYADDRESS = "123 John Street";
    private static final String VALID_GAME_CATEGORY = "Action";
    private static final String VALID_GAME_NAME = "Call of Duty: Black Ops 6";
    private static final float VALID_GAME_PRICE = 89.99f;
    private static final String VALID_GAME_DESCRIPTION = "Fun game I think maybe perchance";
    private static final Game.GeneralFeeling VALID_GAME_GENERAL_FEELING = Game.GeneralFeeling.POSITIVE;
    private static final int VALID_GAME_STOCK = 4;

    private int clientId;
    private int gameId_1;

    @BeforeAll
    public void setupDatabase() {
        //Create owner and client
        appUserRepository.save(new Owner(VALID_OWNER_EMAIL, VALID_OWNER_USERNAME, VALID_OWNER_PASSWORD));
        Client c = appUserRepository.save(new Client(
                VALID_CLIENT_EMAIL,
                VALID_CLIENT_USERNAME,
                VALID_CLIENT_PASSWORD,
                VALID_CLIENT_PHONENUMBER,
                VALID_CLIENT_DELIVERYADDRESS
        ));
        this.clientId = c.getId();

        // Create game category
        GameCategory gameCategory = new GameCategory(VALID_GAME_CATEGORY);
        gameCategoryRepository.save(gameCategory);

        // Create  game
        Game g1 = gameRepository.save(new Game(
                VALID_GAME_NAME,
                VALID_GAME_PRICE,
                VALID_GAME_DESCRIPTION,
                0,
                VALID_GAME_STOCK,
                true,
                VALID_GAME_GENERAL_FEELING,
                gameCategory,
                ""
        ));
        this.gameId_1 = g1.getId();
    }

    @AfterAll
    public void clearDatabase() {
        cartRepository.deleteAll();
        appUserRepository.deleteAll();
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateCart() {
        String url = "/carts/create";

        // Create cart for the game
        CartRequestDto cartRequestDto = new CartRequestDto(this.gameId_1, this.clientId);

        ResponseEntity<CartResponseDto> res = client.postForEntity(url, cartRequestDto, CartResponseDto.class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        CartResponseDto body = res.getBody();
        assertNotNull(body);
        assertEquals(this.clientId, body.getClientId());
        assertEquals(this.gameId_1, body.getGameId());

    }

    @Test
    @Order(2)
    public void testFindCartsByClientId() {
        String url = String.format("/carts/client/%d", this.clientId);

        ResponseEntity<CartResponseDto[]> res = client.getForEntity(url, CartResponseDto[].class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<CartResponseDto> body = List.of(res.getBody());
        assertTrue(body.size() >= 1);
    }

    @Test
    @Order(3)
    public void testFindCartsByGameId() {
        String url = String.format("/carts/game/%d", this.gameId_1);

        ResponseEntity<CartResponseDto[]> res = client.getForEntity(url, CartResponseDto[].class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<CartResponseDto> body = List.of(res.getBody());
        assertTrue(body.size() >= 1);
    }

    @Test
    @Order(4)
    public void testFindCartByClientAndGame() {
        String url = String.format("/carts/client/%d/game/%d", this.clientId, this.gameId_1);

        ResponseEntity<CartResponseDto> res = client.getForEntity(url, CartResponseDto.class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        CartResponseDto body = res.getBody();
        assertEquals(this.clientId, body.getClientId());
        assertEquals(this.gameId_1, body.getGameId());
    }

    @Test
    @Order(5)
    public void testRemoveGameFromCart() {
        String url = String.format("/carts/remove?clientId=%d&gameId=%d", this.clientId, this.gameId_1);

        ResponseEntity<Void> res = client.exchange(url, HttpMethod.DELETE, null, Void.class);

        assertNotNull(res);
        assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
    }

    @Test
    @Order(6)
    public void testFindCartsByClientIdAfterRemoval() {
        String url = String.format("/carts/client/%d", this.clientId);

        ResponseEntity<CartResponseDto[]> res = client.getForEntity(url, CartResponseDto[].class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<CartResponseDto> body = List.of(res.getBody());
        assertTrue(body.size() == 0);
    }

    @Test
    @Order(7)
    public void testFindCartsByGameIdAfterRemoval() {
        String url = String.format("/carts/game/%d", this.gameId_1);

        ResponseEntity<CartResponseDto[]> res = client.getForEntity(url, CartResponseDto[].class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<CartResponseDto> body = List.of(res.getBody());
        assertTrue(body.size() == 0);
    }
}
