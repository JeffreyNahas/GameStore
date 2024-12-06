package ca.mcgill.ecse321.videogamessystem.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;
import ca.mcgill.ecse321.videogamessystem.model.Customer;
import ca.mcgill.ecse321.videogamessystem.model.Review;
import ca.mcgill.ecse321.videogamessystem.model.Wishlist;
import ca.mcgill.ecse321.videogamessystem.repository.CustomerRepository;
import ca.mcgill.ecse321.videogamessystem.repository.WishlistRepository;
import ca.mcgill.ecse321.videogamessystem.service.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCustomer_Success() {
        // Arrange
        String userName = "testUser";
        String email = "test@example.com";
        String password = "password";
        int phoneNumber = 1234567890;
        String address = "123 Test St";

        when(customerRepository.findCustomerByUserName(userName)).thenReturn(null);
        when(customerRepository.findCustomerByEmail(email)).thenReturn(null);

        Customer customer = new Customer(userName, email, password, phoneNumber, address);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act
        Customer createdCustomer = customerService.createCustomer(userName, email, password, phoneNumber, address);

        // Assert
        assertNotNull(createdCustomer);
        assertEquals(userName, createdCustomer.getUserName());
        assertEquals(email, createdCustomer.getEmail());
        assertEquals(phoneNumber, createdCustomer.getPhoneNumber());
        assertEquals(address, createdCustomer.getAddress());
    }

    @Test
    public void testGetCustomerById_CustomerExists() {
        // Arrange
        Long customerId = 1L;
        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Act
        Customer foundCustomer = customerService.getCustomerById(customerId);

        // Assert
        assertNotNull(foundCustomer);
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    public void testGetCustomerById_CustomerDoesNotExist() {
        // Arrange
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            customerService.getCustomerById(customerId);
        });
        assertEquals("Customer with ID 1 not found", exception.getMessage());
    }

    @Test
    public void testUpdateCustomerUserName_Success() {
        // Arrange
        Long customerId = 1L;
        String newUserName = "updatedUser";
        Customer customer = new Customer("oldUser", "old@example.com", "password", 1234, "Old Address");
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        Customer updatedCustomer = customerService.updateCustomerUserName(customerId, newUserName);

        // Assert
        assertNotNull(updatedCustomer);
        assertEquals(newUserName, updatedCustomer.getUserName());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testUpdateCustomerEmail_Success() {
        // Arrange
        Long customerId = 1L;
        String newEmail = "updated@example.com";
        Customer customer = new Customer("testUser", "old@example.com", "password", 1234, "123 Test St");
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        Customer updatedCustomer = customerService.updateCustomerEmail(customerId, newEmail);

        // Assert
        assertNotNull(updatedCustomer);
        assertEquals(newEmail, updatedCustomer.getEmail());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testDeleteCustomer_Success() {
        // Arrange
        Long customerId = 1L;
        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Act
        customerService.deleteCustomer(customerId);

        // Assert
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    public void testGetCustomerByWishlist_Success() {
        // Arrange
        Long wishlistId = 1L;
        Wishlist wishlist = new Wishlist();
        Customer customer = new Customer();
        wishlist.setCustomer(customer);

        when(wishlistRepository.findWishlistById(wishlistId)).thenReturn(wishlist);

        // Act
        Customer foundCustomer = customerService.getCustomerByWishlist(wishlistId);

        // Assert
        assertNotNull(foundCustomer);
        assertEquals(customer, foundCustomer);
    }

    @Test
    public void testGetCustomerByWishlist_NoCustomer() {
        // Arrange
        Long wishlistId = 1L;
        Wishlist wishlist = new Wishlist();

        when(wishlistRepository.findWishlistById(wishlistId)).thenReturn(wishlist);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.getCustomerByWishlist(wishlistId);
        });
        assertEquals("the wishlist has no associated customer", exception.getMessage());
    }

    @Test
    public void testGetCustomerByReview_Success() {
        // Arrange
        Review review = new Review();
        Customer customer = new Customer();
        review.setCustomer(customer);

        // Act
        Customer foundCustomer = customerService.getCustomerByReview(review);

        // Assert
        assertNotNull(foundCustomer);
        assertEquals(customer, foundCustomer);
    }

    @Test
    public void testGetCustomerByReview_NoCustomer() {
        // Arrange
        Review review = new Review();

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            customerService.getCustomerByReview(review);
        });
        assertEquals("Review does not belong to any customer", exception.getMessage());
    }

    // Commented tests are here for potential future use but remain as-is.
    // @Test
    // public void testUpdateCustomer_Success() {
    //     // Arrange
    //     Long customerId = 1L;
    //     Customer customer = new Customer("oldUser", "old@example.com", "password", 1234, "Old Address");
    //     when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
    
    //     // Mock save to return the updated customer
    //     when(customerRepository.save(customer)).thenReturn(customer);
    
    //     // Act
    //     Customer updatedCustomer = customerService.updateCustomer(customerId, "newUser", "new@example.com", 5678, "New Address");
    
    //     // Assert
    //     assertNotNull(updatedCustomer);
    //     assertEquals("newUser", updatedCustomer.getUserName());
    //     assertEquals("new@example.com", updatedCustomer.getEmail());
    //     assertEquals(5678, updatedCustomer.getPhoneNumber());
    //     assertEquals("New Address", updatedCustomer.getAddress());
    //     verify(customerRepository, times(1)).save(customer);
    // }
}
