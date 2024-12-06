package ca.mcgill.ecse321.videogamessystem.integration_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.Arrays;

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
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;

import ca.mcgill.ecse321.videogamessystem.VideogamessystemApplication;
import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerResponseDto;
import ca.mcgill.ecse321.videogamessystem.dto.GameDto.GameRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.GameDto.GameResponseDto;
import ca.mcgill.ecse321.videogamessystem.dto.SpecificGameDto.SpecificGameRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.SpecificGameDto.SpecificGameResponseDto;
import ca.mcgill.ecse321.videogamessystem.dto.SpecificOrderDto.SpecificOrderRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.SpecificOrderDto.SpecificOrderResponseDto;
import ca.mcgill.ecse321.videogamessystem.model.Game.Category;
import ca.mcgill.ecse321.videogamessystem.model.Game.ConsoleType;
import ca.mcgill.ecse321.videogamessystem.repository.SpecificOrderRepository;

@SpringBootTest(classes = VideogamessystemApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SpecificOrderIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SpecificOrderRepository specificOrderRepository;

    private static final String VALID_GAME_TITLE = "Test Game";
    private static final String VALID_GAME_DESCRIPTION = "Test game description.";
    private static final int VALID_GAME_PRICE = 50;
    private static final Category VALID_GAME_CATEGORY = Category.Adventure;
    private static final ConsoleType VALID_GAME_CONSOLE_TYPE = ConsoleType.PS4;

    private static final int VALID_SPECIFIC_GAME_SERIAL_NUMBER = 1001;
    private static final boolean VALID_SPECIFIC_GAME_AVAILABILITY = true;

    private static final String VALID_CUSTOMER_USERNAME = "testcustomer";
    private static final String VALID_CUSTOMER_EMAIL = "testcustomer@example.com";
    private static final String VALID_CUSTOMER_PASSWORD = "password";
    private static final int UPDATED_PHONENUMBER = 987654321;
    private static final String UPDATED_ADDRESS = "456 Updated Street";

    private static final Date VALID_ORDER_DATE = new Date(System.currentTimeMillis());
    private static final int VALID_CARD_NUMBER = 12345678;

    private Long gameId;
    private Long customerId;
    private int specificOrderId;

    @AfterAll
    public void clearDatabase() {
        specificOrderRepository.deleteAll();
    }

    // @Test
    // @Order(1)
    // public void testCreateGame() {
    //     // Arrange
    //     GameRequestDto gameRequest = new GameRequestDto(
    //             VALID_GAME_DESCRIPTION, VALID_GAME_PRICE, VALID_GAME_TITLE, VALID_GAME_CATEGORY, VALID_GAME_CONSOLE_TYPE
    //     );

    //     // Act
    //     ResponseEntity<GameResponseDto> response = restTemplate.postForEntity(
    //             "/games", gameRequest, GameResponseDto.class
    //     );

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());
    //     assertTrue(response.getBody().getId() > 0, "Game ID should be positive.");
    //     this.gameId = response.getBody().getId();
    //     assertEquals(VALID_GAME_TITLE, response.getBody().getTitle());
    //     assertEquals(VALID_GAME_DESCRIPTION, response.getBody().getDescription());
    // }

    // @Test
    // @Order(2)
    // public void testCreateSpecificGame() {
    //     // Arrange
    //     SpecificGameRequestDto specificGameRequest = new SpecificGameRequestDto(
    //         VALID_SPECIFIC_GAME_AVAILABILITY,
    //         VALID_SPECIFIC_GAME_SERIAL_NUMBER,
    //         gameId
    //     );

    //     // Act
    //     ResponseEntity<SpecificGameResponseDto> response = restTemplate.postForEntity(
    //         "/specificGames", specificGameRequest, SpecificGameResponseDto.class
    //     );

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());
    //     assertEquals(VALID_SPECIFIC_GAME_SERIAL_NUMBER, response.getBody().getSerialNumber());
    //     assertEquals(VALID_SPECIFIC_GAME_AVAILABILITY, response.getBody().isAvailability());
    //     assertEquals(gameId, response.getBody().getGame().getId());
    // }

    @Test
    @Order(1)
    public void testCreateCustomer() {
        // Arrange
        CustomerRequestDto customerRequest = new CustomerRequestDto(
                VALID_CUSTOMER_USERNAME, VALID_CUSTOMER_EMAIL, VALID_CUSTOMER_PASSWORD, UPDATED_PHONENUMBER, UPDATED_ADDRESS
        );

        // Act
        ResponseEntity<CustomerResponseDto> response = restTemplate.postForEntity(
                "/customers", customerRequest, CustomerResponseDto.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Customer ID should be positive.");
        this.customerId = response.getBody().getId();
        assertEquals(VALID_CUSTOMER_USERNAME, response.getBody().getUserName());
    }

    @Test
    @Order(2)
    public void testCreateSpecificOrder() {
        // Arrange
        SpecificOrderRequestDto orderRequest = new SpecificOrderRequestDto(
                VALID_ORDER_DATE, VALID_CARD_NUMBER, customerId
        );

        // Act
        ResponseEntity<SpecificOrderResponseDto> response = restTemplate.postForEntity(
                "/orders", orderRequest, SpecificOrderResponseDto.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getOrderNumber() > 0, "Order ID should be positive.");
        this.specificOrderId = response.getBody().getOrderNumber();
        assertEquals(VALID_CARD_NUMBER, response.getBody().getCardNumber());
        assertEquals(customerId, response.getBody().getCustomer().getId());
    }

    @Test
    @Order(3)
    public void testGetOrderById() {
        // Act
        ResponseEntity<SpecificOrderResponseDto> response = restTemplate.getForEntity(
                "/orders/" + specificOrderId, SpecificOrderResponseDto.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(specificOrderId, response.getBody().getOrderNumber());
    }

    @Test
    @Order(4)
    public void testGetOrdersByCustomer() {
        // Act
        ResponseEntity<SpecificOrderResponseDto[]> response = restTemplate.getForEntity(
                "/orders/customer/" + customerId, SpecificOrderResponseDto[].class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecificOrderResponseDto[] orders = response.getBody();
        assertNotNull(orders);
        assertTrue(orders.length > 0, "Customer should have at least one order.");
        boolean found = false;
        for (SpecificOrderResponseDto order : orders) {
            if (order.getOrderNumber() == specificOrderId) {
                found = true;
                assertEquals(customerId, order.getCustomer().getId());
                break;
            }
        }
        assertTrue(found, "Order should be found in customer's orders.");
    }

    @Test
    @Order(5)
    public void testUpdateCardNumber() {
        // Arrange
        int newCardNumber = 87654321;
        String url = String.format("/orders/%d/cardNumber?newCardNumber=%d", specificOrderId, newCardNumber);

        // Act
        ResponseEntity<SpecificOrderResponseDto> response = restTemplate.exchange(
                url, HttpMethod.PUT, null, SpecificOrderResponseDto.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecificOrderResponseDto updatedOrder = response.getBody();
        assertNotNull(updatedOrder);
        assertEquals(newCardNumber, updatedOrder.getCardNumber(), "Card number should be updated.");

        // Verify the update
        ResponseEntity<SpecificOrderResponseDto> getResponse = restTemplate.getForEntity(
                "/orders/" + specificOrderId, SpecificOrderResponseDto.class
        );
        assertNotNull(getResponse);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(newCardNumber, getResponse.getBody().getCardNumber(), "Card number should be updated.");
    }

    @Test
    @Order(6)
    public void testGetAllOrders() {
        // Act
        ResponseEntity<SpecificOrderResponseDto[]> response = restTemplate.getForEntity(
                "/orders", SpecificOrderResponseDto[].class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecificOrderResponseDto[] orders = response.getBody();
        assertNotNull(orders);
        assertTrue(orders.length > 0, "There should be at least one order.");
        boolean found = Arrays.stream(orders).anyMatch(order -> order.getOrderNumber() == specificOrderId);
        assertTrue(found, "Created order should be in the list of all orders.");
    }

    @Test
    @Order(7)
    public void testDeleteOrder() {
        // Act
        String url = "/orders/" + specificOrderId;
        ResponseEntity<Void> response = restTemplate.exchange(
                url, HttpMethod.DELETE, null, Void.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify deletion
        ResponseEntity<String> getResponse = restTemplate.getForEntity(
                "/orders/" + specificOrderId, String.class
        );
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}
