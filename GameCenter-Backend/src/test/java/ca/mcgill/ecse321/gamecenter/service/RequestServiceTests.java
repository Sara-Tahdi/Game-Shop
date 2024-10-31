package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.repository.RequestRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.StaffRepository;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
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
    @InjectMocks
    private RequestService requestService;

    /* Getter tests */
    @Test
    public void testGetAllRequestsError() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                requestService.getAllRequests());
        assertEquals("There are no Requests", e.getMessage());
    }

}
