package ca.mcgill.ecse321.gamecenter.integration;

import ca.mcgill.ecse321.gamecenter.dto.PaymentInfo.PaymentInfoRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.PaymentInfo.PaymentInfoResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Purchase.PurchaseResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Owner;
import ca.mcgill.ecse321.gamecenter.model.PaymentInfo;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.PaymentInfoRepository;
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
public class PaymentInfoIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private PaymentInfoRepository paymentInfoRepository;
    @Autowired
    private ClientRepository appUserRepository;

    private static final String VALID_CLIENT_USERNAME = "GamerNoob";
    private static final String VALID_CLIENT_EMAIL = "fortnite3life@gmail.com";
    private static final String VALID_CLIENT_PASSWORD = "flosslikeaboss";
    private static final String VALID_CLIENT_PHONENUMBER = "5143913850";
    private static final String VALID_CLIENT_DELIVERYADDRESS = "122 John Street";
    private static final String VALID_CLIENT_CARDNUMBER = "1234567890123456";
    private static final int VALID_CLIENT_CARDEXPIRYMONTH = 5;
    private static final int VALID_CLIENT_CARDEXPIRYYEAR = 28;
    private static final int VALID_CLIENT_CVV = 900;

    private int clientId;
    private int paymentInfoId;

    @BeforeEach
    public void setup() {
        Client c = appUserRepository.save(new Client(
                VALID_CLIENT_EMAIL,
                VALID_CLIENT_USERNAME,
                VALID_CLIENT_PASSWORD,
                VALID_CLIENT_PHONENUMBER,
                VALID_CLIENT_DELIVERYADDRESS
        ));
        this.clientId = c.getId();
    }

    @AfterAll
    public void cleanup() {
        paymentInfoRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testAddPaymentInfo() {
        String url = String.format("/paymentInfo/add/%d", this.clientId);

        PaymentInfoRequestDTO paymentInfoRequestDTO = new PaymentInfoRequestDTO(
            VALID_CLIENT_CARDNUMBER,
            VALID_CLIENT_CVV,
            VALID_CLIENT_CARDEXPIRYMONTH,
            VALID_CLIENT_CARDEXPIRYYEAR
        );

        ResponseEntity<PaymentInfoResponseDTO> res = client.postForEntity(url, paymentInfoRequestDTO, PaymentInfoResponseDTO.class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        PaymentInfoResponseDTO createdPaymentInfoResponseDTO = res.getBody();
        assertNotNull(createdPaymentInfoResponseDTO);
        assertEquals(VALID_CLIENT_CARDNUMBER.substring(VALID_CLIENT_CARDNUMBER.length() - 4), createdPaymentInfoResponseDTO.getLastFourDigits());
        assertEquals(VALID_CLIENT_CARDEXPIRYMONTH, createdPaymentInfoResponseDTO.getExpiryMonth());
        assertEquals(VALID_CLIENT_CARDEXPIRYYEAR, createdPaymentInfoResponseDTO.getExpiryYear());
        assertEquals(this.clientId, createdPaymentInfoResponseDTO.getClient().getId());

        this.paymentInfoId = createdPaymentInfoResponseDTO.getId();
    }
}
