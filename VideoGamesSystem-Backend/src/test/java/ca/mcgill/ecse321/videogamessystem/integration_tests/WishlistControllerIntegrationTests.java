package ca.mcgill.ecse321.videogamessystem.integration_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.videogamessystem.VideogamessystemApplication;
import ca.mcgill.ecse321.videogamessystem.dto.WishlistDto.WishlistRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.WishlistDto.WishlistResponseDto;
import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerResponseDto;
import ca.mcgill.ecse321.videogamessystem.repository.CustomerRepository;
import ca.mcgill.ecse321.videogamessystem.repository.WishlistRepository;

@SpringBootTest(classes = VideogamessystemApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class WishlistControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    private static final String CUSTOMER_USERNAME = "testuser";
    private static final String CUSTOMER_EMAIL = "testuser@example.com";
    private static final String CUSTOMER_PASSWORD = "password";
    private static final int CUSTOMER_PHONE = 123456789;
    private static final String CUSTOMER_ADDRESS = "123 Test Street";

    private static final int INITIAL_NB_OF_ITEMS = 0;
    private static final int UPDATED_NB_OF_ITEMS = 5;

    private Long customerId;
    private Long wishlistId;

    @AfterAll
    public void clearDatabase() {
        wishlistRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateCustomerForWishlist() {
        // Arrange
        CustomerRequestDto request = new CustomerRequestDto(
                CUSTOMER_USERNAME, CUSTOMER_EMAIL, CUSTOMER_PASSWORD, CUSTOMER_PHONE, CUSTOMER_ADDRESS);

        // Act
        ResponseEntity<CustomerResponseDto> response = restTemplate.postForEntity("/customers", request, CustomerResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Customer ID should be positive.");
        this.customerId = response.getBody().getId();
        this.wishlistId = wishlistRepository.findWishlistByCustomer(customerRepository.findCustomerById(customerId)).getId();
    }

    // @Test
    // @Order(2)
    // public void testCreateWishlist() {
    //     // Arrange
    //     WishlistRequestDto request = new WishlistRequestDto(customerId);

    //     // Act
    //     ResponseEntity<WishlistResponseDto> response = restTemplate.postForEntity("/wishlists", request, WishlistResponseDto.class);

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());
    //     assertTrue(response.getBody().getId() > 0, "Wishlist ID should be positive.");
    //     this.wishlistId = response.getBody().getId();
    //     assertEquals(customerId, response.getBody().getCustomer().getId());
    //     assertEquals(INITIAL_NB_OF_ITEMS, response.getBody().getNbOfItems());
    // }

    @Test
    @Order(2)
    public void testGetWishlistById() {
        // Arrange
        String url = String.format("/wishlists/%d", wishlistId);

        // Act
        ResponseEntity<WishlistResponseDto> response = restTemplate.getForEntity(url, WishlistResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(wishlistId, response.getBody().getId());
        assertEquals(customerId, response.getBody().getCustomer().getId());
        assertEquals(INITIAL_NB_OF_ITEMS, response.getBody().getNbOfItems());
    }

    @Test
    @Order(3)
    public void testGetWishlistByCustomerId() {
        // Arrange
        String url = String.format("/wishlists/customer/%d", customerId);

        // Act
        ResponseEntity<WishlistResponseDto> response = restTemplate.getForEntity(url, WishlistResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(wishlistId, response.getBody().getId());
        assertEquals(customerId, response.getBody().getCustomer().getId());
        assertEquals(INITIAL_NB_OF_ITEMS, response.getBody().getNbOfItems());
    }

    // @Test
    // @Order(5)
    // public void testUpdateWishlistNbOfItems() {
    //     // Arrange
    //     String url = String.format("/wishlists/%d?nbOfItems=%d", wishlistId, UPDATED_NB_OF_ITEMS);

    //     // Act
    //     restTemplate.put(url, null);

    //     // Assert
    //     ResponseEntity<WishlistResponseDto> response = restTemplate.getForEntity(String.format("/wishlists/%d", wishlistId), WishlistResponseDto.class);
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());
    //     assertEquals(UPDATED_NB_OF_ITEMS, response.getBody().getNbOfItems());
    // }
}
