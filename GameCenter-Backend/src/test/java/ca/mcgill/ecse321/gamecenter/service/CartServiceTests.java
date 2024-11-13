package ca.mcgill.ecse321.gamecenter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ca.mcgill.ecse321.gamecenter.model.Cart;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.repository.CartRepository;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;

public class CartServiceTests {
    @Mock
    private CartRepository mockCartRepo;

    @Mock
    private ClientRepository mockClientRepo;

    @Mock
    private GameRepository mockGameRepo;

    @InjectMocks
    private CartService service;

    private static final int VALID_CART_ID = 1;
    private static final int VALID_CLIENT_ID = 1;
    private static final int VALID_GAME_ID = 1;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateValidCart() {
        // Arrange
        Client mockClient = new Client();
        Game mockGame = new Game();

        when(mockClientRepo.findById(VALID_CLIENT_ID)).thenReturn(Optional.of(mockClient));
        when(mockGameRepo.findById(VALID_GAME_ID)).thenReturn(Optional.of(mockGame));
        when(mockCartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Cart createdCart = service.createCart(VALID_CLIENT_ID, VALID_GAME_ID);

        // Assert
        assertNotNull(createdCart);
        verify(mockCartRepo, times(1)).save(createdCart);
    }

    @Test
    public void testFindCartByValidId() {
        // Arrange
        Cart cart = new Cart();
        cart.setId(VALID_CART_ID);
        when(mockCartRepo.findCartById(VALID_CART_ID)).thenReturn(Optional.of(cart));

        // Act
        Cart foundCart = service.findCartById(VALID_CART_ID);

        // Assert
        assertNotNull(foundCart);
        assertEquals(VALID_CART_ID, foundCart.getId());
    }

    @Test
    public void testFindCartByInvalidId() {
        // Arrange
        when(mockCartRepo.findCartById(VALID_CART_ID)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.findCartById(VALID_CART_ID));
        assertEquals("There is no cart with Cart ID: " + VALID_CART_ID, ex.getMessage());
    }

    @Test
    public void testFindCartsByClientId() {
        // Arrange
        Cart cart = new Cart();
        when(mockCartRepo.findCartsByClientId(VALID_CLIENT_ID)).thenReturn(Optional.of(List.of(cart)));

        // Act
        List<Cart> carts = service.findCartsByClientId(VALID_CLIENT_ID);

        // Assert
        assertNotNull(carts);
        assertEquals(1, carts.size());
    }

    @Test
    public void testFindCartsByInvalidClientId() {
        // Arrange
        when(mockCartRepo.findCartsByClientId(VALID_CLIENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.findCartsByClientId(VALID_CLIENT_ID));
        assertEquals("There is no carts with Client ID: " + VALID_CLIENT_ID, ex.getMessage());
    }

    @Test
    public void testFindCartsByValidGameId() {
        // Arrange
        List<Cart> expectedCarts = new ArrayList<>();
        Cart cart1 = new Cart();
        cart1.setId(1);
        expectedCarts.add(cart1);
        
        Cart cart2 = new Cart();
        cart2.setId(2);
        expectedCarts.add(cart2);

        when(mockCartRepo.findCartsByGameId(VALID_GAME_ID)).thenReturn(Optional.of(expectedCarts));

        // Act
        List<Cart> foundCarts = service.findCartsByGameId(VALID_GAME_ID);

        // Assert
        assertNotNull(foundCarts);
        assertEquals(2, foundCarts.size());
        assertEquals(cart1.getId(), foundCarts.get(0).getId());
        assertEquals(cart2.getId(), foundCarts.get(1).getId());
    }

    @Test
    public void testFindCartsByInvalidGameId() {
        // Arrange
        when(mockCartRepo.findCartsByGameId(VALID_GAME_ID)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, 
            () -> service.findCartsByGameId(VALID_GAME_ID));
        assertEquals("There is no carts with Game ID: " + VALID_GAME_ID, error.getMessage());
    }

    @Test
    public void testFindCartByClientIdAndGameIdFound() {
        // Arrange
        Cart cart = new Cart();
        cart.setId(VALID_CART_ID);
        when(mockCartRepo.findCartByClientIdAndGameId(VALID_CLIENT_ID, VALID_GAME_ID)).thenReturn(Optional.of(cart));

        // Act
        Cart foundCart = service.findCartByClientIdAndGameId(VALID_GAME_ID, VALID_CLIENT_ID);

        // Assert
        assertNotNull(foundCart);
        assertEquals(VALID_CART_ID, foundCart.getId());
    }

    @Test
    public void testFindCartByClientIdAndGameIdNotFound() {
        // Arrange
        when(mockCartRepo.findCartByClientIdAndGameId(VALID_CLIENT_ID, VALID_GAME_ID))
            .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> service.findCartByClientIdAndGameId(VALID_GAME_ID, VALID_CLIENT_ID));
        assertEquals("There is no cart with Game ID: " + VALID_GAME_ID + " and Client ID: " + VALID_CLIENT_ID, exception.getMessage());
    }

    @Test
    public void testAddGameToCartValid() {
        // Arrange
        Client client = new Client();
        client.setId(VALID_CLIENT_ID);
        Game game = new Game();
        game.setId(VALID_GAME_ID);
        
        when(mockClientRepo.findById(VALID_CLIENT_ID)).thenReturn(Optional.of(client));
        when(mockGameRepo.findById(VALID_GAME_ID)).thenReturn(Optional.of(game));
        when(mockCartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Cart cart = service.createCart(VALID_CLIENT_ID, VALID_GAME_ID);

        // Assert
        assertNotNull(cart);
        assertEquals(VALID_CLIENT_ID, cart.getClient().getId());
        assertEquals(VALID_GAME_ID, cart.getGame().getId());
    }

    @Test
    public void testAddGameToCartInvalidClient() {
        // Arrange
        when(mockClientRepo.findById(VALID_CLIENT_ID)).thenReturn(Optional.empty());
        when(mockGameRepo.findById(VALID_GAME_ID)).thenReturn(Optional.of(new Game()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.createCart(VALID_CLIENT_ID, VALID_GAME_ID));
        assertEquals("No client found with client id: " + VALID_CLIENT_ID, exception.getMessage());
    }

    @Test
    public void testAddGameToCartInvalidGame() {
        // Arrange
        Client client = new Client();
        client.setId(VALID_CLIENT_ID);
        when(mockClientRepo.findById(VALID_CLIENT_ID)).thenReturn(Optional.of(client));
        when(mockGameRepo.findById(VALID_GAME_ID)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.createCart(VALID_CLIENT_ID, VALID_GAME_ID));
        assertEquals("No game found with game id: " + VALID_GAME_ID, exception.getMessage());
    }
    
    @Test
    public void testRemoveGameFromCart() {
        // Arrange
        Cart cart = new Cart();
        when(mockCartRepo.findCartByClientIdAndGameId(VALID_CLIENT_ID, VALID_GAME_ID)).thenReturn(Optional.of(cart));

        // Act
        service.removeCart(VALID_CLIENT_ID, VALID_GAME_ID);

        // Assert
        verify(mockCartRepo, times(1)).delete(cart);
    }

    @Test
    public void testRemoveGameFromCartNotFound() {
        // Arrange
        when(mockCartRepo.findCartByClientIdAndGameId(VALID_CLIENT_ID, VALID_GAME_ID)).thenReturn(Optional.empty());
    
        // Act & Assert
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> service.removeCart(VALID_CLIENT_ID, VALID_GAME_ID));
        assertEquals("There is no cart with Game ID: " + VALID_GAME_ID + " and Client ID: " + VALID_CLIENT_ID, error.getMessage());
    }
    
}
