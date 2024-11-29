package ca.mcgill.ecse321.gamecenter.integration;

import ca.mcgill.ecse321.gamecenter.dto.Purchase.PurchaseRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Purchase.PurchaseResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Purchase.RefundRequestDTO;
import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.PurchaseRepository;
import ca.mcgill.ecse321.gamecenter.dto.Purchase.SimplePurchaseResponseDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PurchaseIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private GameRepository gameRepository;
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
    private static final int VALID_PURCHASE_COPIES = 3;
    private static final String VALID_GAME_NAME_2 = "GTA V";
    private static final float VALID_GAME_PRICE_2 = 49.99f;
    private static final String VALID_GAME_DESCRIPTION_2 = "Open world game to become rich";
    private static final Game.GeneralFeeling VALID_GAME_GENERAL_FEELING_2 = Game.GeneralFeeling.VERYPOSITIVE;
    private static final int VALID_GAME_STOCK_2 = 6;
    private static final int VALID_PURCHASE_COPIES_2 = 4;
    private static final String VALID_REFUND_REASON = "Not feeling this game anymore";

    private int clientId;
    private int gameId_1;
    private int gameId_2;
    private int purchaseId_1;
    private int purchaseId_2;


    @BeforeAll
    public void setupDatabase() {
        appUserRepository.save(new Owner(
                VALID_OWNER_EMAIL,
                VALID_OWNER_USERNAME,
                VALID_OWNER_PASSWORD
        ));
        Client c = appUserRepository.save(new Client(
                VALID_CLIENT_EMAIL,
                VALID_CLIENT_USERNAME,
                VALID_CLIENT_PASSWORD,
                VALID_CLIENT_PHONENUMBER,
                VALID_CLIENT_DELIVERYADDRESS
        ));
        this.clientId = c.getId();
        GameCategory gameCategory = gameCategoryRepository.save(new GameCategory(VALID_GAME_CATEGORY));
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
        Game g2 = gameRepository.save(new Game(
                VALID_GAME_NAME_2,
                VALID_GAME_PRICE_2,
                VALID_GAME_DESCRIPTION_2,
                0,
                VALID_GAME_STOCK_2,
                true,
                VALID_GAME_GENERAL_FEELING_2,
                gameCategory
        ));
        this.gameId_2 = g2.getId();
    }

    @AfterAll
    public void clearDatabase() {
        purchaseRepository.deleteAll();
        appUserRepository.deleteAll();
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreatePurchases() {
        String url = String.format("/purchases/place/%d", this.clientId);

        PurchaseRequestDTO purchaseRequestDTO1 = new PurchaseRequestDTO(this.clientId, this.gameId_1, VALID_PURCHASE_COPIES);
        PurchaseRequestDTO purchaseRequestDTO2 = new PurchaseRequestDTO(this.clientId, this.gameId_2, VALID_PURCHASE_COPIES_2);

        List<PurchaseRequestDTO> purchaseRequests = List.of(purchaseRequestDTO1, purchaseRequestDTO2);

        ResponseEntity<PurchaseResponseDTO[]> res = client.postForEntity(url, purchaseRequests, PurchaseResponseDTO[].class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<PurchaseResponseDTO> body = List.of(res.getBody());
        assertEquals(2, body.size());

        assertEquals(this.clientId, body.getFirst().getClient().getId());
        assertEquals(this.gameId_1, body.getFirst().getGame().getId());
        assertEquals(VALID_PURCHASE_COPIES, body.getFirst().getCopies());

        assertEquals(this.clientId, body.getLast().getClient().getId());
        assertEquals(this.gameId_2, body.getLast().getGame().getId());
        assertEquals(VALID_PURCHASE_COPIES_2, body.getLast().getCopies());

        assertEquals(body.getFirst().getTrackingCode(), body.getLast().getTrackingCode());

        this.purchaseId_1 = body.getFirst().getId();
        this.purchaseId_2 = body.getLast().getId();
    }

    @Test
    @Order(2)
    public void testRefundGame() {
        String url = String.format("/purchases/refund/%d", this.purchaseId_1);

        RefundRequestDTO refund = new RefundRequestDTO(VALID_REFUND_REASON);

        HttpEntity<RefundRequestDTO> refundRequest = new HttpEntity<>(refund);

        ResponseEntity<PurchaseResponseDTO> res = client.exchange(
                url,
                HttpMethod.PUT,
                refundRequest,
                PurchaseResponseDTO.class
        );

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        PurchaseResponseDTO body = res.getBody();
        assertNotNull(body.getRefundReason());
        assertEquals(VALID_REFUND_REASON, body.getRefundReason());
    }

    @Test
    @Order(3)
    public void testGetPurchaseHistory() {
        String url = String.format("/purchases/%d", this.clientId);

        ResponseEntity<SimplePurchaseResponseDTO[]> res = client.getForEntity(url, SimplePurchaseResponseDTO[].class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<SimplePurchaseResponseDTO> body = List.of(res.getBody());
        assertEquals(1, body.size());
    }
}
