package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.Request;
import ca.mcgill.ecse321.gamecenter.model.Request.Status;
import ca.mcgill.ecse321.gamecenter.model.LeaveRequest;  // Assume LeaveRequest is a subclass of Request
import ca.mcgill.ecse321.gamecenter.model.Staff;
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

    @Autowired
    private StaffRepository staffRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        requestRepository.deleteAll();
        staffRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadLeaveRequest() {
        // Create Staff (for the createdRequest association)
        Staff staff = new Staff();
        staff.setUsername("testStaff");
        staff.setEmail("test@staff.com");
        staff = staffRepository.save(staff);
        assertNotNull(staff);

        // Create LeaveRequest instance
        LeaveRequest leaveRequest = new LeaveRequest(Status.PENDING, staff);

        // Persist the LeaveRequest object
        leaveRequest = requestRepository.save(leaveRequest);
        assertNotNull(leaveRequest);
        assertEquals(Status.PENDING, leaveRequest.getStatus());
        assertEquals(staff.getId(), leaveRequest.getCreatedRequest().getId());

        // Get by ID tests
        int id = leaveRequest.getId();
        LeaveRequest leaveRequestFromDb = (LeaveRequest) requestRepository.findById(id).orElse(null);
        assertNotNull(leaveRequestFromDb);

        assertEquals(id, leaveRequestFromDb.getId());
        assertEquals(Status.PENDING, leaveRequestFromDb.getStatus());
        assertEquals(staff.getId(), leaveRequestFromDb.getCreatedRequest().getId());
    }

    @Test
    public void testUpdateLeaveRequest() {
        // Create Staff
        Staff staff = new Staff();
        staff.setUsername("testStaff");
        staff.setEmail("test@staff.com");
        staff = staffRepository.save(staff);

        // Create LeaveRequest instance
        LeaveRequest leaveRequest = new LeaveRequest(Status.PENDING, staff);
        leaveRequest = requestRepository.save(leaveRequest);

        // Update the LeaveRequest status
        leaveRequest.setStatus(Status.APPROVED);
        leaveRequest = requestRepository.save(leaveRequest);

        // Retrieve updated LeaveRequest by ID
        LeaveRequest leaveRequestFromDb = (LeaveRequest) requestRepository.findById(leaveRequest.getId()).orElse(null);
        assertNotNull(leaveRequestFromDb);
        assertEquals(Status.APPROVED, leaveRequestFromDb.getStatus());
    }

    @Test
    public void testDeleteLeaveRequest() {
        // Create Staff
        Staff staff = new Staff();
        staff.setUsername("testStaff");
        staff.setEmail("test@staff.com");
        staff = staffRepository.save(staff);

        // Create LeaveRequest instance
        LeaveRequest leaveRequest = new LeaveRequest(Status.PENDING, staff);
        leaveRequest = requestRepository.save(leaveRequest);
        assertNotNull(leaveRequest);

        // Delete the LeaveRequest
        requestRepository.deleteById(leaveRequest.getId());

        // Verify it's deleted
        LeaveRequest deletedLeaveRequest = (LeaveRequest) requestRepository.findById(leaveRequest.getId()).orElse(null);
        assertNull(deletedLeaveRequest);
    }

    @Test
    public void testSetAndGetCreatedRequest() {
        // Create Staff
        Staff staff1 = new Staff();
        staff1.setUsername("testStaff1");
        staff1.setEmail("test1@staff.com");
        staff1 = staffRepository.save(staff1);

        Staff staff2 = new Staff();
        staff2.setUsername("testStaff2");
        staff2.setEmail("test2@staff.com");
        staff2 = staffRepository.save(staff2);

        // Create LeaveRequest instance
        LeaveRequest leaveRequest = new LeaveRequest(Status.PENDING, staff1);
        leaveRequest = requestRepository.save(leaveRequest);

        // Assert that createdRequest is correctly set
        assertEquals(staff1.getId(), leaveRequest.getCreatedRequest().getId());

        // Change createdRequest to a new Staff member (staff2)
        leaveRequest.setCreatedRequest(staff2);
        leaveRequest = requestRepository.save(leaveRequest);

        // Assert that createdRequest was updated correctly
        assertEquals(staff2.getId(), leaveRequest.getCreatedRequest().getId());
    }

    @Test
    public void testGetRequestByStatus() {
        // Create Staff
        Staff staff = new Staff();
        staff.setUsername("testStaff");
        staff.setEmail("test@staff.com");
        staff = staffRepository.save(staff);

        // Create multiple LeaveRequests with different statuses
        LeaveRequest pendingRequest = new LeaveRequest(Status.PENDING, staff);
        LeaveRequest approvedRequest = new LeaveRequest(Status.APPROVED, staff);
        LeaveRequest deniedRequest = new LeaveRequest(Status.DENIED, staff);

        requestRepository.save(pendingRequest);
        requestRepository.save(approvedRequest);
        requestRepository.save(deniedRequest);

        // Get requests by status
        List<Request> pendingRequests = requestRepository.findByStatus(Status.PENDING);
        List<Request> approvedRequests = requestRepository.findByStatus(Status.APPROVED);
        List<Request> deniedRequests = requestRepository.findByStatus(Status.DENIED);

        // Assert that the correct requests are returned
        assertNotNull(pendingRequests);
        assertEquals(1, pendingRequests.size());
        assertTrue(pendingRequests.get(0) instanceof LeaveRequest);

        assertNotNull(approvedRequests);
        assertEquals(1, approvedRequests.size());
        assertTrue(approvedRequests.get(0) instanceof LeaveRequest);

        assertNotNull(deniedRequests);
        assertEquals(1, deniedRequests.size());
        assertTrue(deniedRequests.get(0) instanceof LeaveRequest);
    }
}
