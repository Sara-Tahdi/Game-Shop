package ca.mcgill.ecse321.gamecenter.integration;


import ca.mcgill.ecse321.gamecenter.dto.AppUsers.ClientRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.AppUsers.ClientResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.AppUsers.EmployeeRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.AppUsers.EmployeeResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Owner;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.utilities.Encryption;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppUserIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private AppUserRepository appUserRepository;

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

    private int appUserId;
    private String appUserUsername;
    private String appUserEmail;

    @BeforeAll
    public void createOwner() {
        appUserRepository.save(new Owner(VALID_OWNER_EMAIL, VALID_OWNER_USERNAME, VALID_OWNER_PASSWORD));
    }

    @AfterAll
    public void clearDatabase() {
        appUserRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateValidEmployee() {
        EmployeeRequestDTO kleenex = new EmployeeRequestDTO(VALID_EMPLOYEE_EMAIL, VALID_EMPLOYEE_USERNAME, VALID_EMPLOYEE_PASSWORD);

        ResponseEntity<EmployeeResponseDTO> res = client.postForEntity("/users/employee/create", kleenex, EmployeeResponseDTO.class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        EmployeeResponseDTO body = res.getBody();
        assertTrue(body.getId() > 0, "The ID should be positive");
        assertEquals(VALID_EMPLOYEE_EMAIL, body.getEmail());
        assertEquals(VALID_EMPLOYEE_USERNAME, body.getUsername());
        assertEquals(VALID_EMPLOYEE_PASSWORD, Encryption.encryptDecrypt(body.getPassword()));

        this.appUserId = body.getId();
        this.appUserEmail = body.getEmail();
        this.appUserUsername = body.getUsername();
    }

    @Test
    @Order(2)
    public void testGetValidEmployeeById() {
        String url = String.format("/users/employee/%d", this.appUserId);

        ResponseEntity<EmployeeResponseDTO> res = client.getForEntity(url, EmployeeResponseDTO.class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        EmployeeResponseDTO body = res.getBody();
        assertEquals(this.appUserId, body.getId());
        assertEquals(this.appUserEmail, body.getEmail());
        assertEquals(this.appUserUsername, body.getUsername());
    }

    @Test
    @Order(3)
    public void testGetValidEmployeeByEmail() {
        String url = String.format("/users/employee/email/%s", this.appUserEmail);

        ResponseEntity<EmployeeResponseDTO> res = client.getForEntity(url, EmployeeResponseDTO.class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        EmployeeResponseDTO body = res.getBody();
        assertEquals(this.appUserId, body.getId());
        assertEquals(this.appUserEmail, body.getEmail());
        assertEquals(this.appUserUsername, body.getUsername());
    }

    @Test
    @Order(4)
    public void testGetAllEmployees() {
        ResponseEntity<EmployeeResponseDTO[]> res = client.getForEntity("/users/employee", EmployeeResponseDTO[].class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<EmployeeResponseDTO> body = Arrays.stream(res.getBody()).toList();
        assertEquals(1, body.size());
        assertEquals(this.appUserId, body.getFirst().getId());
    }

    @Test
    @Order(5)
    public void testUpdateValidEmployeeWithNewUsernameAndPassword() {
        String newUsername = "AmongUs";
        String newPassword = "reehheeehee";

        EmployeeRequestDTO update = new EmployeeRequestDTO(this.appUserEmail, newUsername, newPassword);

        HttpEntity<EmployeeRequestDTO> updateEntity = new HttpEntity<>(update);

        ResponseEntity<EmployeeResponseDTO> res = client.exchange(
                "/users/employee/update",
                HttpMethod.PUT,
                updateEntity,
                EmployeeResponseDTO.class
        );

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        EmployeeResponseDTO body = res.getBody();
        assertEquals(this.appUserId, body.getId());
        assertEquals(this.appUserEmail, body.getEmail());
        assertEquals(newUsername, body.getUsername());
        assertEquals(newPassword, Encryption.encryptDecrypt(body.getPassword()));

        this.appUserUsername = body.getUsername();
    }

    @Test
    @Order(6)
    public void testGetValidEmployeeByUsername() {
        String url = String.format("/users/employee/username/%s", this.appUserUsername);

        ResponseEntity<EmployeeResponseDTO> res = client.getForEntity(url, EmployeeResponseDTO.class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        EmployeeResponseDTO body = res.getBody();
        assertEquals(this.appUserId, body.getId());
        assertEquals(this.appUserEmail, body.getEmail());
        assertEquals(this.appUserUsername, body.getUsername());
    }

    @Test
    @Order(7)
    public void testFireEmployee() {
        String url = String.format("/users/employee/fire/%s", this.appUserUsername);

        ResponseEntity<EmployeeResponseDTO> res = client.exchange(
                url,
                HttpMethod.PUT,
                null,
                EmployeeResponseDTO.class
        );

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        EmployeeResponseDTO body = res.getBody();
        assertEquals(this.appUserId, body.getId());
        assertEquals(this.appUserEmail, body.getEmail());
        assertEquals(this.appUserUsername, body.getUsername());
        assertFalse(body.getIsActive());
    }

    @Test
    @Order(8)
    public void testCreateValidClient() {
        ClientRequestDTO noob = new ClientRequestDTO(
                VALID_CLIENT_EMAIL,
                VALID_CLIENT_USERNAME,
                VALID_CLIENT_PASSWORD,
                VALID_CLIENT_PHONENUMBER,
                VALID_CLIENT_DELIVERYADDRESS
        );

        ResponseEntity<ClientResponseDTO> res = client.postForEntity("/users/client/create", noob, ClientResponseDTO.class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        ClientResponseDTO body = res.getBody();
        assertTrue(body.getId() > 0, "The ID should be positive");
        assertEquals(VALID_CLIENT_EMAIL, body.getEmail());
        assertEquals(VALID_CLIENT_USERNAME, body.getUsername());
        assertEquals(VALID_CLIENT_PASSWORD, Encryption.encryptDecrypt(body.getPassword()));
        assertEquals(VALID_CLIENT_PHONENUMBER, body.getPhoneNumber());
        assertEquals(VALID_CLIENT_DELIVERYADDRESS, body.getDeliveryAddress());

        this.appUserId = body.getId();
        this.appUserEmail = body.getEmail();
        this.appUserUsername = body.getUsername();
    }

    @Test
    @Order(9)
    public void testGetValidClientById() {
        String url = String.format("/users/client/%d", this.appUserId);

        ResponseEntity<ClientResponseDTO> res = client.getForEntity(url, ClientResponseDTO.class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        ClientResponseDTO body = res.getBody();
        assertEquals(this.appUserId, body.getId());
        assertEquals(this.appUserEmail, body.getEmail());
        assertEquals(this.appUserUsername, body.getUsername());
    }

    @Test
    @Order(10)
    public void testGetValidClientByEmail() {
        String url = String.format("/users/client/email/%s", this.appUserEmail);

        ResponseEntity<ClientResponseDTO> res = client.getForEntity(url, ClientResponseDTO.class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        ClientResponseDTO body = res.getBody();
        assertEquals(this.appUserId, body.getId());
        assertEquals(this.appUserEmail, body.getEmail());
        assertEquals(this.appUserUsername, body.getUsername());
    }

    @Test
    @Order(11)
    public void testGetAllClients() {
        ResponseEntity<ClientResponseDTO[]> res = client.getForEntity("/users/client", ClientResponseDTO[].class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<ClientResponseDTO> body = Arrays.stream(res.getBody()).toList();
        assertEquals(1, body.size());
        assertEquals(this.appUserId, body.getFirst().getId());
    }

    @Test
    @Order(12)
    public void testUpdateValidClientWithNewUsernameAndPassword() {
        String newUsername = "Skibidi";
        String newPassword = "reehheeehee";

        ClientRequestDTO update = new ClientRequestDTO(this.appUserEmail, newUsername, newPassword, VALID_CLIENT_PHONENUMBER, VALID_CLIENT_DELIVERYADDRESS);

        HttpEntity<ClientRequestDTO> updateEntity = new HttpEntity<>(update);

        ResponseEntity<ClientResponseDTO> res = client.exchange(
                "/users/client/update",
                HttpMethod.PUT,
                updateEntity,
                ClientResponseDTO.class
        );

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        ClientResponseDTO body = res.getBody();
        assertEquals(this.appUserId, body.getId());
        assertEquals(this.appUserEmail, body.getEmail());
        assertEquals(newUsername, body.getUsername());
        assertEquals(newPassword, Encryption.encryptDecrypt(body.getPassword()));

        this.appUserUsername = body.getUsername();
    }

    @Test
    @Order(13)
    public void testGetValidClientByUsername() {
        String url = String.format("/users/client/username/%s", this.appUserUsername);

        ResponseEntity<ClientResponseDTO> res = client.getForEntity(url, ClientResponseDTO.class);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        ClientResponseDTO body = res.getBody();
        assertEquals(this.appUserId, body.getId());
        assertEquals(this.appUserEmail, body.getEmail());
        assertEquals(this.appUserUsername, body.getUsername());
    }

    @Test
    @Order(14)
    public void testBanClient() {
        String url = String.format("/users/client/ban/%s", this.appUserUsername);

        ResponseEntity<ClientResponseDTO> res = client.exchange(
                url,
                HttpMethod.PUT,
                null,
                ClientResponseDTO.class
        );

        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        ClientResponseDTO body = res.getBody();
        assertEquals(this.appUserId, body.getId());
        assertEquals(this.appUserEmail, body.getEmail());
        assertEquals(this.appUserUsername, body.getUsername());
        assertFalse(body.getIsActive());
    }
}
