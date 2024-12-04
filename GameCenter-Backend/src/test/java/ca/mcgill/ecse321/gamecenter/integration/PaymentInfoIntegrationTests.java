package ca.mcgill.ecse321.gamecenter.integration;

import ca.mcgill.ecse321.gamecenter.dto.PaymentInfo.PaymentInfoRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.PaymentInfo.PaymentInfoResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Client;
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
    private static final int VALID_CLIENT_CARDEXPIRYYEAR = 2028;
    private static final String VALID_CLIENT_CVV = "001";

    private int clientId;
    private int paymentInfoId;

    @BeforeAll
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
        String url = String.format("/paymentInfo/%d", this.clientId);

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
        assertEquals(this.clientId, createdPaymentInfoResponseDTO.getClientId());

        this.paymentInfoId = createdPaymentInfoResponseDTO.getId();
    }

    @Test
    @Order(2)
    public void testGetClientPaymentInfos() {
        String url = String.format("/paymentInfo/%d", this.clientId);

        ResponseEntity<PaymentInfoResponseDTO[]> res = client.getForEntity(url, PaymentInfoResponseDTO[].class);
        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<PaymentInfoResponseDTO> body = List.of(res.getBody());
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals(this.clientId, body.getFirst().getClientId());
        assertEquals(this.paymentInfoId, body.getFirst().getId());
    }

    @Test
    @Order(3)
    public void testDeletePaymentInfo() {
        String url_1 = String.format("/paymentInfo/%d", this.paymentInfoId);
        client.delete(url_1);

        String url_2 = String.format("/paymentInfo/%d", this.clientId);
        ResponseEntity<PaymentInfoResponseDTO[]> res_2 = client.getForEntity(url_2, PaymentInfoResponseDTO[].class);
        assertNotNull(res_2);
        assertEquals(HttpStatus.OK, res_2.getStatusCode());
        List<PaymentInfoResponseDTO> body = List.of(res_2.getBody());
        assertNotNull(body);
        assertEquals(0, body.size());
    }
}
