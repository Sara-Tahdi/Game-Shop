package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.model.Request.Status;
import ca.mcgill.ecse321.gamecenter.model.GameRequest.Type;
import ca.mcgill.ecse321.gamecenter.model.Game.GeneralFeeling;
import ca.mcgill.ecse321.gamecenter.repository.RequestRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.StaffRepository;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RequestServiceTests {
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private StaffRepository staffRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameCategoryRepository gameCategoryRepository;
    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private RequestService requestService;
    @InjectMocks
    private AppUserService appUserService;
    @InjectMocks
    private GameService gameService;
    @InjectMocks
    private GameCategoryService gameCategoryService;

    /* Getter tests */
    @Test
    public void testGetAllRequestsError() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getAllRequests());
        assertEquals("There are no Requests", e.getMessage());
    }

    @Test
    public void testGetAllRequests() {
        String categoryName = "Action";
        GameCategory gameCategory = new GameCategory(categoryName);
        when(gameCategoryRepository.save(any(GameCategory.class))).thenReturn(gameCategory);
        GameCategory createdGameCategory = gameCategoryService.createGameCategory(categoryName);

        String gameTitle = "Call of Duty 3";
        float gamePrice = 59.99f;
        String gameDescription = "Call of Duty 3 is a first-person shooter video game developed by Treyarch and published by Activision.";
        float rating = 4.5f;
        int remainingCopies = 10;
        boolean isOffered = false;
        GeneralFeeling generalFeeling = GeneralFeeling.POSITIVE;
        Game game = new Game(gameTitle, gamePrice, gameDescription, rating, remainingCopies, isOffered, generalFeeling, gameCategory);
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        Game createdGame = gameService.createGame(gameTitle, gamePrice, gameDescription, generalFeeling, createdGameCategory);

        String email = "John.Doe@gmail.com";
        String username = "JohnDoe";
        String password = "password";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        String email2 = "Jane.Doe@gmail.com";
        String username2 = "JaneDoe";
        String password2 = "password2";
        Owner o = new Owner(email2, username2, password2);
        when(appUserRepository.save(any(Owner.class))).thenReturn(o);
        Owner createdOwner = appUserService.createOwnerAccount(email2, username2, password2);

        String email3 = "Bob.Smith@gmail.com";
        String username3 = "BobSmith";
        String password3 = "password3";
        String phoneNumber3 = "0123456789";
        String deliveryAddress3 = "123 Where Am I";
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3, 0);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        Type type = Type.ADD;
        GameRequest gr = new GameRequest(status, createdEmployee, type, createdGame);   
        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        GameRequest createdGameRequest = requestService.addGameRequest(username, gameTitle);

        UserRequest ur = new UserRequest(status, createdOwner, createdClient);
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur);
        UserRequest createdUserRequest = requestService.flagUser(username2, username3);

        when(requestRepository.findRequestsByRequestType(Request.class)).thenReturn(Optional.of(List.of(
            createdGameRequest, createdUserRequest
        )));
        List<Request> requests = requestService.getAllRequests();

        assertNotNull(requests);
        assertEquals(2, requests.size());
    }

    @Test
    public void testGetRequestById() {
        String email2 = "Jane.Doe@gmail.com";
        String username2 = "JaneDoe";
        String password2 = "password2";
        Owner o = new Owner(email2, username2, password2);
        when(appUserRepository.save(any(Owner.class))).thenReturn(o);
        Owner createdOwner = appUserService.createOwnerAccount(email2, username2, password2);

        String email3 = "Bob.Smith@gmail.com";
        String username3 = "BobSmith";
        String password3 = "password3";
        String phoneNumber3 = "0123456789";
        String deliveryAddress3 = "123 Where Am I";
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3, 0);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        UserRequest ur = new UserRequest(status, createdOwner, createdClient);
        ur.setId(23);
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur);
        requestService.flagUser(username2, username3);

        when(requestRepository.findRequestById(ur.getId())).thenReturn(Optional.of(ur));
        UserRequest r = (UserRequest) requestService.getRequestById(ur.getId());

        assertInstanceOf(UserRequest.class, r);
        assertEquals(23, r.getId());
        assertEquals(status, r.getStatus());


        assertInstanceOf(Owner.class, r.getCreatedRequest());
        assertEquals(email2, r.getCreatedRequest().getEmail());
        assertEquals(username2, r.getCreatedRequest().getUsername());
        assertEquals(password2, r.getCreatedRequest().getPassword());

        assertInstanceOf(Client.class, r.getUserFacingJudgement());
        assertEquals(email3, r.getUserFacingJudgement().getEmail());
        assertEquals(username3, r.getUserFacingJudgement().getUsername());
        assertEquals(password3, r.getUserFacingJudgement().getPassword());
        assertEquals(phoneNumber3, r.getUserFacingJudgement().getPhoneNumber());
        assertEquals(deliveryAddress3, r.getUserFacingJudgement().getDeliveryAddress());
    }

    @Test
    public void testGetRequestByIdFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getRequestById(2));
        assertEquals("There is no Request with id: " + 2, e.getMessage());
    }

    @Test
    public void testGetRequestsBy


}
