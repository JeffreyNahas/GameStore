package ca.mcgill.ecse321.videogamessystem.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerResponseDto;
import ca.mcgill.ecse321.videogamessystem.repository.CustomerRepository;

@SpringBootTest(classes = VideogamessystemApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    private static final String VALID_USERNAME = "testuser";
    private static final String VALID_EMAIL = "testuser@example.com";
    private static final String VALID_PASSWORD = "securepassword";
    private static final int VALID_PHONENUMBER = 123456789;
    private static final String VALID_ADDRESS = "123 Test Street";

    private static final String UPDATED_USERNAME = "updateduser";
    private static final String UPDATED_EMAIL = "updateduser@example.com";
    private static final int UPDATED_PHONENUMBER = 987654321;
    private static final String UPDATED_ADDRESS = "456 Updated Street";

    private Long customerId;

    @AfterAll
    public void clearDatabase() {
        customerRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateCustomer() {
        // Arrange
        CustomerRequestDto request = new CustomerRequestDto(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PHONENUMBER, VALID_ADDRESS);

        // Act
        ResponseEntity<CustomerResponseDto> response = restTemplate.postForEntity("/customers", request, CustomerResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "The ID should be positive.");
        this.customerId = response.getBody().getId();
        assertEquals(VALID_USERNAME, response.getBody().getUserName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
        assertEquals(VALID_PHONENUMBER, response.getBody().getPhoneNumber());
        assertEquals(VALID_ADDRESS, response.getBody().getAddress());
    }

    @Test
    @Order(2)
    public void testGetCustomerById() {
        // Arrange
        String url = String.format("/customers/%d", this.customerId);

        // Act
        ResponseEntity<CustomerResponseDto> response = restTemplate.getForEntity(url, CustomerResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(this.customerId, response.getBody().getId());
        assertEquals(VALID_USERNAME, response.getBody().getUserName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
        assertEquals(VALID_PHONENUMBER, response.getBody().getPhoneNumber());
        assertEquals(VALID_ADDRESS, response.getBody().getAddress());
    }

    @Test
    @Order(3)
    public void testUpdateCustomerDetails() {
        // Update username
        String updateUsernameUrl = String.format("/customers/%d/userName?newUserName=%s", this.customerId, UPDATED_USERNAME);
        restTemplate.put(updateUsernameUrl, null);
        ResponseEntity<CustomerResponseDto> responseAfterUsernameUpdate = restTemplate.getForEntity(String.format("/customers/%d", this.customerId), CustomerResponseDto.class);
        assertEquals(UPDATED_USERNAME, responseAfterUsernameUpdate.getBody().getUserName());

        // Update email
        String updateEmailUrl = String.format("/customers/%d/email?newEmail=%s", this.customerId, UPDATED_EMAIL);
        restTemplate.put(updateEmailUrl, null);
        ResponseEntity<CustomerResponseDto> responseAfterEmailUpdate = restTemplate.getForEntity(String.format("/customers/%d", this.customerId), CustomerResponseDto.class);
        assertEquals(UPDATED_EMAIL, responseAfterEmailUpdate.getBody().getEmail());

        // Update phone number
        String updatePhoneNumberUrl = String.format("/customers/%d/phoneNumber?newPhoneNumber=%d", this.customerId, UPDATED_PHONENUMBER);
        restTemplate.put(updatePhoneNumberUrl, null);
        ResponseEntity<CustomerResponseDto> responseAfterPhoneNumberUpdate = restTemplate.getForEntity(String.format("/customers/%d", this.customerId), CustomerResponseDto.class);
        assertEquals(UPDATED_PHONENUMBER, responseAfterPhoneNumberUpdate.getBody().getPhoneNumber());

        // Update address
        String updateAddressUrl = String.format("/customers/%d/address?newAddress=%s", this.customerId, UPDATED_ADDRESS);
        restTemplate.put(updateAddressUrl, null);
        ResponseEntity<CustomerResponseDto> responseAfterAddressUpdate = restTemplate.getForEntity(String.format("/customers/%d", this.customerId), CustomerResponseDto.class);
        assertEquals(UPDATED_ADDRESS, responseAfterAddressUpdate.getBody().getAddress());
    }

    @Test
    @Order(4)
    public void testDeleteCustomer() {
        // Act
        restTemplate.delete(String.format("/customers/%d", this.customerId));

        // Assert
        ResponseEntity<CustomerResponseDto> response = restTemplate.getForEntity(String.format("/customers/%d", this.customerId), CustomerResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
