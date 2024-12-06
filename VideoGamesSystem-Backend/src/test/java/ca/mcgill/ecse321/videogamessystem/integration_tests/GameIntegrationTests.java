
// Tests for Game are not updated yet.
// package ca.mcgill.ecse321.videogamessystem.integration_tests;

// import static org.junit.jupiter.api.Assertions.*;

// import java.sql.Date;
// import java.time.LocalDate;

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

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import org.springframework.boot.test.web.client.TestRestTemplate;

// import ca.mcgill.ecse321.videogamessystem.VideogamessystemApplication;
// import ca.mcgill.ecse321.videogamessystem.dto.GameDto.GameRequestDto;
// import ca.mcgill.ecse321.videogamessystem.dto.GameDto.GameResponseDto;
// import ca.mcgill.ecse321.videogamessystem.dto.PromotionDto.PromotionRequestDto;
// import ca.mcgill.ecse321.videogamessystem.dto.PromotionDto.PromotionResponseDto;
// import ca.mcgill.ecse321.videogamessystem.dto.WishlistDto.WishlistRequestDto;
// import ca.mcgill.ecse321.videogamessystem.dto.WishlistDto.WishlistResponseDto;
// import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerRequestDto;
// import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerResponseDto;
// import ca.mcgill.ecse321.videogamessystem.model.Game.Category;
// import ca.mcgill.ecse321.videogamessystem.model.Game.ConsoleType;
// import ca.mcgill.ecse321.videogamessystem.repository.GameRepository;
// import ca.mcgill.ecse321.videogamessystem.repository.PromotionRepository;
// import ca.mcgill.ecse321.videogamessystem.repository.WishlistRepository;
// @SpringBootTest(classes = VideogamessystemApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
// @TestMethodOrder(OrderAnnotation.class)
// @TestInstance(Lifecycle.PER_CLASS)
// public class GameIntegrationTests {

//     @Autowired
//     private TestRestTemplate restTemplate;

//     @Autowired
//     private GameRepository gameRepository;

//     @Autowired
//     private PromotionRepository promotionRepository;

//     @Autowired
//     private WishlistRepository wishlistRepository;

//     private static final String VALID_TITLE = "Test Game";
//     private static final String VALID_DESCRIPTION = "Test game description.";
//     private static final int VALID_PRICE = 50;
//     private static final Category VALID_CATEGORY = Category.Adventure;
//     private static final ConsoleType VALID_CONSOLE_TYPE = ConsoleType.PS4;

//     private Long gameId;
//     private Long promotionId;
//     private Long wishlistId;
//     private Long customerId;

