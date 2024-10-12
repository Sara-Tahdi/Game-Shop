package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.model.GameRequest;
import ca.mcgill.ecse321.gamecenter.model.UserRequest;
import ca.mcgill.ecse321.gamecenter.model.Employee;
import ca.mcgill.ecse321.gamecenter.model.AppUser;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.repository.GameRequestRepository;
import ca.mcgill.ecse321.gamecenter.repository.UserRequestRepository;
import ca.mcgill.ecse321.gamecenter.repository.EmployeeRepository;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
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
    private GameRequestRepository gameRequestRepository;

    @Autowired
    private UserRequestRepository userRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        gameRequestRepository.deleteAll();
        userRequestRepository.deleteAll();
        employeeRepository.deleteAll();
        clientRepository.deleteAll();
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
        employee = employeeRepository.save(employee);

        // Create the game
        GameCategory category = new GameCategory();
        category.setCategory("Multiplayer online battle arena");
        category = gameCategoryRepository.save(category);
        Game game = new Game("League", 3, "May cause brainrot", 3, 100, true, Game.GeneralFeeling.POSITIVE, category);
        game = gameRepository.save(game);

        // Create a new game request
        GameRequest gameRequest = new GameRequest(GameRequest.Status.PENDING, (Employee) employee, GameRequest.Type.ADD, game);
        gameRequest = gameRequestRepository.save(gameRequest);
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
        GameRequest loadedRequest = (GameRequest) gameRequestRepository.findById(gameRequest.getId()).orElse(null);
        assertNotNull(loadedRequest);
        assertEquals(GameRequest.Status.PENDING, loadedRequest.getStatus());
        assertEquals(GameRequest.Type.ADD, loadedRequest.getType());
        assertEquals(game.getId(), loadedRequest.getGame().getId());
        assertEquals(employee.getUsername(), loadedRequest.getCreatedRequest().getUsername());
        assertEquals("League", loadedRequest.getGame().getTitle());
        assertEquals(3, loadedRequest.getGame().getPrice(), 0.01);
        assertEquals(Game.GeneralFeeling.POSITIVE, loadedRequest.getGame().getPublicOpinion());
        assertEquals("Multiplayer online battle arena", loadedRequest.getGame().getCategory().getCategory());
    }

    @Test
    public void testPersistAndLoadBanRequest() {
        // Create a staff member to issue the ban
        Employee employee = new Employee();
        employee.setUsername("Emily");
        employee.setEmail("Emily@gmail.com");
        employee.setPassword("54321");
        employee = employeeRepository.save(employee);

        // Create a client that will be banned
        Client client = new Client();
        client.setUsername("Charlie");
        client.setEmail("Charlie@gmail.com");
        client.setPassword("01234");
        client = clientRepository.save(client);

        // Create a user ban request
        UserRequest userRequest = new UserRequest(UserRequest.Status.PENDING, (Employee) employee, client);
        userRequest = userRequestRepository.save(userRequest);
        assertNotNull(userRequest);
        assertEquals(UserRequest.Status.PENDING, userRequest.getStatus());
        assertEquals(client.getUsername(), userRequest.getUserFacingJudgement().getUsername());
        assertEquals(employee.getUsername(), userRequest.getCreatedRequest().getUsername());

        // Get by ID
        UserRequest loadedRequest = (UserRequest) userRequestRepository.findById(userRequest.getId()).orElse(null);
        assertNotNull(loadedRequest);
        assertEquals(UserRequest.Status.PENDING, loadedRequest.getStatus());
        assertEquals(client.getUsername(), loadedRequest.getUserFacingJudgement().getUsername());
        assertEquals(employee.getUsername(), loadedRequest.getCreatedRequest().getUsername());
    }

    @Test
    public void testUpdateRequestStatus() {
        // Create a staff member to issue the new game request
        Employee employee = new Employee();
        employee.setUsername("Emily");
        employee.setEmail("Emily@gmail.com");
        employee.setPassword("54321");
        employee = employeeRepository.save(employee);

        // Create a game for the request
        GameCategory category = new GameCategory();
        category.setCategory("Strategy");
        category = gameCategoryRepository.save(category);
        Game game = new Game("Strategic Game", 10, "A challenging strategy game.", 2, 15, true, Game.GeneralFeeling.NEUTRAL, category);
        game = gameRepository.save(game);

        // Create a game request
        GameRequest gameRequest = new GameRequest(GameRequest.Status.PENDING, (Employee) employee, GameRequest.Type.ADD, game);
        gameRequest = gameRequestRepository.save(gameRequest);

        // Approve the request
        gameRequest.setStatus(GameRequest.Status.APPROVED);
        gameRequestRepository.save(gameRequest);

        // Get by ID and double check that request is Approved
        GameRequest updatedRequest = (GameRequest) gameRequestRepository.findById(gameRequest.getId()).orElse(null);
        assertNotNull(updatedRequest);
        assertEquals(GameRequest.Status.APPROVED, updatedRequest.getStatus());
    }

    @Test
    public void testDeleteRequest() {
        // Create a staff member to issue the new game request
        Employee employee = new Employee();
        employee.setUsername("Emily");
        employee.setEmail("Emily@gmail.com");
        employee.setPassword("54321");
        employee = employeeRepository.save(employee);

        // Create a user ban request
        Client client = new Client();
        client.setUsername("canada");
        client.setEmail("canada@gmail.com");
        client.setPassword("canada");
        client = clientRepository.save(client);

        UserRequest userRequest = new UserRequest(UserRequest.Status.PENDING, (Employee) employee, client);
        userRequest = userRequestRepository.save(userRequest);

        // Delete the request
        userRequestRepository.delete(userRequest);

        // Get by id, confirm it was deleted
        UserRequest deletedRequest = (UserRequest) userRequestRepository.findById(userRequest.getId()).orElse(null);
        assertNull(deletedRequest);
    }
}
