package ca.mcgill.ecse321.gamecenter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Employee;
import ca.mcgill.ecse321.gamecenter.model.Request;
import ca.mcgill.ecse321.gamecenter.model.UserRequest;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.EmployeeRepository;
import ca.mcgill.ecse321.gamecenter.repository.UserRequestRepository;

@SpringBootTest
public class UserRequestTests {
    @Autowired
    private ClientRepository ClientRepo;
    @Autowired
    private EmployeeRepository EmployeeRepo;
    @Autowired
    private UserRequestRepository UserRequestRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        UserRequestRepo.deleteAll();
        ClientRepo.deleteAll();
        EmployeeRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadUserRequestAsRequest() {
        // Create an Employee object with actual values
        Employee createdRequest = new Employee(
            "employee@example.com", 
            "employeeuser",         
            "employeepassword"     
        );
        createdRequest = EmployeeRepo.save(createdRequest);

        // Create a client
        Client userFacingJudgement = new Client(
        "test@example.com",     
        "testuser",            
        "password123",        
        "123-456-7890",       
        "123 Main St",           
        0                        
        );
        userFacingJudgement = ClientRepo.save(userFacingJudgement);

        // Create a user request
        Request.Status status = Request.Status.PENDING;
        UserRequest userRequest = new UserRequest(status, createdRequest, userFacingJudgement);
        userRequest = UserRequestRepo.save(userRequest);

        UserRequest userRequestFromDb = UserRequestRepo.findUserRequestById(userRequest.getId());

        // Check that the UserRequest was successfully retrieved
        assertNotNull(userRequestFromDb);
        
        // Validate the status of the UserRequest
        assertEquals(status, userRequestFromDb.getStatus());
        
        // Validate the associated Employee (Staff) of the UserRequest
        assertNotNull(userRequestFromDb.getCreatedRequest());
        assertEquals(createdRequest.getEmail(), userRequestFromDb.getCreatedRequest().getEmail());
        assertEquals(createdRequest.getUsername(), userRequestFromDb.getCreatedRequest().getUsername());
        assertEquals(createdRequest.getPassword(), userRequestFromDb.getCreatedRequest().getPassword());

        // Validate the associated Client of the UserRequest
        assertNotNull(userRequestFromDb.getUserFacingJudgement());
        assertEquals(userFacingJudgement.getEmail(), userRequestFromDb.getUserFacingJudgement().getEmail());
        assertEquals(userFacingJudgement.getUsername(), userRequestFromDb.getUserFacingJudgement().getUsername());
        assertEquals(userFacingJudgement.getPhoneNumber(), userRequestFromDb.getUserFacingJudgement().getPhoneNumber());
        assertEquals(userFacingJudgement.getDeliveryAddress(), userRequestFromDb.getUserFacingJudgement().getDeliveryAddress());
        assertEquals(userFacingJudgement.getNumberOfFlags(), userRequestFromDb.getUserFacingJudgement().getNumberOfFlags());
    }
}