//     @AfterAll
//     public void clearDatabase() {
//         gameRepository.deleteAll();
//         promotionRepository.deleteAll();
//         wishlistRepository.deleteAll();
//     }

    // @Test
    // @Order(1)
    // public void testCreateGame() {
    //     // Arrange
    //     GameRequestDto request = new GameRequestDto(VALID_DESCRIPTION, VALID_PRICE, VALID_TITLE, VALID_CATEGORY, VALID_CONSOLE_TYPE);

    //     // Act
    //     ResponseEntity<GameResponseDto> response = restTemplate.postForEntity("/games", request, GameResponseDto.class);

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());
    //     assertTrue(response.getBody().getId() > 0, "The ID should be positive.");
    //     this.gameId = response.getBody().getId();
    //     assertEquals(VALID_TITLE, response.getBody().getTitle());
    //     assertEquals(VALID_DESCRIPTION, response.getBody().getDescription());
    //     assertEquals(VALID_PRICE, response.getBody().getPrice());
    //     assertEquals(VALID_CATEGORY, response.getBody().getCategory());
    //     assertEquals(VALID_CONSOLE_TYPE, response.getBody().getConsoleType());
    // }

    // @Test
    // @Order(2)
    // public void testCreateGameWithSpecificGames() {
    //     // Arrange
    //     int stockQuantity = 3;
    //     GameRequestDto request = new GameRequestDto(
    //         VALID_DESCRIPTION,
    //         VALID_PRICE,
    //         VALID_TITLE,
    //         VALID_CATEGORY,
    //         VALID_CONSOLE_TYPE
    //     );
    //     request.setStockQuantity(stockQuantity);
    
    //     // Act
    //     ResponseEntity<GameResponseDto> response = restTemplate.postForEntity("/games", request, GameResponseDto.class);
    
    //     // Assert game creation
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());
    //     Long gameId = response.getBody().getId();
    
    //     // Verify basic game properties
    //     assertEquals(VALID_TITLE, response.getBody().getTitle());
    //     assertEquals(VALID_PRICE, response.getBody().getPrice());
    //     assertEquals(VALID_CATEGORY, response.getBody().getCategory());
    //     assertEquals(VALID_CONSOLE_TYPE, response.getBody().getConsoleType());
    
    //     // Get game by ID to verify it exists
    //     String gameUrl = String.format("/games/%d", gameId);
    //     ResponseEntity<GameResponseDto> getResponse = restTemplate.getForEntity(gameUrl, GameResponseDto.class);
    //     assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    //     assertNotNull(getResponse.getBody());
    
    //     // If you have a specific endpoint for specific games, use that instead
    //     // For example, if you have an endpoint like /games/{id}/specific-games
    //     String specificGamesUrl = String.format("/games/%d/specific-games", gameId);
    //     ResponseEntity<Object[]> specificGamesResponse = restTemplate.getForEntity(
    //         specificGamesUrl, 
    //         Object[].class
    //     );
        
    //     assertNotNull(specificGamesResponse.getBody());
    //     assertEquals(stockQuantity, specificGamesResponse.getBody().length);
    // }

    // @Test
    // @Order(3)
    // public void testGetGameById() {
    //     // Arrange
    //     String url = String.format("/games/%d", this.gameId);

    //     // Act
    //     ResponseEntity<GameResponseDto> response = restTemplate.getForEntity(url, GameResponseDto.class);

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());
    //     assertEquals(this.gameId, response.getBody().getId());
    //     assertEquals(VALID_TITLE, response.getBody().getTitle());
    //     assertEquals(VALID_DESCRIPTION, response.getBody().getDescription());
    //     assertEquals(VALID_PRICE, response.getBody().getPrice());
    // }

    // @Test
    // @Order(4)
    // public void testGetAllGames() {
    //     // Act
    //     ResponseEntity<GameResponseDto[]> response = restTemplate.getForEntity("/games", GameResponseDto[].class);

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());
    //     assertTrue(response.getBody().length > 0, "The list of games should not be empty.");
    // }

    // @Test
    // @Order(5)
    // public void testGetStockQuantity() {
    //     // Arrange
    //     String url = String.format("/games/%d/stock", this.gameId);

    //     // Act
    //     ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());

    //     // Since we haven't added any SpecificGame entities, the stock should be 0.
    //     int stock = response.getBody();
    //     assertEquals(0, stock, "Stock quantity should be 0.");
    // }

    // @Test
    // @Order(6)
    // public void testDeleteGame() {
    //     // Arrange
    //     String url = String.format("/games/%d", this.gameId);

    //     // Act
    //     ResponseEntity<GameResponseDto> response = restTemplate.exchange(url, HttpMethod.DELETE, null, GameResponseDto.class);

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());
    //     assertEquals(this.gameId, response.getBody().getId());

    //     // Verify that the game is actually deleted
    //     ResponseEntity<String> getResponse = restTemplate.getForEntity(url, String.class);
    //     assertEquals(HttpStatus.CONFLICT, getResponse.getStatusCode());
    // }

    // @Test
    // @Order(7)
    // public void testGetGamesByTitle() {
    //     // Arrange
    //     String titleToTest = "Same Title Game";

    //     // Create games with the same title
    //     GameRequestDto request1 = new GameRequestDto("Description 1", 60, titleToTest, Category.Action, ConsoleType.XBOX);
    //     GameRequestDto request2 = new GameRequestDto("Description 2", 70, titleToTest, Category.Action, ConsoleType.PS4);
    //     GameRequestDto request3 = new GameRequestDto("Description 3", 80, "Different Title", Category.Adventure, ConsoleType.PC);

    //     restTemplate.postForEntity("/games", request1, GameResponseDto.class);
    //     restTemplate.postForEntity("/games", request2, GameResponseDto.class);
    //     restTemplate.postForEntity("/games", request3, GameResponseDto.class);

    //     String url = String.format("/games/title/%s", titleToTest);

    //     // Act
    //     ResponseEntity<GameResponseDto[]> response = restTemplate.getForEntity(url, GameResponseDto[].class);

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());

    //     GameResponseDto[] games = response.getBody();
    //     assertEquals(2, games.length, "There should be 2 games with the title.");

    //     for (GameResponseDto game : games) {
    //         assertEquals(titleToTest, game.getTitle());
    //     }
    // }

    // @Test
    // @Order(8)
    // public void testGetGamesByCategory() {
    //     // Arrange
    //     Category categoryToTest = Category.Action;

    //     String url = String.format("/games/category/%s", categoryToTest);

    //     // Act
    //     ResponseEntity<GameResponseDto[]> response = restTemplate.getForEntity(url, GameResponseDto[].class);

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());

    //     GameResponseDto[] games = response.getBody();
    //     assertTrue(games.length > 0, "There should be at least one game in the category.");

    //     for (GameResponseDto game : games) {
    //         assertEquals(categoryToTest, game.getCategory());
    //     }
    // }

    // @Test
    // @Order(9)
    // public void testGetGamesByConsoleType() {
    //     // Arrange
    //     ConsoleType consoleTypeToTest = ConsoleType.PS4;

    //     String url = String.format("/games/console/%s", consoleTypeToTest);

    //     // Act
    //     ResponseEntity<GameResponseDto[]> response = restTemplate.getForEntity(url, GameResponseDto[].class);

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());

    //     GameResponseDto[] games = response.getBody();
    //     assertTrue(games.length > 0, "There should be at least one game for the console type.");

    //     for (GameResponseDto game : games) {
    //         assertEquals(consoleTypeToTest, game.getConsoleType());
    //     }
    // }

    // @Test
    // @Order(10)
    // public void testGetGamesBetweenPrices() {
    //     // Arrange
    //     int minPrice = 60;
    //     int maxPrice = 80;

    //     String url = String.format("/games/price?min=%d&max=%d", minPrice, maxPrice);

    //     // Act
    //     ResponseEntity<GameResponseDto[]> response = restTemplate.getForEntity(url, GameResponseDto[].class);

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertNotNull(response.getBody());

    //     GameResponseDto[] games = response.getBody();
    //     assertTrue(games.length > 0, "There should be at least one game in the price range.");

    //     for (GameResponseDto game : games) {
    //         assertTrue(game.getPrice() >= minPrice && game.getPrice() <= maxPrice, "Game price should be within the specified range.");
    //     }
    // }

    // @Test
    // @Order(10)
    // public void testAddPromotionToGame() {
    //     // Arrange: Create game
    //     GameRequestDto gameRequest = new GameRequestDto(
    //         "A test game", 50, "Test Title", Category.Action, ConsoleType.PS4
    //     );
    //     ResponseEntity<GameResponseDto> gameResponse = restTemplate.postForEntity(
    //         "/games", gameRequest, GameResponseDto.class
    //     );

    //     // Assert: Game creation
    //     assertNotNull(gameResponse);
    //     assertEquals(HttpStatus.OK, gameResponse.getStatusCode());
    //     assertNotNull(gameResponse.getBody());
    //     Long gameId = gameResponse.getBody().getId();
    //     assertNotNull(gameId, "Game ID should not be null");

    //     // Arrange: Create promotion
    //     PromotionRequestDto promoRequest = new PromotionRequestDto(
    //         20, Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now().plusDays(5))
    //     );
    //     ResponseEntity<PromotionResponseDto> promoResponse = restTemplate.postForEntity(
    //         "/promotions", promoRequest, PromotionResponseDto.class
    //     );

    //     // Assert: Promotion creation
    //     assertNotNull(promoResponse);
    //     assertEquals(HttpStatus.OK, promoResponse.getStatusCode());
    //     assertNotNull(promoResponse.getBody());
    //     Long promoId = promoResponse.getBody().getId();
    //     assertNotNull(promoId, "Promotion ID should not be null");

    //     // Act: Associate promotion with game
    //     String url = String.format("/games/%d/promotion/%d", gameId, promoId);
    //     ResponseEntity<GameResponseDto> updatedGameResponse = restTemplate.exchange(
    //         url, HttpMethod.PUT, null, GameResponseDto.class
    //     );

    //     // Assert: Verify association
    //     assertNotNull(updatedGameResponse);
    //     assertEquals(HttpStatus.OK, updatedGameResponse.getStatusCode());

    //     GameResponseDto updatedGame = updatedGameResponse.getBody();
    //     assertNotNull(updatedGame, "Updated game should not be null");
    //     assertNotNull(updatedGame.getPromotion(), "Game should have a promotion associated");
    //     assertEquals(promoId, updatedGame.getPromotion().getId(), "Promotion ID should match the associated promotion");

    //     // Additional Assertions (Optional)
    //     assertEquals(gameRequest.getTitle(), updatedGame.getTitle(), "Game title should remain unchanged");
    //     assertEquals(gameRequest.getPrice(), updatedGame.getPrice(), "Game price should remain unchanged");
    // }



    // @Test
    // @Order(11)
    // public void testGetPriceAfterPromo() throws JsonProcessingException {
    //     // Arrange
    //     String url = String.format("/games/%d/price-after-promo", this.gameId);

    //     // Act
    //     ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     String responseBody = response.getBody();
    //     assertNotNull(responseBody);

    //     // Parse the response body to get the integer value
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     Integer priceAfterPromo = objectMapper.readValue(responseBody, Integer.class);

    //     // Calculate expected price after applying promotion
    //     int expectedPrice = VALID_PRICE - (VALID_PRICE * 20 / 100);
    //     assertEquals(expectedPrice, priceAfterPromo.intValue(), "Price after promotion should be correctly calculated.");
    // }

    
