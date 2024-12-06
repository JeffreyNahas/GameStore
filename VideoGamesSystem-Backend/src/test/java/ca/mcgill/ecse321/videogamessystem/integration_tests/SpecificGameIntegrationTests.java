// package ca.mcgill.ecse321.videogamessystem.integration_tests;

// import static org.junit.jupiter.api.Assertions.*;

// import java.sql.Date;
// import java.time.LocalDate;
// import java.util.List;

// import org.junit.jupiter.api.AfterAll;
// import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
// import org.junit.jupiter.api.Order;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import org.junit.jupiter.api.TestInstance.Lifecycle;
// import org.junit.jupiter.api.TestMethodOrder;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.http.*;
// import org.springframework.boot.test.web.client.TestRestTemplate;

// import ca.mcgill.ecse321.videogamessystem.VideogamessystemApplication;
// import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerRequestDto;
// import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerResponseDto;
// import ca.mcgill.ecse321.videogamessystem.dto.GameDto.GameRequestDto;
// import ca.mcgill.ecse321.videogamessystem.dto.GameDto.GameResponseDto;
// import ca.mcgill.ecse321.videogamessystem.dto.SpecificGameDto.SpecificGameRequestDto;
// import ca.mcgill.ecse321.videogamessystem.dto.SpecificGameDto.SpecificGameResponseDto;
// import ca.mcgill.ecse321.videogamessystem.dto.SpecificOrderDto.SpecificOrderRequestDto;
// import ca.mcgill.ecse321.videogamessystem.dto.SpecificOrderDto.SpecificOrderResponseDto;
// import ca.mcgill.ecse321.videogamessystem.model.Game.Category;
// import ca.mcgill.ecse321.videogamessystem.model.Game.ConsoleType;
// import ca.mcgill.ecse321.videogamessystem.repository.CustomerRepository;
// import ca.mcgill.ecse321.videogamessystem.repository.SpecificGameRepository;
// import ca.mcgill.ecse321.videogamessystem.repository.SpecificOrderRepository;

// @SpringBootTest(classes = VideogamessystemApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
// @TestMethodOrder(OrderAnnotation.class)
// @TestInstance(Lifecycle.PER_CLASS)
// public class SpecificGameIntegrationTests {

//     @Autowired
//     private TestRestTemplate restTemplate;

//     @Autowired
//     private SpecificGameRepository specificGameRepository;

//     @Autowired
//     private SpecificOrderRepository specificOrderRepository;

//     @Autowired
//     private CustomerRepository customerRepository;

//     private static final String VALID_TITLE = "Test Game";
//     private static final String VALID_DESCRIPTION = "Test game description.";
//     private static final int VALID_PRICE = 50;
//     private static final Category VALID_CATEGORY = Category.Adventure;
//     private static final ConsoleType VALID_CONSOLE_TYPE = ConsoleType.PS4;

//     private static final int SPECIFIC_GAME_SERIAL_NUMBER = 12345;
//     private static final boolean SPECIFIC_GAME_AVAILABILITY = true;

//     private Long gameId;
//     private int specificGameSerialNumber;
//     private int orderNumber;
//     private Long customerId;

//     @AfterAll
//     public void clearDatabase() {
//         specificGameRepository.deleteAll();
//         specificOrderRepository.deleteAll();
//         customerRepository.deleteAll();
//     }

//     @Test
//     @Order(1)
//     public void testCreateGame() {
//         // Arrange
//         GameRequestDto gameRequest = new GameRequestDto(
//                 VALID_DESCRIPTION,
//                 VALID_PRICE,
//                 VALID_TITLE,
//                 VALID_CATEGORY,
//                 VALID_CONSOLE_TYPE
//         );

//         // Act
//         ResponseEntity<GameResponseDto> response = restTemplate.postForEntity("/games", gameRequest, GameResponseDto.class);

//         // Assert
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotNull(response.getBody());
//         this.gameId = response.getBody().getId();
//         assertNotNull(gameId);
//         assertEquals(VALID_TITLE, response.getBody().getTitle());
//     }

//     @Test
//     @Order(2)
//     public void testCreateSpecificGame() {
//         // Arrange
//         SpecificGameRequestDto specificGameRequest = new SpecificGameRequestDto(
//                 SPECIFIC_GAME_AVAILABILITY,
//                 SPECIFIC_GAME_SERIAL_NUMBER,
//                 gameId
//         );

//         // Act
//         ResponseEntity<SpecificGameResponseDto> response = restTemplate.postForEntity("/specificGames", specificGameRequest, SpecificGameResponseDto.class);

//         // Assert
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotNull(response.getBody());
//         assertEquals(SPECIFIC_GAME_SERIAL_NUMBER, response.getBody().getSerialNumber());
//         assertEquals(SPECIFIC_GAME_AVAILABILITY, response.getBody().isAvailability());
//         assertEquals(gameId, response.getBody().getGame().getId());

//         this.specificGameSerialNumber = response.getBody().getSerialNumber();
//     }

//     @Test
//     @Order(3)
//     public void testGetSpecificGameBySerialNumber() {
//         // Act
//         ResponseEntity<SpecificGameResponseDto> response = restTemplate.getForEntity(
//                 "/specificGames/" + specificGameSerialNumber,
//                 SpecificGameResponseDto.class
//         );

//         // Assert
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotNull(response.getBody());
//         assertEquals(specificGameSerialNumber, response.getBody().getSerialNumber());
//         assertEquals(SPECIFIC_GAME_AVAILABILITY, response.getBody().isAvailability());
//     }

