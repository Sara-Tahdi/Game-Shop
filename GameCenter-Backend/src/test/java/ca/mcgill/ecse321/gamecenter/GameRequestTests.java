package ca.mcgill.ecse321.gamecenter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameRequest;
import ca.mcgill.ecse321.gamecenter.model.Request;
import ca.mcgill.ecse321.gamecenter.model.Employee;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.repository.EmployeeRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRequestRepository;

@SpringBootTest
public class GameRequestTests {
    @Autowired
    private GameCategoryRepository GameCategoryRepo;
    @Autowired
    private GameRepository GameRepo;
    @Autowired
    private GameRequestRepository GameRequestRepo;
    @Autowired
    private EmployeeRepository EmployeeRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        GameRequestRepo.deleteAll();
        EmployeeRepo.deleteAll();
        GameRepo.deleteAll();
        GameCategoryRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadGameRequestAsRequest() {
        // Create an employee 
        Employee createdRequest = new Employee(
            "employee@example.com", 
            "employeeuser",         
            "employeepassword"     
        );
        createdRequest = EmployeeRepo.save(createdRequest);

        // Create a game category
        GameCategory gameCategory = new GameCategory("Action");
        gameCategory = GameCategoryRepo.save(gameCategory);

        // Create a game
        Game game = new Game(
        "Super Fun Game",        
        59.99f,                  
        "An exciting adventure",  
        4.5f,                     
        100,                     
        false,                
        Game.GeneralFeeling.VERYPOSITIVE, 
        gameCategory              
        );
        game = GameRepo.save(game);

        // Create a game request
        Request.Status status = Request.Status.PENDING;
        GameRequest.Type type = GameRequest.Type.ADD;
        GameRequest gameRequest = new GameRequest(status, createdRequest, type, game);
        gameRequest = GameRequestRepo.save(gameRequest);

        // Retrieve the GameRequest, check that it was successfully retrieved, validate the attributes.
        GameRequest gameRequestFromDb = GameRequestRepo.findGameRequestById(gameRequest.getId());
        assertNotNull(gameRequestFromDb);
        assertEquals(status, gameRequestFromDb.getStatus());
        assertEquals(type, gameRequestFromDb.getType());

        // Validate the associated Employee (Staff) of the UserRequest
        assertNotNull(gameRequestFromDb.getCreatedRequest());
        assertEquals(createdRequest.getId(), gameRequestFromDb.getCreatedRequest().getId());
        assertEquals(createdRequest.getEmail(), gameRequestFromDb.getCreatedRequest().getEmail());
        assertEquals(createdRequest.getUsername(), gameRequestFromDb.getCreatedRequest().getUsername());
        assertEquals(createdRequest.getPassword(), gameRequestFromDb.getCreatedRequest().getPassword());

        // Validate the associated Game of the GameRequest
        assertNotNull(gameRequestFromDb.getGame());
        assertEquals(game.getId(), gameRequestFromDb.getGame().getId());
        assertEquals(game.getTitle(), gameRequestFromDb.getGame().getTitle());
        assertEquals(game.getPrice(), gameRequestFromDb.getGame().getPrice());
        assertEquals(game.getDescription(), gameRequestFromDb.getGame().getDescription());
        assertEquals(game.getRating(), gameRequestFromDb.getGame().getRating());
        assertEquals(game.getRemainingQuantity(), gameRequestFromDb.getGame().getRemainingQuantity());
        assertEquals(game.getIsOffered(), gameRequestFromDb.getGame().getIsOffered());
        assertEquals(game.getPublicOpinion(), gameRequestFromDb.getGame().getPublicOpinion());

        // Validate the associated GameCategory of the GameRequest
        assertNotNull(gameRequestFromDb.getGame().getCategories());
        assertEquals(gameCategory.getId(), gameRequestFromDb.getGame().getCategories().getId());
        assertEquals(gameCategory.getCategory(), gameRequestFromDb.getGame().getCategories().getCategory());
    }
}
