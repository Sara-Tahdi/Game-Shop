package ca.mcgill.ecse321.gamecenter.integration;

import ca.mcgill.ecse321.gamecenter.dto.Wishlist.*;
import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.WishlistRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
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
public class WishlistIntegrationTests {

    @Autowired
    private TestRestTemplate client;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private WishlistRepository wishlistRepository;
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
    private int wishlistId_1;

    @BeforeAll
    public void setupDatabase() {
        //  Create owner and client
        appUserRepository.save(new Owner(VALID_OWNER_EMAIL, VALID_OWNER_USERNAME, VALID_OWNER_PASSWORD));
        Client c = appUserRepository.save(new Client(
                VALID_CLIENT_EMAIL,
                VALID_CLIENT_USERNAME,
                VALID_CLIENT_PASSWORD,
                VALID_CLIENT_PHONENUMBER,
                VALID_CLIENT_DELIVERYADDRESS
        ));
        this.clientId = c.getId();

        // Create a game category
        GameCategory gameCategory = new GameCategory(VALID_GAME_CATEGORY);
        gameCategoryRepository.save(gameCategory);

        Game g1 = gameRepository.save(new Game(
                VALID_GAME_NAME,
                VALID_GAME_PRICE,
                VALID_GAME_DESCRIPTION,
                0,
                VALID_GAME_STOCK,
                true,
                VALID_GAME_GENERAL_FEELING,
                gameCategory
        ));
        this.gameId_1 = g1.getId();
    }

    @AfterAll
    public void clearDatabase() {
        wishlistRepository.deleteAll();
        appUserRepository.deleteAll();
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateWishlist() {
        String url = "/wishlists/create";

        // Create wishlist for only the first game
        WishlistRequestDto wishlistRequestDto = new WishlistRequestDto(this.gameId_1, this.clientId);

        ResponseEntity<WishlistResponseDto> res = client.postForEntity(url, wishlistRequestDto, WishlistResponseDto.class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        WishlistResponseDto body = res.getBody();
        assertNotNull(body);
        assertEquals(this.clientId, body.getClientId());
        assertEquals(this.gameId_1, body.getGameId());

        this.wishlistId_1 = body.getId();
    }

    @Test
    @Order(2)
    public void testFindWishlistsByClientId() {
        String url = String.format("/wishlists/client/%d", this.clientId);

        ResponseEntity<WishlistResponseDto[]> res = client.getForEntity(url, WishlistResponseDto[].class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<WishlistResponseDto> body = List.of(res.getBody());
        assertTrue(body.size() >= 1);
    }

    @Test
    @Order(3)
    public void testFindWishlistById() {
        String url = String.format("/wishlists/%d", this.wishlistId_1);

        ResponseEntity<WishlistResponseDto> res = client.getForEntity(url, WishlistResponseDto.class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        WishlistResponseDto body = res.getBody();
        assertEquals(this.wishlistId_1, body.getId());
        assertEquals(this.clientId, body.getClientId());
        assertEquals(this.gameId_1, body.getGameId());
    }

    @Test
    @Order(4)
    public void testRemoveGameFromWishlist() {
        String url = String.format("/wishlists/remove?clientId=%d&gameId=%d", this.clientId, this.gameId_1);

        ResponseEntity<Void> res = client.exchange(url, HttpMethod.DELETE, null, Void.class);

        assertNotNull(res);
        assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
    }

    @Test
    @Order(5)
    public void testFindWishlistsByGameId() {
        String url = String.format("/wishlists/game/%d", this.gameId_1);

        ResponseEntity<WishlistResponseDto[]> res = client.getForEntity(url, WishlistResponseDto[].class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<WishlistResponseDto> body = List.of(res.getBody());
        assertTrue(body.size() == 0);  
    }
}