//     @Test
//     @Order(4)
//     public void testGetAllSpecificGames() {
//         // Act
//         ResponseEntity<SpecificGameResponseDto[]> response = restTemplate.getForEntity("/specificGames", SpecificGameResponseDto[].class);

//         // Assert
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         SpecificGameResponseDto[] specificGames = response.getBody();
//         assertNotNull(specificGames);
//         assertTrue(specificGames.length > 0);
//         boolean found = false;
//         for (SpecificGameResponseDto specificGame : specificGames) {
//             if (specificGame.getSerialNumber() == specificGameSerialNumber) {
//                 found = true;
//                 break;
//             }
//         }
//         assertTrue(found, "Specific game should be in the list of all specific games.");
//     }

//     @Test
//     @Order(5)
//     public void testUpdateAvailability() {
//         // Arrange
//         String url = String.format("/specificGames/%d/availability?newAvailability=false", specificGameSerialNumber);

//         // Act
//         ResponseEntity<SpecificGameResponseDto> response = restTemplate.exchange(
//                 url,
//                 HttpMethod.PUT,
//                 null,
//                 SpecificGameResponseDto.class
//         );

//         // Assert
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         SpecificGameResponseDto updatedSpecificGame = response.getBody();
//         assertNotNull(updatedSpecificGame);
//         assertFalse(updatedSpecificGame.isAvailability(), "Availability should be updated to false.");

//         // Verify the update
//         ResponseEntity<SpecificGameResponseDto> getResponse = restTemplate.getForEntity(
//                 "/specificGames/" + specificGameSerialNumber,
//                 SpecificGameResponseDto.class
//         );
//         assertNotNull(getResponse);
//         assertEquals(HttpStatus.OK, getResponse.getStatusCode());
//         assertNotNull(getResponse.getBody());
//         assertFalse(getResponse.getBody().isAvailability(), "Availability should be updated to false.");
//     }

//     @Test
//     @Order(6)
//     public void testAddSpecificGameToOrder() {
//         // Arrange
//         // Create a customer
//         CustomerRequestDto customerRequest = new CustomerRequestDto("Test User", "test@example.com", "password", 1233454567, "123 chemin 12");
//         ResponseEntity<CustomerResponseDto> customerResponse = restTemplate.postForEntity("/customers", customerRequest, CustomerResponseDto.class);
//         assertNotNull(customerResponse);
//         assertEquals(HttpStatus.OK, customerResponse.getStatusCode());
//         assertNotNull(customerResponse.getBody());
//         this.customerId = customerResponse.getBody().getId();
//         assertNotNull(this.customerId);

//         // Create an order
//         SpecificOrderRequestDto orderRequest = new SpecificOrderRequestDto(
//                 Date.valueOf(LocalDate.now()),
//                 123456789,
//                 customerId
//         );
//         ResponseEntity<SpecificOrderResponseDto> orderResponse = restTemplate.postForEntity("/orders", orderRequest, SpecificOrderResponseDto.class);
//         assertNotNull(orderResponse);
//         assertEquals(HttpStatus.OK, orderResponse.getStatusCode());
//         assertNotNull(orderResponse.getBody());
//         this.orderNumber = orderResponse.getBody().getOrderNumber();

//         // Act
//         String url = String.format("/specificGames/%d/addToOrder?orderId=%d", specificGameSerialNumber, orderNumber);
//         ResponseEntity<SpecificGameResponseDto[]> response = restTemplate.exchange(
//                 url,
//                 HttpMethod.PUT,
//                 null,
//                 SpecificGameResponseDto[].class
//         );

//         // Assert
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         SpecificGameResponseDto[] updatedSpecificGames = response.getBody();
//         assertNotNull(updatedSpecificGames);
//         assertTrue(updatedSpecificGames.length > 0);

//         // Verify that the specific game is associated with the order
//         boolean found = false;
//         for (SpecificGameResponseDto sg : updatedSpecificGames) {
//             if (sg.getSerialNumber() == specificGameSerialNumber) {
//                 found = true;
//                 break;
//             }
//         }
//         assertTrue(found, "Specific game should be associated with the order.");
//     }

//     // @Test
//     // @Order(7)
//     // public void testRemoveSpecificGameFromOrder() {
//     //     // Act
//     //     String url = String.format("/specificGames/%d/removeFromOrder?orderId=%d", specificGameSerialNumber, orderNumber);
//     //     ResponseEntity<SpecificGameResponseDto[]> response = restTemplate.exchange(
//     //             url,
//     //             HttpMethod.PUT,
//     //             null,
//     //             SpecificGameResponseDto[].class
//     //     );

//     //     // Assert
//     //     assertNotNull(response);
//     //     assertEquals(HttpStatus.OK, response.getStatusCode());
//     //     SpecificGameResponseDto[] updatedSpecificGames = response.getBody();
//     //     assertNotNull(updatedSpecificGames);

//     //     // Verify that the specific game is no longer associated with the order
//     //     boolean found = false;
//     //     for (SpecificGameResponseDto sg : updatedSpecificGames) {
//     //         if (sg.getSerialNumber() == specificGameSerialNumber) {
//     //             found = true;
//     //             break;
//     //         }
//     //     }
//     //     assertFalse(found, "Specific game should not be associated with the order after removal.");
//     // }

//     @Test
//     @Order(8)
//     public void testDeleteSpecificGame() {
//         // Act
//         restTemplate.delete("/specificGames/" + specificGameSerialNumber);

//         // Verify deletion
//         ResponseEntity<String> response = restTemplate.getForEntity(
//                 "/specificGames/" + specificGameSerialNumber,
//                 String.class
//         );

//         // Assert
//         assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//     }
// }
