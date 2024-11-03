package ca.mcgill.ecse321.gamecenter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
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
        Cart cart = new Cart();
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
    
}
