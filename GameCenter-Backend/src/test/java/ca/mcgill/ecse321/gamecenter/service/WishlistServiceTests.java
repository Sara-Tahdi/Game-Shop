package ca.mcgill.ecse321.gamecenter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.gamecenter.model.Wishlist;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.repository.WishlistRepository;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;

import java.util.Collections;
import java.util.Optional;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class WishlistServiceTests {
    @Mock
    private WishlistRepository mockWishlistRepo;
    
    @Mock
    private ClientRepository mockClientRepo;
    
    @Mock
    private GameRepository mockGameRepo;
    
    @InjectMocks
    private WishlistService service;

    private static final int VALID_CLIENT_ID = 1;
    private static final int VALID_GAME_ID = 100;
    private static final int VALID_WISHLIST_ID = 10;

    @Test
    public void testCreateValidWishlist() {
        // Arrange
        Client client = new Client();
        when(mockClientRepo.findById(VALID_CLIENT_ID)).thenReturn(Optional.of(client));
        
        Game game = new Game();
        when(mockGameRepo.findById(VALID_GAME_ID)).thenReturn(Optional.of(game));
        
        // Whenever mockWishlistRepo.save(wishlist) is called, return the wishlist
        when(mockWishlistRepo.save(any(Wishlist.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));

        // Act
        Wishlist createdWishlist = service.addGameToWishlist(VALID_CLIENT_ID, VALID_GAME_ID);

        // Assert
        assertNotNull(createdWishlist);
        assertEquals(client, createdWishlist.getClient());
        assertEquals(game, createdWishlist.getGame());
        verify(mockWishlistRepo, times(1)).save(createdWishlist);
    }

    @Test
    public void testFindWishlistByValidId() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        wishlist.setId(VALID_WISHLIST_ID);
        when(mockWishlistRepo.findById(VALID_WISHLIST_ID)).thenReturn(Optional.of(wishlist));

        // Act
        Wishlist foundWishlist = service.findWishlistById(VALID_WISHLIST_ID);

        // Assert
        assertNotNull(foundWishlist);
    }

    @Test
    public void testFindWishlistByInvalidId() {
        // Arrange
        when(mockWishlistRepo.findById(VALID_WISHLIST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> service.findWishlistById(VALID_WISHLIST_ID));
        assertEquals("There is no wishlist with ID: " +  VALID_WISHLIST_ID, error.getMessage());
    }

    @Test
    public void testAddGameToWishlistForInvalidClient() {
        // Arrange
        Game game = new Game();
        game.setId(VALID_GAME_ID);
        when(mockClientRepo.findById(VALID_CLIENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> service.addGameToWishlist(VALID_CLIENT_ID, VALID_GAME_ID));
        assertEquals("No client found with id: " +  VALID_CLIENT_ID, error.getMessage());
    }

    @Test
    public void testAddGameToWishlistForInvalidGame() {
        // Arrange
        Client client = new Client();
        when(mockClientRepo.findById(VALID_CLIENT_ID)).thenReturn(Optional.of(client));
        when(mockGameRepo.findById(VALID_GAME_ID)).thenReturn(Optional.empty());


        // Act & Assert
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> service.addGameToWishlist(VALID_CLIENT_ID, VALID_GAME_ID));
        assertEquals("No game found with id: " +  VALID_GAME_ID, error.getMessage());
    }

    @Test
    public void testRemoveGameFromWishlist() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        when(mockWishlistRepo.findWishlistByClientIdAndGameId(VALID_CLIENT_ID, VALID_GAME_ID)).thenReturn(Optional.of(wishlist));

        // Act
        service.removeGameFromWishlist(VALID_CLIENT_ID, VALID_GAME_ID);

        // Assert
        verify(mockWishlistRepo, times(1)).delete(wishlist);
    }

    @Test
    public void testRemoveGameFromWishlistNotFound() {
        // Arrange
        when(mockWishlistRepo.findWishlistByClientIdAndGameId(VALID_CLIENT_ID, VALID_GAME_ID)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> service.removeGameFromWishlist(VALID_CLIENT_ID, VALID_GAME_ID));
        assertEquals("There is no wishlist with Game ID: " + VALID_GAME_ID + " and Client ID: " + VALID_CLIENT_ID, error.getMessage());

    }
}
