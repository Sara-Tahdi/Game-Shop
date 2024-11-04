package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;
import java.util.List;


@SpringBootTest
public class RequestTests {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        requestRepository.deleteAll();
        staffRepository.deleteAll();
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadGameRequest() {
        // Create a staff member to issue the new game request
        Employee employee = new Employee();
        employee.setUsername("Emily");
        employee.setEmail("Emily@gmail.com");
        employee.setPassword("54321");
        employee = staffRepository.save(employee);

        // Create the game
        GameCategory category = new GameCategory();
        category.setCategory("Multiplayer online battle arena");
        category = gameCategoryRepository.save(category);
        Game game = new Game("League", 3, "May cause brainrot", 3, 100, true, Game.GeneralFeeling.POSITIVE, category);
        game = gameRepository.save(game);

        // Create a new game request
        GameRequest gameRequest = new GameRequest(GameRequest.Status.PENDING, "", (Employee) employee, GameRequest.Type.ADD, game);
        gameRequest = requestRepository.save(gameRequest);
        assertNotNull(gameRequest);
        assertEquals(GameRequest.Status.PENDING, gameRequest.getStatus());
        assertEquals(GameRequest.Type.ADD, gameRequest.getType());
        assertEquals(game, gameRequest.getGame());
        assertEquals(employee.getUsername(), gameRequest.getCreatedRequest().getUsername());
        assertEquals("League", gameRequest.getGame().getTitle());
        assertEquals(3, gameRequest.getGame().getPrice(), 0.01);
        assertEquals(Game.GeneralFeeling.POSITIVE, gameRequest.getGame().getPublicOpinion());
        assertEquals("Multiplayer online battle arena", gameRequest.getGame().getCategory().getCategory());

        // Get by ID
        Request loadedRequest = requestRepository.findById(gameRequest.getId()).orElse(null);
        assertNotNull(loadedRequest);

        GameRequest castedLoadedRequest = (GameRequest) loadedRequest;
        assertEquals(GameRequest.Status.PENDING, loadedRequest.getStatus());
        assertEquals(GameRequest.Type.ADD, castedLoadedRequest.getType());
        assertEquals(game.getId(), castedLoadedRequest.getGame().getId());
        assertEquals(employee.getUsername(), loadedRequest.getCreatedRequest().getUsername());
        assertEquals("League", castedLoadedRequest.getGame().getTitle());
        assertEquals(3, castedLoadedRequest.getGame().getPrice(), 0.01);
        assertEquals(Game.GeneralFeeling.POSITIVE, castedLoadedRequest.getGame().getPublicOpinion());
        assertEquals("Multiplayer online battle arena", castedLoadedRequest.getGame().getCategory().getCategory());
    }

    @Test
    public void testPersistAndLoadBanRequest() {
        // Create a staff member to issue the ban
        Employee employee = new Employee();
        employee.setUsername("Emily");
        employee.setEmail("Emily@gmail.com");
        employee.setPassword("54321");
        employee = staffRepository.save(employee);

        // Create a client that will be banned
        Client client = new Client();
        client.setUsername("Charlie");
        client.setEmail("Charlie@gmail.com");
        client.setPassword("01234");
        client = clientRepository.save(client);

        // Create a user ban request
        UserRequest userRequest = new UserRequest(UserRequest.Status.PENDING, "", (Employee) employee, client);
        userRequest = requestRepository.save(userRequest);
        assertNotNull(userRequest);
        assertEquals(UserRequest.Status.PENDING, userRequest.getStatus());
        assertEquals(client.getUsername(), userRequest.getUserFacingJudgement().getUsername());
        assertEquals(employee.getUsername(), userRequest.getCreatedRequest().getUsername());

        // Get by ID
        Request loadedRequest = requestRepository.findById(userRequest.getId()).orElse(null);
        assertNotNull(loadedRequest);

        UserRequest castedLoadedRequest = (UserRequest) loadedRequest;
        assertEquals(Request.Status.PENDING, castedLoadedRequest.getStatus());
        assertEquals(client.getUsername(), castedLoadedRequest.getUserFacingJudgement().getUsername());
        assertEquals(employee.getUsername(), castedLoadedRequest.getCreatedRequest().getUsername());
    }

    @Test
    public void testUpdateRequestStatus() {
        // Create a staff member to issue the new game request
        Employee employee = new Employee();
        employee.setUsername("Emily");
        employee.setEmail("Emily@gmail.com");
        employee.setPassword("54321");
        employee = staffRepository.save(employee);

        // Create a game for the request
        GameCategory category = new GameCategory();
        category.setCategory("Strategy");
        category = gameCategoryRepository.save(category);
        Game game = new Game("Strategic Game", 10, "A challenging strategy game.", 2, 15, true, Game.GeneralFeeling.NEUTRAL, category);
        game = gameRepository.save(game);

        // Create a game request
        GameRequest gameRequest = new GameRequest(GameRequest.Status.PENDING, "", (Employee) employee, GameRequest.Type.ADD, game);
        gameRequest = requestRepository.save(gameRequest);

        // Approve the request
        gameRequest.setStatus(GameRequest.Status.APPROVED);
        requestRepository.save(gameRequest);

        // Get by ID and double check that request is Approved
        Request updatedRequest = requestRepository.findById(gameRequest.getId()).orElse(null);
        assertNotNull(updatedRequest);
        assertEquals(Request.Status.APPROVED, updatedRequest.getStatus());
    }