//     @Test
//     @Order(12)
//     public void testAddGameToWishlist() {
//         // Arrange
//         // Create a customer
//         CustomerRequestDto customerRequest = new CustomerRequestDto("test@example.com", "password", "Test User");
//         ResponseEntity<CustomerResponseDto> customerResponse = restTemplate.postForEntity("/customers", customerRequest, CustomerResponseDto.class);
//         assertNotNull(customerResponse);
//         assertEquals(HttpStatus.OK, customerResponse.getStatusCode());
//         assertNotNull(customerResponse.getBody());
//         this.customerId = customerResponse.getBody().getId();
//         assertNotNull(this.customerId);

//         // Create a wishlist for the customer
//         WishlistRequestDto wishlistRequest = new WishlistRequestDto(this.customerId);
//         ResponseEntity<WishlistResponseDto> wishlistResponse = restTemplate.postForEntity("/wishlists", wishlistRequest, WishlistResponseDto.class);

//         assertNotNull(wishlistResponse);
//         assertEquals(HttpStatus.OK, wishlistResponse.getStatusCode());
//         assertNotNull(wishlistResponse.getBody());
//         this.wishlistId = wishlistResponse.getBody().getId();
//         assertNotNull(this.wishlistId);

//         // Act
//         String url = String.format("/games/%d/wishlist/%d", this.gameId, this.wishlistId);
//         ResponseEntity<GameResponseDto> response = restTemplate.exchange(url, HttpMethod.PUT, null, GameResponseDto.class);

//         // Assert
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotNull(response.getBody());

//         // Since GameResponseDto doesn't include wishlist details, fetch the game again
//         ResponseEntity<GameResponseDto> gameResponse = restTemplate.getForEntity(String.format("/games/%d", this.gameId), GameResponseDto.class);
//         assertNotNull(gameResponse);
//         assertEquals(HttpStatus.OK, gameResponse.getStatusCode());
//         GameResponseDto game = gameResponse.getBody();
//         assertNotNull(game);
//         // Verify wishlist (you may need to adjust GameResponseDto to include wishlist details)
//         // assertEquals(this.wishlistId, game.getWishlist().getId());
//     }
// }
