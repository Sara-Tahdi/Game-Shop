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
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        Type type = Type.ADD;
        GameRequest gr = new GameRequest(status, "", createdEmployee, type, createdGame);
        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());
        GameRequest createdGameRequest = requestService.addGameRequest(username, gameTitle, "");

        UserRequest ur = new UserRequest(status, "", createdOwner, createdClient);
        when(staffRepository.findStaffByUsername(username2)).thenReturn(Optional.of(o));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username2)).thenReturn(Optional.empty());  
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur); 
        UserRequest createdUserRequest = requestService.flagUser(username2, username3, "");

        when(requestRepository.findRequestsByRequestType(Request.class)).thenReturn(Optional.of(List.of(
            createdGameRequest, createdUserRequest
        )));
        List<Request> requests = requestService.getAllRequests();

        assertNotNull(requests);
        assertEquals(2, requests.size());
    }

    @Test
    public void testGetAllRequestsError() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getAllRequests());
        assertEquals("There are no Requests", e.getMessage());
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
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        UserRequest ur = new UserRequest(status, "", createdOwner, createdClient);
        ur.setId(23);
        when(staffRepository.findStaffByUsername(username2)).thenReturn(Optional.of(o));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username2)).thenReturn(Optional.empty());  
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur); 
        requestService.flagUser(username2, username3, "");

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
    public void testGetRequestsByCreatedRequestId() {
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
        e.setId(23);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        String email3 = "Bob.Smith@gmail.com";
        String username3 = "BobSmith";
        String password3 = "password3";
        String phoneNumber3 = "0123456789";
        String deliveryAddress3 = "123 Where Am I";
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        Type type = Type.ADD;
        GameRequest gr = new GameRequest(status, "", createdEmployee, type, createdGame);
        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());
        GameRequest createdGameRequest = requestService.addGameRequest(username, gameTitle, "");

        UserRequest ur = new UserRequest(status, "", createdEmployee, createdClient);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());  
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur); 
        UserRequest createdUserRequest = requestService.flagUser(username, username3, "");

        when(requestRepository.findRequestsByCreatedRequestId(e.getId())).thenReturn(Optional.of(List.of(
            createdGameRequest, createdUserRequest
        )));
        List<Request> requests = requestService.getRequestsByCreatedRequestId(23);

        assertNotNull(requests);
        assertEquals(2, requests.size());
    }

    @Test
    public void testGetRequestsByCreatedRequestIdFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getRequestsByCreatedRequestId(2));
        assertEquals("There are no Requests with createdRequestId: " + 2, e.getMessage());
    }

    @Test
    public void testGetRequestsByCreatedRequestUsername() {
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
        e.setId(23);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        String email3 = "Bob.Smith@gmail.com";
        String username3 = "BobSmith";
        String password3 = "password3";
        String phoneNumber3 = "0123456789";
        String deliveryAddress3 = "123 Where Am I";
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        Type type = Type.ADD;
        GameRequest gr = new GameRequest(status, "", createdEmployee, type, createdGame);
        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());
        GameRequest createdGameRequest = requestService.addGameRequest(username, gameTitle, "");

        UserRequest ur = new UserRequest(status, "", createdEmployee, createdClient);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.of(List.of(createdGameRequest)));  
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur); 
        UserRequest createdUserRequest = requestService.flagUser(username, username3, "");

        when(requestRepository.findRequestsByCreatedRequestUsername(e.getUsername())).thenReturn(Optional.of(List.of(
            createdGameRequest, createdUserRequest
        )));
        List<Request> requests = requestService.getRequestsByCreatedRequestUsername(username);

        assertNotNull(requests);
        assertEquals(2, requests.size());
    }

    @Test
    public void testGetRequestsByCreatedRequestUsernameFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getRequestsByCreatedRequestUsername("JohnDoe"));
        assertEquals("There are no Requests with createdRequestsUsername: " + "JohnDoe", e.getMessage());
    }

    @Test
    public void testGetRequestsByCreatedRequestEmail() {
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
        e.setId(23);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        String email3 = "Bob.Smith@gmail.com";
        String username3 = "BobSmith";
        String password3 = "password3";
        String phoneNumber3 = "0123456789";
        String deliveryAddress3 = "123 Where Am I";
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        Type type = Type.ADD;
        GameRequest gr = new GameRequest(status, "", createdEmployee, type, createdGame);
        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());
        GameRequest createdGameRequest = requestService.addGameRequest(username, gameTitle, "");

        UserRequest ur = new UserRequest(status, "", createdEmployee, createdClient);

        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.of(List.of(createdGameRequest))); 
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur); 
        UserRequest createdUserRequest = requestService.flagUser(username, username3, "");

        when(requestRepository.findRequestsByCreatedRequestEmail(e.getEmail())).thenReturn(Optional.of(List.of(
            createdGameRequest, createdUserRequest
        )));
        List<Request> requests = requestService.getRequestsByCreatedRequestEmail(email);

        assertNotNull(requests);
        assertEquals(2, requests.size());
    }

    @Test
    public void testGetRequestsByCreatedRequestEmailFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getRequestsByCreatedRequestEmail("John.Doe@gmail.com"));
                assertEquals("There are no Requests with createdRequestEmail: " + "John.Doe@gmail.com", e.getMessage());
    }

    @Test
    public void testGetRequestsByGameTitle() {
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

        Status status = Status.PENDING;
        Type type = Type.ADD;
        GameRequest gr = new GameRequest(status, "", createdEmployee, type, createdGame);
        gr.setId(23);  
        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());
        GameRequest createdGameRequest = requestService.addGameRequest(username, gameTitle, "");

        when(requestRepository.findRequestsByGameTitle(gr.getGame().getTitle())).thenReturn(Optional.of(List.of(createdGameRequest)));
        List<GameRequest> r = requestService.getRequestsByGameTitle(gr.getGame().getTitle());

        assertNotNull(r);
        assertEquals(1, r.size());
    }

    @Test
    public void testGetRequestsByGameTitleFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getRequestsByGameTitle("Call of Duty 3"));
        assertEquals("There are no Requests with gameTitle: " + "Call of Duty 3", e.getMessage());
    }

    @Test
    public void testGetRequestByGameId() {
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

        Status status = Status.PENDING;
        Type type = Type.ADD;
        GameRequest gr = new GameRequest(status, "", createdEmployee, type, createdGame);
        gr.setId(23);  
        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());
        GameRequest createdGameRequest = requestService.addGameRequest(username, gameTitle, "");

        when(requestRepository.findRequestsByGameId(gr.getGame().getId())).thenReturn(Optional.of(List.of(createdGameRequest)));
        List<GameRequest> r = requestService.getRequestsByGameId(gr.getGame().getId());

        assertNotNull(r);
        assertEquals(1, r.size());
    }

    @Test
    public void testGetRequestsByGameIdFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getRequestsByGameId(23));
        assertEquals("There are no Requests with gameId: " + 23, e.getMessage());
    }

    @Test
    public void testGetAllGameRequests() {
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
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        Type type = Type.ADD;
        GameRequest gr = new GameRequest(status, "", createdEmployee, type, createdGame);
        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());
        GameRequest createdGameRequest = requestService.addGameRequest(username, gameTitle, "");

        UserRequest ur = new UserRequest(status, "", createdOwner, createdClient);
        when(staffRepository.findStaffByUsername(username2)).thenReturn(Optional.of(o));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username2)).thenReturn(Optional.empty());  
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur); 
        requestService.flagUser(username2, username3, "");

        when(requestRepository.findRequestsByRequestType(GameRequest.class)).thenReturn(Optional.of(List.of(
            createdGameRequest
        )));
        List<GameRequest> requests = requestService.getAllGameRequests();

        assertNotNull(requests);
        assertEquals(1, requests.size());
    }

    @Test
    public void testGetAllGameRequestsFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getAllGameRequests());
        assertEquals("There are no GameRequests", e.getMessage());
    }

    @Test
    public void testGetAllUserRequests() {
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
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        Type type = Type.ADD;
        GameRequest gr = new GameRequest(status, "", createdEmployee, type, createdGame);
        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());
        requestService.addGameRequest(username, gameTitle, "");

        UserRequest ur = new UserRequest(status, "", createdOwner, createdClient);
        when(staffRepository.findStaffByUsername(username2)).thenReturn(Optional.of(o));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username2)).thenReturn(Optional.empty());  
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur); 
        UserRequest createdUserRequest = requestService.flagUser(username2, username3, "");

        when(requestRepository.findRequestsByRequestType(UserRequest.class)).thenReturn(Optional.of(List.of(
            createdUserRequest
        )));
        List<UserRequest> requests = requestService.getAllUserRequests();

        assertNotNull(requests);
        assertEquals(1, requests.size());
    }

    @Test
    public void testGetAllUserRequestsFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getAllUserRequests());
        assertEquals("There are no UserRequests", e.getMessage());
    }

    @Test
    public void testGetRequestsByStatus() {
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
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        Type type = Type.ADD;
        GameRequest gr = new GameRequest(status, "", createdEmployee, type, createdGame);
        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());
        GameRequest createdGameRequest = requestService.addGameRequest(username, gameTitle, "");

        UserRequest ur = new UserRequest(status, "", createdOwner, createdClient);
        when(staffRepository.findStaffByUsername(username2)).thenReturn(Optional.of(o));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username2)).thenReturn(Optional.empty());  
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur); 
        UserRequest createdUserRequest = requestService.flagUser(username2, username3, "");

        when(requestRepository.findRequestsByStatus(gr.getStatus())).thenReturn(Optional.of(List.of(
            createdGameRequest, createdUserRequest
        )));
        List<Request> requests = requestService.getRequestsByStatus(status);

        assertNotNull(requests);
        assertEquals(2, requests.size());
    }

    @Test
    public void testGetRequestsByStatusFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getRequestsByStatus(Status.PENDING));
        assertEquals("There are no Requests with status: " + Status.PENDING, e.getMessage());
    }

    @Test
    public void testFlagUser() {
        String email = "John.Doe@gmail.com";
        String username = "JohnDoe";
        String password = "password";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        String email3 = "Bob.Smith@gmail.com";
        String username3 = "BobSmith";
        String password3 = "password3";
        String phoneNumber3 = "0123456789";
        String deliveryAddress3 = "123 Where Am I";
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        UserRequest ur = new UserRequest(status, "", createdEmployee, createdClient);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());  
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur);
        UserRequest createdUserRequest = requestService.flagUser(username, username3, "");

        assertNotNull(createdUserRequest);
        assertEquals(status, createdUserRequest.getStatus());
        assertEquals(createdEmployee, createdUserRequest.getCreatedRequest());
        assertEquals(createdClient, createdUserRequest.getUserFacingJudgement());
    }

    @Test
    public void testFlagUserFailMissingStaff() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.flagUser("JohnDoe", "BobSmith", ""));
        assertEquals("There is no Staff with username: " + "JohnDoe", e.getMessage());
    }

    @Test
    public void testFlagUserFailMissingClient() {
        String email = "John.Doe@gmail.com";
        String username = "JohnDoe";
        String password = "password";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        appUserService.createEmployeeAccount(email, username, password);


        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                requestService.flagUser("JohnDoe", "BobSmith", ""));
        assertEquals("There is no Client with username: " + "BobSmith", err.getMessage());
    }

    @Test
    public void testFlagUserFailAlreadyFlagged() {
        String email = "John.Doe@gmail.com";
        String username = "JohnDoe";
        String password = "password";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        String email3 = "Bob.Smith@gmail.com";
        String username3 = "BobSmith";
        String password3 = "password3";
        String phoneNumber3 = "0123456789";
        String deliveryAddress3 = "123 Where Am I";
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        UserRequest ur = new UserRequest(status, "", createdEmployee, createdClient);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());  
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur); 
        UserRequest createdRequest = requestService.flagUser(username, username3, "");

        
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.of(List.of(createdRequest)));  
        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                requestService.flagUser("JohnDoe", "BobSmith", ""));
        assertEquals("There is already a request from " + "JohnDoe" + " regarding " + "BobSmith", err.getMessage());
    }

    @Test
    public void testAddGameRequest() {
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

        Status status = Status.PENDING;
        Type type = Type.ADD;
        GameRequest gr = new GameRequest(status, "", createdEmployee, type, createdGame);
        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());
        GameRequest createdGameRequest = requestService.addGameRequest(username, gameTitle, "");

        assertNotNull(createdGameRequest);
        assertEquals(status, createdGameRequest.getStatus());
        assertEquals(type, createdGameRequest.getType());
        assertEquals(createdEmployee, createdGameRequest.getCreatedRequest());
        assertEquals(createdGame, createdGameRequest.getGame());
    }

    @Test
    public void testAddGameRequestFailMissingStaff() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.addGameRequest("JohnDoe", "Call of Duty 3", ""));
        assertEquals("There is no Staff with username: " + "JohnDoe", e.getMessage());
    }

    @Test
    public void testAddGameRequestFailMissingGame() {
        String email = "John.Doe@gmail.com";
        String username = "JohnDoe";
        String password = "password";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        appUserService.createEmployeeAccount(email, username, password);

        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                requestService.addGameRequest("JohnDoe", "Call of Duty 3", ""));
        assertEquals("There is no Game with title: " + "Call of Duty 3", err.getMessage());
    }

    @Test
    public void testAddGameRequestFailAlreadyRequested() {
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

        Status status = Status.PENDING;
        Type type = Type.ADD;
        GameRequest gr = new GameRequest(status, "", createdEmployee, type, createdGame);
        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());
        GameRequest createdGameRequest = requestService.addGameRequest(username, gameTitle, "");

        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.of(List.of(createdGameRequest)));
        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                requestService.addGameRequest("JohnDoe", "Call of Duty 3", ""));
        assertEquals("There is already a request of type " + "ADD" + " from " + "JohnDoe" + " regarding " + "Call of Duty 3", err.getMessage());
    }

    @Test
    public void testRemoveGameRequest() {
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

        Status status = Status.PENDING;
        Type type = Type.REMOVE;
        GameRequest gr = new GameRequest(status, "", createdEmployee, type, createdGame);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());
        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        GameRequest createdGameRequest = requestService.removeGameRequest(username, gameTitle, "");

        assertNotNull(createdGameRequest);
        assertEquals(status, createdGameRequest.getStatus());
        assertEquals(type, createdGameRequest.getType());
        assertEquals(createdEmployee, createdGameRequest.getCreatedRequest());
        assertEquals(createdGame, createdGameRequest.getGame());
    }

    @Test
    public void testRemoveGameRequestFailMissingStaff() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.removeGameRequest("JohnDoe", "Call of Duty 3", ""));
        assertEquals("There is no Staff with username: " + "JohnDoe", e.getMessage());
    }

    @Test
    public void testRemoveGameRequestFailMissingGame() {
        String email = "John.Doe@gmail.com";
        String username = "JohnDoe";
        String password = "password";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        appUserService.createEmployeeAccount(email, username, password);

        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                requestService.removeGameRequest("JohnDoe", "Call of Duty 3", ""));
        assertEquals("There is no Game with title: " + "Call of Duty 3", err.getMessage());
    }

    @Test
    public void testRemoveGameRequestFailAlreadyRequested() {
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

        Status status = Status.PENDING;
        Type type = Type.REMOVE;
        GameRequest gr = new GameRequest(status, "", createdEmployee, type, createdGame);

        when(requestRepository.save(any(GameRequest.class))).thenReturn(gr);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());
        GameRequest createdGameRequest = requestService.removeGameRequest(username, gameTitle, "");

        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(gameRepository.findGameByTitle(gameTitle)).thenReturn(Optional.of(createdGame));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.of(List.of(createdGameRequest)));
        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                requestService.removeGameRequest("JohnDoe", "Call of Duty 3", ""));
        assertEquals("There is already a request of type " + "REMOVE" + " from " + "JohnDoe" + " regarding " + "Call of Duty 3", err.getMessage());
    }

    @Test
    public void testHandleRequestApprovalApproved() {
        String email = "John.Doe@gmail.com";
        String username = "JohnDoe";
        String password = "password";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        String email3 = "Bob.Smith@gmail.com";
        String username3 = "BobSmith";
        String password3 = "password3";
        String phoneNumber3 = "0123456789";
        String deliveryAddress3 = "123 Where Am I";
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        UserRequest ur = new UserRequest(status, "", createdEmployee, createdClient);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty()); 
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur); 
        UserRequest createdUserRequest = requestService.flagUser(username, username3, "");

        when(requestRepository.findRequestById(createdUserRequest.getId())).thenReturn(Optional.of(createdUserRequest));
        createdUserRequest.setStatus(Status.APPROVED);
        when(requestRepository.save(any(UserRequest.class))).thenReturn(createdUserRequest);
        Request updatedUserRequest = requestService.handleRequestApproval(createdUserRequest.getId(), true);

        assertInstanceOf(UserRequest.class, updatedUserRequest);
        assertEquals(Status.APPROVED, updatedUserRequest.getStatus());
    }

    @Test
    public void testHandleRequestApprovalDenied() {
        String email = "John.Doe@gmail.com";
        String username = "JohnDoe";
        String password = "password";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        String email3 = "Bob.Smith@gmail.com";
        String username3 = "BobSmith";
        String password3 = "password3";
        String phoneNumber3 = "0123456789";
        String deliveryAddress3 = "123 Where Am I";
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        UserRequest ur = new UserRequest(status, "", createdEmployee, createdClient);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());  
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur);
        UserRequest createdUserRequest = requestService.flagUser(username, username3, "");

        createdUserRequest.setStatus(Status.DENIED);
        when(requestRepository.findRequestById(createdUserRequest.getId())).thenReturn(Optional.of(createdUserRequest));
        createdUserRequest.setStatus(Status.DENIED);
        when(requestRepository.save(any(UserRequest.class))).thenReturn(createdUserRequest);
        Request updatedUserRequest = requestService.handleRequestApproval(createdUserRequest.getId(), false);

        assertInstanceOf(UserRequest.class, updatedUserRequest);
        assertEquals(Status.DENIED, updatedUserRequest.getStatus());
    }

    @Test
    public void testHandleRequestApprovalFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.handleRequestApproval(23, true));
        assertEquals("There is no Request with id: " + 23, e.getMessage());
    }

    @Test
    public void testGetRequestByUserFacingJudgementId() {
        String email = "John.Doe@gmail.com";
        String username = "JohnDoe";
        String password = "password";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        String email3 = "Bob.Smith@gmail.com";
        String username3 = "BobSmith";
        String password3 = "password3";
        String phoneNumber3 = "0123456789";
        String deliveryAddress3 = "123 Where Am I";
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        UserRequest ur = new UserRequest(status, "", createdEmployee, createdClient);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());  
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur); 
        UserRequest createdUserRequest = requestService.flagUser(username, username3, "");

        when(requestRepository.findRequestsByUserFacingJudgementId(ur.getUserFacingJudgement().getId())).thenReturn(Optional.of(List.of(createdUserRequest)));
        List<UserRequest> requests = requestService.getRequestsByUserFacingJudgementId(ur.getUserFacingJudgement().getId());

        assertNotNull(requests);
        assertEquals(1, requests.size());
    }

    @Test
    public void testGetRequestsByUserFacingJudgementIdFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getRequestsByUserFacingJudgementId(23));
        assertEquals("There are no Requests with userFacingJudgementId: " + 23, e.getMessage());
    }

    @Test
    public void testGetRequestByUserFacingJudgementUsername() {
        String email = "John.Doe@gmail.com";
        String username = "JohnDoe";
        String password = "password";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        String email3 = "Bob.Smith@gmail.com";
        String username3 = "BobSmith";
        String password3 = "password3";
        String phoneNumber3 = "0123456789";
        String deliveryAddress3 = "123 Where Am I";
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        UserRequest ur = new UserRequest(status, "", createdEmployee, createdClient);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());  
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur); 
        UserRequest createdUserRequest = requestService.flagUser(username, username3, "");

        when(requestRepository.findRequestsByUserFacingJudgementUsername(ur.getUserFacingJudgement().getUsername())).thenReturn(Optional.of(List.of(createdUserRequest)));
        List<UserRequest> requests = requestService.getRequestsByUserFacingJudgementUsername(ur.getUserFacingJudgement().getUsername());

        assertNotNull(requests);
        assertEquals(1, requests.size());
    }

    @Test
    public void testGetRequestsByUserFacingJudgementUsernameFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getRequestsByUserFacingJudgementUsername("BobSmith"));
        assertEquals("There are no Requests with userFacingJudgementUsername: " + "BobSmith", e.getMessage());
    }

    @Test
    public void testGetRequestByUserFacingJudgementEmail() {
        String email = "John.Doe@gmail.com";
        String username = "JohnDoe";
        String password = "password";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        String email3 = "Bob.Smith@gmail.com";
        String username3 = "BobSmith";
        String password3 = "password3";
        String phoneNumber3 = "0123456789";
        String deliveryAddress3 = "123 Where Am I";
        Client c = new Client(email3, username3, password3, phoneNumber3, deliveryAddress3);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient = appUserService.createClientAccount(email3, username3, password3, phoneNumber3, deliveryAddress3);

        Status status = Status.PENDING;
        UserRequest ur = new UserRequest(status, "", createdEmployee, createdClient);
        when(staffRepository.findStaffByUsername(username)).thenReturn(Optional.of(e));
        when(clientRepository.findClientByUsername(username3)).thenReturn(Optional.of(c));
        when(requestRepository.findRequestsByCreatedRequestUsername(username)).thenReturn(Optional.empty());  
        when(requestRepository.save(any(UserRequest.class))).thenReturn(ur); 
        UserRequest createdUserRequest = requestService.flagUser(username, username3, "");

        when(requestRepository.findRequestsByUserFacingJudgementEmail(ur.getUserFacingJudgement().getEmail())).thenReturn(Optional.of(List.of(createdUserRequest)));
        List<UserRequest> requests = requestService.getRequestsByUserFacingJudgementEmail(ur.getUserFacingJudgement().getEmail());

        assertNotNull(requests);
        assertEquals(1, requests.size());
    }

    @Test
    public void testGetRequestsByUserFacingJudgementEmailFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getRequestsByUserFacingJudgementEmail("Bob.Smith@gmail.com"));
        assertEquals("There are no Requests with userFacingJudgementEmail: " + "Bob.Smith@gmail.com", e.getMessage());
    }
}
