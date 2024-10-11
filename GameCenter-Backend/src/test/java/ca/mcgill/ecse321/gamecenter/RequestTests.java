package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.model.GameRequest;
import ca.mcgill.ecse321.gamecenter.model.UserRequest;
import ca.mcgill.ecse321.gamecenter.model.Staff;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.repository.RequestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RequestTests {

    @Autowired
    private RequestRepository requestRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        requestRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadGameRequest() {
        // Create a staff member to issue the new game request
        Staff staff = new Staff();
        staff.setUsername("Sara");
        staff.setEmail("Sara@gmail.com");
        staff.setPassword("12345");

        // Create the game
        GameCategory category = new GameCategory();
        category.setCategoryName("Multiplayer online battle arena");
        Game game = new Game("League", 3, "May cause brainrot", 3, 100, true, Game.GeneralFeeling.POSITIVE, category);

        // Create a new game request
        GameRequest gameRequest = new GameRequest(GameRequest.Status.PENDING, staff, GameRequest.Type.ADD, game);
        gameRequest = requestRepository.save(gameRequest);
        assertNotNull(gameRequest);
        assertEquals(GameRequest.Status.PENDING, gameRequest.getStatus());
        assertEquals(GameRequest.Type.ADD, gameRequest.getType());
        assertEquals(game, gameRequest.getGame());
        assertEquals(staff.getUsername(), gameRequest.getCreatedRequest().getUsername());
        assertEquals("League", gameRequest.getGame().getTitle());
        assertEquals(59.99f, gameRequest.getGame().getPrice(), 0.01);
        assertEquals(Game.GeneralFeeling.POSITIVE, gameRequest.getGame().getPublicOpinion());
        assertEquals("Multiplayer online battle arena", gameRequest.getGame().getCategories().getCategoryName());

        // Get by ID
        GameRequest loadedRequest = (GameRequest) requestRepository.findById(gameRequest.getId()).orElse(null);
        assertNotNull(loadedRequest);
        assertEquals(GameRequest.Status.PENDING, loadedRequest.getStatus());
        assertEquals(GameRequest.Type.ADD, loadedRequest.getType());
        assertEquals(game, loadedRequest.getGame());
        assertEquals(staff.getUsername(), loadedRequest.getCreatedRequest().getUsername());
        assertEquals("League", loadedRequest.getGame().getTitle());
        assertEquals(59.99f, loadedRequest.getGame().getPrice(), 0.01);
        assertEquals(Game.GeneralFeeling.POSITIVE, loadedRequest.getGame().getPublicOpinion());
        assertEquals("Multiplayer online battle arena", loadedRequest.getGame().getCategories().getCategoryName());
    }

    @Test
    public void testPersistAndLoadBanRequest() {
        // Create a staff member to issue the ban
        Staff staff = new Staff();
        staff.setUsername("Emily");
        staff.setEmail("Emily@gmail.com");
        staff.setPassword("54321");

        // Create a client that will be banned
        Client client = new Client();
        client.setUsername("Charlie");
        client.setEmail("Charlie@gmail.com");
        client.setPassword("01234");

        // Create a user ban request
        UserRequest userRequest = new UserRequest(UserRequest.Status.PENDING, staff, client);
        userRequest = requestRepository.save(userRequest);
        assertNotNull(userRequest);
        assertEquals(UserRequest.Status.PENDING, userRequest.getStatus());
        assertEquals(client.getUsername(), userRequest.getUserFacingJudgement().getUsername());
        assertEquals(staff.getUsername(), userRequest.getCreatedRequest().getUsername());

        // Get by ID
        UserRequest loadedRequest = (UserRequest) requestRepository.findById(userRequest.getId()).orElse(null);
        assertNotNull(loadedRequest);
        assertEquals(UserRequest.Status.PENDING, loadedRequest.getStatus());
        assertEquals(client.getUsername(), loadedRequest.getUserFacingJudgement().getUsername());
        assertEquals(staff.getUsername(), loadedRequest.getCreatedRequest().getUsername());
    }

    @Test
    public void testUpdateRequestStatus() {
        // Create a staff member to issue the new game request
        Staff staff = new Staff();
        staff.setUsername("Annie");
        staff.setEmail("Annie@gmail.com");
        staff.setPassword("123password");

        // Create a game for the request
        GameCategory category = new GameCategory();
        category.setCategoryName("Strategy");
        Game game = new Game("Strategic Game", 10, "A challenging strategy game.", 2, 15, true, Game.GeneralFeeling.NEUTRAL, category);

        // Create a game request
        GameRequest gameRequest = new GameRequest(GameRequest.Status.PENDING, staff, GameRequest.Type.ADD, game);
        gameRequest = requestRepository.save(gameRequest);

        // Approve the request
        gameRequest.setStatus(GameRequest.Status.APPROVED);
        requestRepository.save(gameRequest);

        // Get by ID and double check that request is Approved
        GameRequest updatedRequest = (GameRequest) requestRepository.findById(gameRequest.getId()).orElse(null);
        assertNotNull(updatedRequest);
        assertEquals(GameRequest.Status.APPROVED, updatedRequest.getStatus());
    }

    @Test
    public void testDeleteRequest() {
        // Create a staff member to issue the new game request
        Staff staff = new Staff();
        staff.setUsername("Silly");
        staff.setEmail("Silly@gmail.com");
        staff.setPassword("lol123");

        // Create a user ban request
        Client client = new Client();
        client.setUsername("canada");
        client.setEmail("canada@gmail.com");
        client.setPassword("canada");
        UserRequest userRequest = new UserRequest(UserRequest.Status.PENDING, staff, client);
        userRequest = requestRepository.save(userRequest);

        // Delete the request
        requestRepository.delete(userRequest);

        // Get by id, confirm it was deleted
        UserRequest deletedRequest = (UserRequest) requestRepository.findById(userRequest.getId()).orElse(null);
        assertNull(deletedRequest);
    }
}