    @Test
    public void testDeleteRequest() {
        // Create a staff member to issue the new game request
        Employee employee = new Employee();
        employee.setUsername("Emily");
        employee.setEmail("Emily@gmail.com");
        employee.setPassword("54321");
        employee = staffRepository.save(employee);

        // Create a user ban request
        Client client = new Client();
        client.setUsername("canada");
        client.setEmail("canada@gmail.com");
        client.setPassword("canada");
        client = clientRepository.save(client);

        UserRequest userRequest = new UserRequest(UserRequest.Status.PENDING, "", (Employee) employee, client);
        userRequest = requestRepository.save(userRequest);

        // Delete the request
        requestRepository.delete(userRequest);

        // Get by id, confirm it was deleted
        Request deletedRequest = requestRepository.findById(userRequest.getId()).orElse(null);
        assertNull(deletedRequest);
    }

    @Test
    public void testLoadByRequestType() {
        Employee employee = new Employee();
        employee.setUsername("Emily");
        employee.setEmail("Emily@gmail.com");
        employee.setPassword("54321");
        employee = staffRepository.save(employee);

        // Create a user ban request
        Client client = new Client();
        client.setUsername("canada");
        client.setEmail("canada@gmail.com");
        client.setPassword("canada");
        client = clientRepository.save(client);

        UserRequest userRequest = new UserRequest(UserRequest.Status.PENDING, "", employee, client);
        userRequest = requestRepository.save(userRequest);

        // Create a game for the request
        GameCategory category = new GameCategory();
        category.setCategory("Strategy");
        category = gameCategoryRepository.save(category);
        Game game = new Game("Strategic Game", 10, "A challenging strategy game.", 2, 15, true, Game.GeneralFeeling.NEUTRAL, category);
        game = gameRepository.save(game);

        // Create a game request
        GameRequest gameRequest = new GameRequest(GameRequest.Status.PENDING, "", employee, GameRequest.Type.ADD, game);
        gameRequest = requestRepository.save(gameRequest);

        List<Request> userRequestsFromDb = requestRepository.findRequestsByRequestType(UserRequest.class).orElse(null);
        assertNotNull(userRequestsFromDb);

        assertEquals(1, userRequestsFromDb.size());
        assertInstanceOf(UserRequest.class, userRequestsFromDb.getFirst());

        List<Request> gameRequestsFromDb = requestRepository.findRequestsByRequestType(GameRequest.class).orElse(null);
        assertNotNull(gameRequestsFromDb);

        assertEquals(1, gameRequestsFromDb.size());
        assertInstanceOf(GameRequest.class, gameRequestsFromDb.getFirst());
    }

    @Test
    void testLoadByStaffId() {
        Employee employee = new Employee();
        employee.setUsername("Emily");
        employee.setEmail("Emily@gmail.com");
        employee.setPassword("54321");
        employee = staffRepository.save(employee);

        // Create a user ban request
        Client client = new Client();
        client.setUsername("canada");
        client.setEmail("canada@gmail.com");
        client.setPassword("canada");
        client = clientRepository.save(client);

        UserRequest userRequest = new UserRequest(UserRequest.Status.PENDING, "", employee, client);
        userRequest = requestRepository.save(userRequest);

        // Create a game for the request
        GameCategory category = new GameCategory();
        category.setCategory("Strategy");
        category = gameCategoryRepository.save(category);
        Game game = new Game("Strategic Game", 10, "A challenging strategy game.", 2, 15, true, Game.GeneralFeeling.NEUTRAL, category);
        game = gameRepository.save(game);

        // Create a game request
        GameRequest gameRequest = new GameRequest(GameRequest.Status.PENDING, "", employee, GameRequest.Type.ADD, game);
        gameRequest = requestRepository.save(gameRequest);

        // Create a dummy second staff
        Employee dummyEmployee = new Employee("fake@real.lol", "fake", "IamReal");
        dummyEmployee = staffRepository.save(dummyEmployee);

        // Create a dummy Request
        Game dummyGame = new Game();
        dummyGame.setPrice(Float.parseFloat("999.99"));
        dummyGame.setTitle("Best Game Ever 2");
        dummyGame = gameRepository.save(dummyGame);

        GameRequest dummyGameRequest = new GameRequest(Request.Status.PENDING, "", dummyEmployee, GameRequest.Type.ADD, dummyGame);
        dummyGameRequest = requestRepository.save(dummyGameRequest);

        List<Request> requestsFromDb = requestRepository.findRequestsByCreatedRequestId(employee.getId()).orElse(null);
        assertNotNull(requestsFromDb);

        assertEquals(2, requestsFromDb.size());
        assertInstanceOf(UserRequest.class, requestsFromDb.getFirst());
        assertEquals(userRequest.getId(), requestsFromDb.getFirst().getId());
        assertInstanceOf(GameRequest.class, requestsFromDb.getLast());
        assertEquals(gameRequest.getId(), requestsFromDb.getLast().getId());
    }
}
