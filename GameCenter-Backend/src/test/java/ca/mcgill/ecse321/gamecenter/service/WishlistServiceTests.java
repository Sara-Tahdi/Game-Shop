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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

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
        
        when(mockWishlistRepo.save(any(Wishlist.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));

        // Act
        Wishlist createdWishlist = service.createWishlist(VALID_CLIENT_ID, VALID_GAME_ID);

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
    public void testRemoveGameFromWishlist() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        when(mockWishlistRepo.findWishlistByClientIdAndGameId(VALID_CLIENT_ID, VALID_GAME_ID)).thenReturn(Optional.of(wishlist));

        // Act
        service.removeWishlist(VALID_CLIENT_ID, VALID_GAME_ID);

        // Assert
        verify(mockWishlistRepo, times(1)).delete(wishlist);
    }

    @Test
    public void testRemoveGameFromWishlistNotFound() {
        // Arrange
        when(mockWishlistRepo.findWishlistByClientIdAndGameId(VALID_CLIENT_ID, VALID_GAME_ID)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> service.removeWishlist(VALID_CLIENT_ID, VALID_GAME_ID));
        assertEquals("There is no wishlist with Game ID: " + VALID_GAME_ID + " and Client ID: " + VALID_CLIENT_ID, error.getMessage());

    }

    @Test
    public void testCreateWishlistValid() {
        // Arrange
        Client mockClient = new Client();
        mockClient.setId(VALID_CLIENT_ID);

        Game mockGame = new Game();
        mockGame.setId(VALID_GAME_ID);

        when(mockClientRepo.findById(VALID_CLIENT_ID)).thenReturn(Optional.of(mockClient));
        when(mockGameRepo.findById(VALID_GAME_ID)).thenReturn(Optional.of(mockGame));
        when(mockWishlistRepo.save(any(Wishlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Wishlist wishlist = service.createWishlist(VALID_CLIENT_ID, VALID_GAME_ID);

        // Assert
        assertNotNull(wishlist);
        assertEquals(mockClient, wishlist.getClient());
        assertEquals(mockGame, wishlist.getGame());

        verify(mockClientRepo).save(mockClient);
        verify(mockGameRepo).save(mockGame);
        verify(mockWishlistRepo).save(wishlist);
    }

    @Test
    public void testCreateWishlistInvalidClient() {
        // Arrange
        when(mockClientRepo.findById(VALID_CLIENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.createWishlist(VALID_CLIENT_ID, VALID_GAME_ID));
        
        assertEquals("No client found with client id: " + VALID_CLIENT_ID, exception.getMessage());
    }

    @Test
    public void testFindWishlistsByValidClientId() {
        // Arrange
        List<Wishlist> wishlists = new ArrayList<>();
        Wishlist wishlist1 = new Wishlist();
        Wishlist wishlist2 = new Wishlist();
        wishlists.add(wishlist1);
        wishlists.add(wishlist2);
        
        when(mockWishlistRepo.findWishlistsByClientId(VALID_CLIENT_ID)).thenReturn(Optional.of(wishlists));

        // Act
        List<Wishlist> foundWishlists = service.findWishlistsByClientId(VALID_CLIENT_ID);

        // Assert
        assertNotNull(foundWishlists);
        assertEquals(2, foundWishlists.size());
    }

    @Test
    public void testFindWishlistsByInvalidClientId() {
        // Arrange
        when(mockWishlistRepo.findWishlistsByClientId(VALID_CLIENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.findWishlistsByClientId(VALID_CLIENT_ID));
        assertEquals("There is no wishlist with Client ID: " + VALID_CLIENT_ID, exception.getMessage());
    }

    @Test
    public void testFindWishlistsByValidGameId() {
        // Arrange
        List<Wishlist> wishlists = new ArrayList<>();
        Wishlist wishlist1 = new Wishlist();
        Wishlist wishlist2 = new Wishlist();
        wishlists.add(wishlist1);
        wishlists.add(wishlist2);

        when(mockWishlistRepo.findWishlistsByGameId(VALID_GAME_ID)).thenReturn(Optional.of(wishlists));

        // Act
        List<Wishlist> foundWishlists = service.findWishlistsByGameId(VALID_GAME_ID);

        // Assert
        assertNotNull(foundWishlists);
        assertEquals(2, foundWishlists.size());
    }

    @Test
    public void testFindWishlistsByInvalidGameId() {
        // Arrange
        when(mockWishlistRepo.findWishlistsByGameId(VALID_GAME_ID)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.findWishlistsByGameId(VALID_GAME_ID));
        assertEquals("There is no wishlist with Game ID: " + VALID_GAME_ID, exception.getMessage());
    }

    @Test
    public void testFindWishlistByValidClientIdAndGameId() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        when(mockWishlistRepo.findWishlistByClientIdAndGameId(VALID_CLIENT_ID, VALID_GAME_ID))
                .thenReturn(Optional.of(wishlist));

        // Act
        Wishlist foundWishlist = service.findWishlistByClientIdAndGameId(VALID_GAME_ID, VALID_CLIENT_ID);

        // Assert
        assertNotNull(foundWishlist);
    }

    @Test
    public void testFindWishlistByInvalidClientIdAndGameId() {
        // Arrange
        when(mockWishlistRepo.findWishlistByClientIdAndGameId(VALID_CLIENT_ID, VALID_GAME_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.findWishlistByClientIdAndGameId(VALID_GAME_ID, VALID_CLIENT_ID));
        assertEquals("There is no wishlist with Game ID: " + VALID_GAME_ID + " and Client ID: " + VALID_CLIENT_ID, 
                     exception.getMessage());
    }
}