package ca.mcgill.ecse321.gamecenter.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import ca.mcgill.ecse321.gamecenter.model.GameCenter;
import ca.mcgill.ecse321.gamecenter.repository.GameCenterRepository;
import ca.mcgill.ecse321.gamecenter.service.GameCenterService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Time;
import java.util.Optional;

@SpringBootTest
class GameCenterServiceTests {

    // Mocking the repository to simulate database operations
    @Mock
    private GameCenterRepository gameCenterRepository;

    // Injecting the actual GameCenterService to test its functionality
    @InjectMocks
    private GameCenterService gameCenterService;

    @Test
    void testCreateNewGameCenter() {
        // Arrange: Set up a new GameCenter to be created
        GameCenter gameCenter = new GameCenter("GameZone", Time.valueOf("09:00:00"), Time.valueOf("21:00:00"), "No food allowed");

        // Mock the repository to return false when checking if the GameCenter already exists
        when(gameCenterRepository.existsById(gameCenter.getName())).thenReturn(false);

        // Mock the save operation to return the same GameCenter
        when(gameCenterRepository.save(gameCenter)).thenReturn(gameCenter);

        // Act: Call the service method to create the GameCenter
        GameCenter createdGameCenter = gameCenterService.saveOrUpdateGameCenter(gameCenter);

        // Assert: Verify that the GameCenter was created correctly
        assertNotNull(createdGameCenter);
        assertEquals("GameZone", createdGameCenter.getName());
        verify(gameCenterRepository, times(1)).save(gameCenter);
    }

    @Test
    void testUpdateExistingGameCenter() {
        // Arrange: Set up an existing GameCenter and a new version with updated data
        GameCenter existingGameCenter = new GameCenter("GameZone", Time.valueOf("09:00:00"), Time.valueOf("21:00:00"), "No food allowed");
        GameCenter updatedGameCenter = new GameCenter("GameZone", Time.valueOf("10:00:00"), Time.valueOf("22:00:00"), "Food allowed");

        // Mock repository to return true, indicating that the GameCenter exists
        when(gameCenterRepository.existsById(existingGameCenter.getName())).thenReturn(true);

        // Mock findById to return the existing GameCenter for updating
        when(gameCenterRepository.findById(existingGameCenter.getName())).thenReturn(Optional.of(existingGameCenter));

        // Mock save to return the updated GameCenter
        when(gameCenterRepository.save(existingGameCenter)).thenReturn(updatedGameCenter);

        // Act: Call the service method to update the GameCenter
        GameCenter result = gameCenterService.saveOrUpdateGameCenter(updatedGameCenter);

        // Assert: Verify that the GameCenter was updated with the new values
        assertNotNull(result);
        assertEquals("GameZone", result.getName());
        assertEquals(Time.valueOf("10:00:00"), result.getOpen());
        assertEquals(Time.valueOf("22:00:00"), result.getClose());
        assertEquals("Food allowed", result.getStorePolicy());
        verify(gameCenterRepository, times(1)).save(existingGameCenter);
    }

    @Test
    void testSaveOrUpdateGameCenter_InvalidInput() {
        // Arrange: Create an invalid GameCenter object with missing required fields
        GameCenter invalidGameCenter = new GameCenter();

        // Act & Assert: Expect an IllegalArgumentException due to missing fields
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gameCenterService.saveOrUpdateGameCenter(invalidGameCenter);
        });

        // Verify that the exception message is as expected
        assertEquals("GameCenter name, opening time, and closing time are required.", exception.getMessage());

        // Verify that save was never called due to the validation failure
        verify(gameCenterRepository, never()).save(any(GameCenter.class));
    }

    @Test
    void testCreateGameCenterWithDuplicateName() {
        // Arrange: Set up an existing GameCenter and a duplicate GameCenter with the same name
        GameCenter existingGameCenter = new GameCenter("GameZone", Time.valueOf("09:00:00"), Time.valueOf("21:00:00"), "No food allowed");

        // Mock repository to return true, indicating that the GameCenter already exists
        when(gameCenterRepository.existsById(existingGameCenter.getName())).thenReturn(true);

        // Mock findById to return the existing GameCenter for updating
        when(gameCenterRepository.findById(existingGameCenter.getName())).thenReturn(Optional.of(existingGameCenter));

        when(gameCenterRepository.save(existingGameCenter)).thenReturn(existingGameCenter);


        // Create a duplicate GameCenter with different times and policy
        GameCenter duplicateGameCenter = new GameCenter("GameZone", Time.valueOf("08:00:00"), Time.valueOf("22:00:00"), "Food allowed");

        // Act: Call the service method to handle the duplicate, expecting it to update instead of creating a new entry
        GameCenter result = gameCenterService.saveOrUpdateGameCenter(duplicateGameCenter);

        // Assert: Verify that the GameCenter was updated with the new values
        assertEquals("GameZone", result.getName());
        assertEquals(Time.valueOf("08:00:00"), result.getOpen());
        assertEquals(Time.valueOf("22:00:00"), result.getClose());
        assertEquals("Food allowed", result.getStorePolicy());
        verify(gameCenterRepository, times(1)).save(existingGameCenter);
    }

    @Test
    void testInvalidTimeRange() {
        // Arrange: Set up a GameCenter with an invalid time range (closing time before opening time)
        GameCenter invalidTimeGameCenter = new GameCenter("NightOwl", Time.valueOf("22:00:00"), Time.valueOf("08:00:00"), "Night hours only");

        // Act & Assert: Expect an IllegalArgumentException due to invalid time range
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gameCenterService.saveOrUpdateGameCenter(invalidTimeGameCenter);
        });

        // Verify that the exception message is as expected
        assertEquals("Closing time must be after opening time.", exception.getMessage());

        // Verify that save was never called due to the validation failure
        verify(gameCenterRepository, never()).save(any(GameCenter.class));
    }

    @Test
    void testBoundaryTimeValues() {
        // Arrange: Set up a GameCenter with boundary time values (e.g., midnight to end of day)
        GameCenter boundaryGameCenter = new GameCenter("MidnightClub", Time.valueOf("00:00:00"), Time.valueOf("23:59:59"), "Open all day");

        // Mock repository to return false, indicating that the GameCenter does not already exist
        when(gameCenterRepository.existsById(boundaryGameCenter.getName())).thenReturn(false);

        // Mock save to return the boundary GameCenter
        when(gameCenterRepository.save(boundaryGameCenter)).thenReturn(boundaryGameCenter);

        // Act: Call the service method to create the GameCenter
        GameCenter result = gameCenterService.saveOrUpdateGameCenter(boundaryGameCenter);

        // Assert: Verify that the GameCenter was created with the correct boundary values
        assertNotNull(result);
        assertEquals(Time.valueOf("00:00:00"), result.getOpen());
        assertEquals(Time.valueOf("23:59:59"), result.getClose());
        verify(gameCenterRepository, times(1)).save(boundaryGameCenter);
    }
}
