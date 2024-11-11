package ca.mcgill.ecse321.gamecenter.integration;

import ca.mcgill.ecse321.gamecenter.dto.Purchase.PurchaseResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Review.ManagerReplyRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Review.ReviewRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Review.ReviewResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.ReviewRepository;
import ca.mcgill.ecse321.gamecenter.service.ReviewService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReviewIntegrationTests {
    @Autowired
    TestRestTemplate client;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    private static final String VALID_OWNER_USERNAME = "JoeBiden";
    private static final String VALID_OWNER_EMAIL = "chocochoclatechip@cookie.com";
    private static final String VALID_OWNER_PASSWORD = "MINNESOTAAA";

    private static final String VALID_EMPLOYEE_USERNAME = "KleenexBox";
    private static final String VALID_EMPLOYEE_EMAIL = "ilovedrake@ovo.com";
    private static final String VALID_EMPLOYEE_PASSWORD = "DrakeyPie";

    private static final String VALID_CLIENT_USERNAME_1 = "GamerNoob";
    private static final String VALID_CLIENT_EMAIL_1 = "fortnite4life@gmail.com";
    private static final String VALID_CLIENT_PASSWORD_1 = "flosslikeaboss";
    private static final String VALID_CLIENT_PHONENUMBER_1 = "5143913851";
    private static final String VALID_CLIENT_DELIVERYADDRESS_1 = "123 John Street";
    private static final String VALID_CLIENT_USERNAME_2 = "EpicUser123214";
    private static final String VALID_CLIENT_EMAIL_2 = "epic@user.com";
    private static final String VALID_CLIENT_PASSWORD_2 = "epicUser";
    private static final String VALID_CLIENT_PHONENUMBER_2 = "137-1337-1337";
    private static final String VALID_CLIENT_DELIVERYADDRESS_2 = "1337 Epic Street, United States of Epic";

    private static final String VALID_GAME_CATEGORY_1 = "Action";
    private static final String VALID_GAME_CATEGORY_2 = "Adventure";

    private static final String VALID_GAME_TITLE_1 = "Fortnite";
    private static final float VALID_GAME_PRICE_1 = 39.99f;
    private static final String VALID_GAME_DESCRIPTION_1 = "Battle Royale";
    private static final float VALID_GAME_RATING_1 = 4.5f;
    private static final int VALID_GAME_REMAINING_QUANTITY_1 = 100;
    private static final boolean VALID_GAME_IS_OFFERED_1 = true;
    private static final Game.GeneralFeeling VALID_GAME_PUBLIC_OPINION_1 = Game.GeneralFeeling.POSITIVE;

    private static final String VALID_GAME_TITLE_2 = "Minecraft";
    private static final float VALID_GAME_PRICE_2 = 29.99f;
    private static final String VALID_GAME_DESCRIPTION_2 = "Sandbox";
    private static final float VALID_GAME_RATING_2 = 4.8f;
    private static final int VALID_GAME_REMAINING_QUANTITY_2 = 50;
    private static final boolean VALID_GAME_IS_OFFERED_2 = false;
    private static final Game.GeneralFeeling VALID_GAME_PUBLIC_OPINION_2 = Game.GeneralFeeling.VERYPOSITIVE;

    private static final String VALID_GAME_REVIEW_1 = "good game. i like";
    private static final Review.Rating VALID_GAME_REVIEW_RATING_1 = Review.Rating.FIVE;
    private static final String VALID_GAME_REVIEW_2 = "bad game. no like";
    private static final Review.Rating VALID_GAME_REVIEW_RATING_2 = Review.Rating.ONE;
    private static final String VALID_REVIEW_MANAGER_REPLY = "thanks a 1000kg";


    private static final String VALID_GAME_REQUEST_REASON_1 = "I want to play this game, so would our customers.";
    private static final String VALID_GAME_REQUEST_REASON_2 = "No one is buying this game, this should go!";
    private static final String VALID_USER_REQUEST_REASON = "This user leaves rude comments!";

    private int gameId;
    private Client client_1;
    private Client client_2;
    private int reviewId_1;
    private int reviewId_2;

    @BeforeAll
    public void setup() {
        // Create app users
        appUserRepository.save(new Owner(VALID_OWNER_EMAIL, VALID_OWNER_USERNAME, VALID_OWNER_PASSWORD));
        appUserRepository.save(new Employee(VALID_EMPLOYEE_EMAIL, VALID_EMPLOYEE_USERNAME, VALID_EMPLOYEE_PASSWORD));
        this.client_1 = appUserRepository.save(new Client(VALID_CLIENT_EMAIL_1, VALID_CLIENT_USERNAME_1, VALID_CLIENT_PASSWORD_1, VALID_CLIENT_PHONENUMBER_1, VALID_CLIENT_DELIVERYADDRESS_1));
        this.client_2 = appUserRepository.save(new Client(VALID_CLIENT_EMAIL_2, VALID_CLIENT_USERNAME_2, VALID_CLIENT_PASSWORD_2, VALID_CLIENT_PHONENUMBER_2, VALID_CLIENT_DELIVERYADDRESS_2));

        GameCategory gc = gameCategoryRepository.save(new GameCategory(VALID_GAME_CATEGORY_1));
        // Create games and game categories
        Game g = gameRepository.save(new Game(VALID_GAME_TITLE_1, VALID_GAME_PRICE_1, VALID_GAME_DESCRIPTION_1, VALID_GAME_RATING_1, VALID_GAME_REMAINING_QUANTITY_1, VALID_GAME_IS_OFFERED_1, VALID_GAME_PUBLIC_OPINION_1, gc));
        this.gameId = g.getId();
    }

    @AfterAll
    public void cleanup() {
        reviewRepository.deleteAll();
        appUserRepository.deleteAll();
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testAddReviewToGame() {
        ReviewRequestDTO reviewRequestDTO1 = new ReviewRequestDTO(client_1.getUsername(), VALID_GAME_REVIEW_1, VALID_GAME_REVIEW_RATING_1);

        String url = String.format("/reviews/%d", this.gameId);

        ResponseEntity<ReviewResponseDTO> res_1 = client.postForEntity(url, reviewRequestDTO1, ReviewResponseDTO.class);

        assertNotNull(res_1);
        assertEquals(HttpStatus.OK, res_1.getStatusCode());
        ReviewResponseDTO createdReviewResponseDTO1 = res_1.getBody();

        assertNotNull(createdReviewResponseDTO1);
        assertEquals(this.gameId, createdReviewResponseDTO1.getGame().getId());
        assertEquals(this.client_1.getUsername(), createdReviewResponseDTO1.getAuthor());
        assertEquals(createdReviewResponseDTO1.getReviewMessage(), VALID_GAME_REVIEW_1);
        assertEquals(createdReviewResponseDTO1.getRating(), VALID_GAME_REVIEW_RATING_1);
        this.reviewId_1 = createdReviewResponseDTO1.getId();

        ReviewRequestDTO reviewRequestDTO2 = new ReviewRequestDTO(client_2.getUsername(), VALID_GAME_REVIEW_2, VALID_GAME_REVIEW_RATING_2);
        ResponseEntity<ReviewResponseDTO> res_2 = client.postForEntity(url, reviewRequestDTO2, ReviewResponseDTO.class);

        assertNotNull(res_2);
        assertEquals(HttpStatus.OK, res_2.getStatusCode());
        ReviewResponseDTO createdReviewResponseDTO2 = res_2.getBody();

        assertNotNull(createdReviewResponseDTO2);
        assertEquals(this.gameId, createdReviewResponseDTO2.getGame().getId());
        assertEquals(this.client_2.getUsername(), createdReviewResponseDTO2.getAuthor());
        assertEquals(createdReviewResponseDTO2.getReviewMessage(), VALID_GAME_REVIEW_2);
        assertEquals(createdReviewResponseDTO2.getRating(), VALID_GAME_REVIEW_RATING_2);
        this.reviewId_2 = createdReviewResponseDTO2.getId();
    }

    @Test
    @Order(2)
    public void testGetReviewsForGame() {
        String url = String.format("/reviews/%d", this.gameId);

        ResponseEntity<ReviewResponseDTO[]> res = client.getForEntity(url, ReviewResponseDTO[].class);
        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<ReviewResponseDTO> body = List.of(res.getBody());
        assertNotNull(body);
        assertEquals(2, body.size());

        assertEquals(this.reviewId_1, body.getFirst().getId());
        assertEquals(this.reviewId_2, body.getLast().getId());
    }

    @Test
    @Order(3)
    public void managerReplyToReview() {
        ManagerReplyRequestDTO managerReplyRequestDTO = new ManagerReplyRequestDTO(VALID_REVIEW_MANAGER_REPLY);

        String url = String.format("/reviews/%d/managerReply", this.reviewId_1);

        ResponseEntity<ReviewResponseDTO> res = client.postForEntity(url, managerReplyRequestDTO, ReviewResponseDTO.class);
        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        ReviewResponseDTO createdReviewResponseDTO = res.getBody();
        assertNotNull(createdReviewResponseDTO);
        assertEquals(this.reviewId_1, createdReviewResponseDTO.getId());
        assertEquals(VALID_REVIEW_MANAGER_REPLY, createdReviewResponseDTO.getManagerReply());
    }
}
