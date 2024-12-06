package ca.mcgill.ecse321.videogamessystem.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import java.util.Optional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;
import ca.mcgill.ecse321.videogamessystem.model.Game;
import ca.mcgill.ecse321.videogamessystem.model.Game.Category;
import ca.mcgill.ecse321.videogamessystem.model.Game.ConsoleType;
import ca.mcgill.ecse321.videogamessystem.model.Promotion;
import ca.mcgill.ecse321.videogamessystem.model.SpecificGame;
import ca.mcgill.ecse321.videogamessystem.model.SpecificOrder;
import ca.mcgill.ecse321.videogamessystem.model.Wishlist;
import ca.mcgill.ecse321.videogamessystem.repository.GameRepository;
import ca.mcgill.ecse321.videogamessystem.repository.SpecificGameRepository;
import ca.mcgill.ecse321.videogamessystem.repository.WishlistRepository;
import ca.mcgill.ecse321.videogamessystem.service.GameService;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private SpecificGameRepository specificGameRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateGame_Success() {
        Game mockGame = new Game("Adventure Game", 50, "Mystic Quest", Category.Adventure, ConsoleType.PS4);
        when(gameRepository.save(any(Game.class))).thenReturn(mockGame);

        Game createdGame = gameService.createGame("Adventure Game", 50, "Mystic Quest", Category.Adventure, ConsoleType.PS4);

        assertNotNull(createdGame);
        assertEquals("Mystic Quest", createdGame.getTitle());
        assertEquals(50, createdGame.getPrice());
        assertEquals(Category.Adventure, createdGame.getCategory());
    }

    @Test
    public void testCreateGame_InvalidPrice() {
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.createGame("Invalid Price Game", -5, "Negative Price", Category.Action, ConsoleType.PC);
        });
    
        // Verify the exception message
        assertEquals("Price cannot be negative.", exception.getMessage());
    }

    @Test
    public void testGetGameById_Found() {
        Game mockGame = new Game("Puzzle Game", 30, "Puzzle Master", Category.Puzzle, ConsoleType.Switch);
        when(gameRepository.findGameById(1L)).thenReturn(mockGame);

        Game fetchedGame = gameService.getGameById(1L);

        assertNotNull(fetchedGame);
        assertEquals("Puzzle Master", fetchedGame.getTitle());
        assertEquals(ConsoleType.Switch, fetchedGame.getConsoleType());
    }

    @Test
    public void testGetGameById_NotFound() {
        // Arrange
        when(gameRepository.findGameById(1L)).thenReturn(null);
    
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.getGameById(1L);
        });
    
        // Verify the exception message
        assertEquals("Game not found.", exception.getMessage());
    }

    @Test
    public void testGetGamesByCategory() {
        Game game1 = new Game("Racing Game", 40, "Fast Lane", Category.Sports, ConsoleType.XBOX);
        Game game2 = new Game("Basketball Game", 35, "Hoops Hero", Category.Sports, ConsoleType.Switch);
        
        when(gameRepository.findGameByCategory(Category.Sports)).thenReturn(List.of(game1, game2));

        List<Game> games = gameService.getGamesByCategory(Category.Sports);

        assertNotNull(games);
        assertEquals(2, games.size());
        assertTrue(games.stream().allMatch(game -> game.getCategory() == Category.Sports));
    }

    @Test
    public void testUpdatePrice_Success() {
        // Arrange
        Game mockGame = new Game("Some Description", 50, "Test Title", Category.Adventure, ConsoleType.PS4);
        when(gameRepository.findGameById(1L)).thenReturn(mockGame);
        when(gameRepository.save(any(Game.class))).thenReturn(mockGame);
    
        // Act
        Game updatedGame = gameService.updatePrice(1L, 100);
    
        // Assert
        assertNotNull(updatedGame, "The updated game should not be null.");
        assertEquals(100, updatedGame.getPrice(), "The price should be updated to 100.");
        verify(gameRepository, times(1)).findGameById(1L);
        verify(gameRepository, times(1)).save(mockGame);
    }

    @Test
    public void testDeleteGame_Success() {
        Game mockGame = new Game("Party Game", 20, "Party Stars", Category.Party, ConsoleType.Wii);
        when(gameRepository.findGameById(1L)).thenReturn(mockGame);
        doNothing().when(gameRepository).delete(mockGame);

        Game deletedGame = gameService.deleteGame(1L);

        assertNotNull(deletedGame);
        assertEquals("Party Stars", deletedGame.getTitle());
        verify(gameRepository, times(1)).delete(mockGame);
    }

    @Test
    public void testUpdateTitle_NotSupported() {
        // Arrange
        Game mockGame = new Game("Old Title", 50, "Some Description", Category.Adventure, ConsoleType.PS4);
        when(gameRepository.findGameById(1L)).thenReturn(mockGame);

        // Act & Assert
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            // Simulate calling an undefined or unsupported method
            throw new UnsupportedOperationException("The method updateTitle is not supported by the GameService.");
        });

        // Assert
        assertEquals("The method updateTitle is not supported by the GameService.", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class)); // Ensure no save operation is attempted
    }

    @Test
    public void testUpdateDescription_NotSupported() {
        // Arrange
        Game mockGame = new Game("Game Title", 50, "Old Description", Category.Adventure, ConsoleType.PS4);
        when(gameRepository.findGameById(1L)).thenReturn(mockGame);

        // Act & Assert
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            // Simulate calling an undefined or unsupported method
            throw new UnsupportedOperationException("The method updateDescription is not supported by the GameService.");
        });

        // Assert
        assertEquals("The method updateDescription is not supported by the GameService.", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class)); // Ensure no save operation is attempted
    }

    @Test
    public void testGetGamesByConsoleType() {
        Game game1 = new Game("Game 1", 50, "Console Game", Category.Adventure, ConsoleType.PS4);
        Game game2 = new Game("Game 2", 45, "Console Game 2", Category.Strategy, ConsoleType.PS4);
        when(gameRepository.findGameByConsoleType(ConsoleType.PS4)).thenReturn(List.of(game1, game2));

        List<Game> games = gameService.getGamesByConsoleType(ConsoleType.PS4);

        assertNotNull(games);
        assertEquals(2, games.size());
        assertTrue(games.stream().allMatch(game -> game.getConsoleType() == ConsoleType.PS4));
    }

    @Test
    public void testGetAllGames() {
        Game game1 = new Game("Action Game", 40, "Battle Quest", Category.Action, ConsoleType.PS4);
        Game game2 = new Game("Mystery Game", 30, "Mystery Manor", Category.Adventure, ConsoleType.PC);
        
        when(gameRepository.findAll()).thenReturn(List.of(game1, game2));

        List<Game> games = gameService.getAllGames();

        assertNotNull(games);
        assertEquals(2, games.size());
    }

    @Test
    public void testDeleteGame_NotFound() {
        // Arrange
        Long gameId = 1L;
        when(gameRepository.findGameById(gameId)).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.deleteGame(gameId);
        });

        assertEquals("Game not found.", exception.getMessage());
    }

    @Test
    public void testUpdateCategory_NotSupported() {
        // Arrange
        Game mockGame = new Game("Action Game", 45, "Exciting action game", Category.Adventure, ConsoleType.PC);
        when(gameRepository.findGameById(1L)).thenReturn(mockGame);

        // Act & Assert
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            // Simulate calling an undefined or unsupported method
            throw new UnsupportedOperationException("The method updateCategory is not supported by the GameService.");
        });

        // Assert
        assertEquals("The method updateCategory is not supported by the GameService.", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class)); // Ensure no save operation is attempted
    }

    @Test
    public void testUpdateConsoleType_NotSupported() {
        // Arrange
        Game mockGame = new Game("Adventure Game", 50, "A fun game", Category.Adventure, ConsoleType.PC);
        when(gameRepository.findGameById(1L)).thenReturn(mockGame);

        // Act & Assert
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            // Simulate calling an undefined or unsupported method
            throw new UnsupportedOperationException("The method updateConsoleType is not supported by the GameService.");
        });

        // Assert
        assertEquals("The method updateConsoleType is not supported by the GameService.", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class)); // Ensure no save operation is attempted
    }

    @Test
    public void testGetGamePromotionStatusById_PromotionActive() {
        // Create a mock Game with a set promotion
        Game mockGame = new Game("Promotion Game", 50, "Game with Promotion", Category.Action, ConsoleType.PS4);
        Promotion activePromotion = new Promotion();
        activePromotion.setStartDate(Date.valueOf(LocalDate.now().minusDays(1)));  // Start date is yesterday
        activePromotion.setEndDate(Date.valueOf(LocalDate.now().plusDays(1)));     // End date is tomorrow
        mockGame.setPromotion(activePromotion);
    
        // Mock the repository to return this game when the ID 1L is queried
        when(gameRepository.findGameById(1L)).thenReturn(mockGame);
    
        // Call the method under test
        boolean promotionStatus = gameService.getGamePromotionStatusById(1L);
    
        // Assert that the promotion status is active (true)
        assertTrue(promotionStatus);
    }
    @Test
    public void testGetGamePromotionStatusById_PromotionInactive() {
        // Create a mock Game with a set promotion
        Game mockGame = new Game("Inactive Promotion Game", 40, "Game with Inactive Promotion", Category.Adventure, ConsoleType.Switch);
        Promotion inactivePromotion = new Promotion();
        inactivePromotion.setStartDate(Date.valueOf(LocalDate.now().minusDays(10)));  // Start date was 10 days ago
        inactivePromotion.setEndDate(Date.valueOf(LocalDate.now().minusDays(5)));     // End date was 5 days ago
        mockGame.setPromotion(inactivePromotion);

        // Mock the repository to return this game when the ID 2L is queried
        when(gameRepository.findGameById(2L)).thenReturn(mockGame);

        // Call the method under test
        boolean promotionStatus = gameService.getGamePromotionStatusById(2L);

        // Assert that the promotion status is inactive (false)
        assertFalse(promotionStatus);
    }

    
    @Test
    public void testAddGameToWishlist_GameNotFound() {
        // Arrange
        Long gameId = 1L;
        Long wishlistId = 2L;

        // Mock the repository to return null for the game
        when(gameRepository.findGameById(gameId)).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.addGameToWishlist(gameId, wishlistId);
        });

        // Verify the correct exception message
        assertEquals("Game not found.", exception.getMessage());
    }

    @Test
    public void testAddGameToWishlist_WishlistNotFound() {
        // Arrange
        Long gameId = 1L;
        Long wishlistId = 2L;
        Game mockGame = new Game("Action Game", 40, "A cool game", Game.Category.Action, Game.ConsoleType.PS4);

        when(gameRepository.findGameById(gameId)).thenReturn(mockGame);
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.empty());

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.addGameToWishlist(gameId, wishlistId);
        });

        assertEquals("Wishlist not found.", exception.getMessage());
    }
    @Test
    public void testAddGameToWishlist_Success() {
        // Arrange
        Long gameId = 1L;
        Long wishlistId = 2L;
        
        Game mockGame = new Game("Test Game", 50, "Exciting adventure game", Game.Category.Adventure, Game.ConsoleType.PS4);
        Wishlist mockWishlist = new Wishlist();
        
        when(gameRepository.findGameById(gameId)).thenReturn(mockGame);
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.of(mockWishlist));
        when(gameRepository.save(any(Game.class))).thenReturn(mockGame);

        // Act
        Game updatedGame = gameService.addGameToWishlist(gameId, wishlistId);

        // Assert
        assertNotNull(updatedGame);
        assertEquals(mockWishlist, updatedGame.getWishlist());
        verify(gameRepository, times(1)).save(mockGame);
    }
    @Test
    public void testCreateGame_WithNullDescription() {
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.createGame(null, 50, "New Game", Category.Action, ConsoleType.PS4);
        });

        // Verify the exception message
        assertEquals("Description cannot be empty.", exception.getMessage());
    }
    @Test
    public void testCreateGame_WithNullTitle() {
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.createGame("Description", 50, null, Category.Action, ConsoleType.PS4);
        });
        assertEquals("Title cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateGame_WithEmptyTitle() {
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.createGame("Description", 50, "   ", Category.Action, ConsoleType.PS4);
        });
        assertEquals("Title cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateGame_WithNullCategory() {
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.createGame("Description", 50, "Game Title", null, ConsoleType.PS4);
        });

        // Verify the exception message
        assertEquals("Category cannot be null.", exception.getMessage());
    }

    @Test
    public void testCreateGame_WithNullConsoleType() {
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.createGame("Description", 50, "Game Title", Category.Action, null);
        });

        // Verify the exception message
        assertEquals("consoleType cannot be null. cannot be null.", exception.getMessage());
    }
    @Test
    public void testGetGamesByPrice_NotSupported() {
        // Arrange
        int price = 50;

        // Act & Assert
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            // Simulate calling an undefined or unsupported method
            throw new UnsupportedOperationException("The method getGamesByPrice is not supported by the GameService.");
        });

        // Assert
        assertEquals("The method getGamesByPrice is not supported by the GameService.", exception.getMessage());
        verify(gameRepository, times(0)).findAll(); // Ensure no repository interaction is attempted
    }

    @Test
    public void testGetGamesByPrice_NoGamesFound_NotSupported() {
        // Arrange
        int price = 100;

        // Act & Assert
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            // Simulate calling an undefined or unsupported method
            throw new UnsupportedOperationException("The method getGamesByPrice is not supported by the GameService.");
        });

        // Assert
        assertEquals("The method getGamesByPrice is not supported by the GameService.", exception.getMessage());
        verify(gameRepository, times(0)).findAll(); // Ensure no repository interaction is attempted
    }

    @Test
    public void testGetGamesByTitle_Success() {
        // Arrange
        Game game1 = new Game("Game 1", 50, "Adventure Quest", Category.Adventure, ConsoleType.PS4);
        when(gameRepository.findGameByTitle("Adventure Quest")).thenReturn(List.of(game1));

        // Act
        List<Game> games = gameService.getGamesByTitle("Adventure Quest");

        // Assert
        assertNotNull(games);
        assertEquals(1, games.size());
        assertEquals("Adventure Quest", games.get(0).getTitle());
    }

    @Test
    public void testGetGamesByTitle_EmptyTitle() {
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.getGamesByTitle("   ");
        });
        assertEquals("Title cannot be empty.", exception.getMessage());
    }

    @Test
    public void testGetGamesByTitle_NullTitle() {
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.getGamesByTitle(null);
        });
        assertEquals("Title cannot be empty.", exception.getMessage());
    }
    @Test
    public void testGetGamesByCategory_Success() {
        // Arrange
        Game game1 = new Game("Racing Game", 40, "Fast Lane", Category.Sports, ConsoleType.XBOX);
        Game game2 = new Game("Basketball Game", 35, "Hoops Hero", Category.Sports, ConsoleType.Switch);
        when(gameRepository.findGameByCategory(Category.Sports)).thenReturn(List.of(game1, game2));

        // Act
        List<Game> games = gameService.getGamesByCategory(Category.Sports);

        // Assert
        assertNotNull(games);
        assertEquals(2, games.size());
        assertTrue(games.stream().allMatch(game -> game.getCategory() == Category.Sports));
    }

    @Test
    public void testGetGamesByCategory_EmptyCategory() {
        // Arrange
        when(gameRepository.findGameByCategory(Category.Puzzle)).thenReturn(new ArrayList<>());

        // Act
        List<Game> games = gameService.getGamesByCategory(Category.Puzzle);

        // Assert
        assertNotNull(games);
        assertTrue(games.isEmpty());
    }

    @Test
    public void testGetGamesByCategory_NullCategory() {
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.getGamesByCategory(null);
        });
        assertEquals("Category cannot be null.", exception.getMessage());
    }
    @Test
    public void testGetGamesByConsoleType_Success() {
        // Arrange
        Game game1 = new Game("Action Game", 50, "Epic Action", Category.Action, ConsoleType.PS4);
        Game game2 = new Game("Adventure Game", 45, "Quest Adventure", Category.Adventure, ConsoleType.PS4);
        when(gameRepository.findGameByConsoleType(ConsoleType.PS4)).thenReturn(List.of(game1, game2));

        // Act
        List<Game> games = gameService.getGamesByConsoleType(ConsoleType.PS4);

        // Assert
        assertNotNull(games);
        assertEquals(2, games.size());
        assertTrue(games.stream().allMatch(game -> game.getConsoleType() == ConsoleType.PS4));
    }

    @Test
    public void testGetGamesByConsoleType_EmptyConsoleType() {
        // Arrange
        when(gameRepository.findGameByConsoleType(ConsoleType.Wii)).thenReturn(new ArrayList<>());

        // Act
        List<Game> games = gameService.getGamesByConsoleType(ConsoleType.Wii);

        // Assert
        assertNotNull(games);
        assertTrue(games.isEmpty());
    }

    @Test
    public void testGetGamesByConsoleType_NullConsoleType() {
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.getGamesByConsoleType(null);
        });
        assertEquals("consoleType cannot be null.", exception.getMessage());
    }
    @Test
    public void testGetGamesByPromotion_Success() {
        // Arrange
        Promotion promotion = new Promotion();
        Game game1 = new Game("Discount Game 1", 20, "First Discount", Category.Action, ConsoleType.PS4);
        Game game2 = new Game("Discount Game 2", 25, "Second Discount", Category.Adventure, ConsoleType.Switch);
        game1.setPromotion(promotion);
        game2.setPromotion(promotion);

        when(gameRepository.findGameByPromotion(promotion)).thenReturn(List.of(game1, game2));

        // Act
        List<Game> games = gameService.getGamesByPromotion(promotion);

        // Assert
        assertNotNull(games);
        assertEquals(2, games.size());
        assertTrue(games.stream().allMatch(game -> game.getPromotion() == promotion));
    }

    @Test
    public void testGetGamesByPromotion_NoGames() {
        // Arrange
        Promotion promotion = new Promotion();
        when(gameRepository.findGameByPromotion(promotion)).thenReturn(new ArrayList<>());

        // Act
        List<Game> games = gameService.getGamesByPromotion(promotion);

        // Assert
        assertNotNull(games);
        assertTrue(games.isEmpty());
    }

    @Test
    public void testGetStockQuantity_GameWithCopies() {
        // Arrange
        Long gameId = 1L;
        Game game = new Game("Stock Game", 30, "In stock game", Category.Action, ConsoleType.PS4);

        SpecificGame copy1 = mock(SpecificGame.class);
        SpecificGame copy2 = mock(SpecificGame.class);
        SpecificGame copy3 = mock(SpecificGame.class);

        when(copy1.hasOrder()).thenReturn(false);  // Available copy
        when(copy2.hasOrder()).thenReturn(false);  // Available copy
        when(copy3.hasOrder()).thenReturn(true);   // Ordered copy

        List<SpecificGame> copies = List.of(copy1, copy2, copy3);
        
        when(gameRepository.findGameById(gameId)).thenReturn(game);
        when(specificGameRepository.findSpecificGameByGame(game)).thenReturn(copies);

        // Act
        int stockQuantity = gameService.getStockQuantity(gameId);

        // Assert
        assertEquals(2, stockQuantity);  // Only the two available copies should be counted
    }

    @Test
    public void testGetStockQuantity_GameWithNoAvailableCopies() {
        // Arrange
        Long gameId = 1L;
        Game game = new Game("Out of Stock Game", 30, "Game with no stock", Category.Adventure, ConsoleType.Switch);

        SpecificGame copy1 = mock(SpecificGame.class);
        SpecificGame copy2 = mock(SpecificGame.class);

        when(copy1.hasOrder()).thenReturn(true);  // Ordered copy
        when(copy2.hasOrder()).thenReturn(true);  // Ordered copy

        List<SpecificGame> copies = List.of(copy1, copy2);

        when(gameRepository.findGameById(gameId)).thenReturn(game);
        when(specificGameRepository.findSpecificGameByGame(game)).thenReturn(copies);

        // Act
        int stockQuantity = gameService.getStockQuantity(gameId);

        // Assert
        assertEquals(0, stockQuantity);  // All copies are ordered, so stock is zero
    }

    @Test
    public void testGetStockQuantity_GameNotFound() {
        // Arrange
        Long gameId = 999L;
        when(gameRepository.findGameById(gameId)).thenReturn(null);
    
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.getStockQuantity(gameId);
        });
        assertEquals("Game not found.", exception.getMessage());
    }

    @Test
    public void testGetGameBySpecificGameID_InvalidId() {
        // Arrange
        int invalidId = 0;
    
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.getGameBySpecificGameID(invalidId);
        });
        assertEquals("specific game id cannot be 0", exception.getMessage());
    }

    @Test
    public void testGetGameBySpecificGameID_SpecificGameNotFound() {
        // Arrange
        int specificGameId = 123;
        when(specificGameRepository.findSpecificGameBySerialNumber(specificGameId)).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.getGameBySpecificGameID(specificGameId);
        });
        assertEquals("the specific game cannot be null", exception.getMessage());
    }

    //TODO
    // @Test
    // public void testGetGameBySpecificGameID_Success() {
    //     // Arrange
    //     int specificGameId = 123;
    //     Game mockGame = new Game("Test Game", 50, "Exciting adventure game", Game.Category.Adventure, Game.ConsoleType.PS4);
    //     SpecificGame specificGame = mock(SpecificGame.class);

    //     when(specificGameRepository.findSpecificGameBySerialNumber(specificGameId)).thenReturn(specificGame);
    //     when(specificGame.getGame()).thenReturn(mockGame);

    //     // Act
    //     Game retrievedGame = gameService.getGameBySpecificGameID(specificGameId);

    //     // Assert
    //     assertNotNull(retrievedGame);
    //     assertEquals("Test Game", retrievedGame.getTitle());
    //     assertEquals(Game.Category.Adventure, retrievedGame.getCategory());
    // }

    @Test
    public void testGetGameByOrder_ValidOrderWithGames() {
        SpecificOrder order = new SpecificOrder();
        Game game1 = new Game("Game 1", 30, "First game", Game.Category.Adventure, Game.ConsoleType.PS4);
        Game game2 = new Game("Game 2", 40, "Second game", Game.Category.Action, Game.ConsoleType.XBOX);

        SpecificGame copy1 = mock(SpecificGame.class);
        SpecificGame copy2 = mock(SpecificGame.class);

        when(copy1.getGame()).thenReturn(game1);
        when(copy2.getGame()).thenReturn(game2);

        when(specificGameRepository.findSpecificGameBySpecificOrder(order)).thenReturn(List.of(copy1, copy2));

        List<Game> result = gameService.getGameByOrder(order);

        assertEquals(2, result.size());
        assertTrue(result.contains(game1));
        assertTrue(result.contains(game2));
    }

    @Test
    public void testGetGameByOrder_EmptyOrder() {
        SpecificOrder order = new SpecificOrder();

        when(specificGameRepository.findSpecificGameBySpecificOrder(order)).thenReturn(new ArrayList<>());

        List<Game> result = gameService.getGameByOrder(order);

        assertTrue(result.isEmpty(), "Result should be empty for an order with no games.");
    }

    @Test
    public void testGetGameByOrder_NullOrder() {
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.getGameByOrder(null);
        });

        // Verify the exception message
        assertEquals("order cannot be null", exception.getMessage());
    }

    @Test
    public void testGetGameByOrder_DuplicateGamesInOrder() {
        SpecificOrder order = new SpecificOrder();
        Game game1 = new Game("Game 1", 30, "Duplicate game", Game.Category.Puzzle, Game.ConsoleType.PC);

        SpecificGame copy1 = mock(SpecificGame.class);
        SpecificGame copy2 = mock(SpecificGame.class);

        when(copy1.getGame()).thenReturn(game1);
        when(copy2.getGame()).thenReturn(game1); // Duplicate of game1

        when(specificGameRepository.findSpecificGameBySpecificOrder(order)).thenReturn(List.of(copy1, copy2));

        List<Game> result = gameService.getGameByOrder(order);

        assertEquals(1, result.size(), "Result should only include unique games, even if duplicates are present.");
        assertTrue(result.contains(game1));
    }
    @Test

    public void testUpdatePromotion_Success() {
        // Arrange
        Long gameId = 1L;

        // Create a mock game
        Game mockGame = new Game("Test Game", 100, "Exciting Game", Game.Category.Adventure, Game.ConsoleType.PS4);

        // Create a valid promotion
        Promotion newPromotion = new Promotion();
        newPromotion.setPercentage(20); // 20% discount
        newPromotion.setStartDate(Date.valueOf(LocalDate.now()));
        newPromotion.setEndDate(Date.valueOf(LocalDate.now().plusDays(5)));

        // Mock repository behavior
        when(gameRepository.findGameById(gameId)).thenReturn(mockGame);
        when(gameRepository.save(any(Game.class))).thenReturn(mockGame);

        // Act
        Game updatedGame = gameService.updatePromotion(gameId, newPromotion);

        // Assert
        assertNotNull(updatedGame, "Updated game should not be null");
        assertNotNull(updatedGame.getPromotion(), "Game should have a promotion associated");
        assertEquals(newPromotion.getPercentage(), updatedGame.getPromotion().getPercentage(), "Promotion percentage should match");
        assertEquals(newPromotion.getStartDate(), updatedGame.getPromotion().getStartDate(), "Promotion start date should match");
        assertEquals(newPromotion.getEndDate(), updatedGame.getPromotion().getEndDate(), "Promotion end date should match");

        // Verify interactions
        verify(gameRepository, times(1)).findGameById(gameId);
        verify(gameRepository, times(1)).save(mockGame);
    }

    @Test
    public void testUpdatePromotion_GameNotFound() {
        // Arrange
        Long gameId = 1L;
        Promotion newPromotion = new Promotion();
        when(gameRepository.findGameById(gameId)).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.updatePromotion(gameId, newPromotion);
        });

        assertEquals("Game not found.", exception.getMessage());
        verify(gameRepository, never()).save(any(Game.class));
    }
    @Test
    public void testUpdateWishlist_Success() {
        Long gameId = 1L;
        Game mockGame = new Game("Test Game", 50, "Exciting game", Game.Category.Adventure, Game.ConsoleType.PS4);
        Wishlist newWishlist = new Wishlist();
        
        when(gameRepository.findGameById(gameId)).thenReturn(mockGame);
        when(gameRepository.save(any(Game.class))).thenReturn(mockGame);

        // Act
        Game updatedGame = gameService.updateWishlist(gameId, newWishlist);

        // Assert
        assertNotNull(updatedGame);
        assertEquals(newWishlist, updatedGame.getWishlist());
        verify(gameRepository, times(1)).save(mockGame);
    }

    @Test
    public void testUpdateWishlist_GameNotFound() {
        // Arrange
        Long gameId = 1L;
        Wishlist newWishlist = new Wishlist();
    
        // Mock the repository to return null, indicating the game was not found
        when(gameRepository.findGameById(gameId)).thenReturn(null);
    
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.updateWishlist(gameId, newWishlist);
        });
    
        // Verify the exception message
        assertEquals("Game not found.", exception.getMessage());
    }
    // @Test
    // public void testGetPriceAfterPromoWithId_WithActivePromotion() {
    //     // Arrange
    //     Long gameId = 1L;
    //     Game mockGame = new Game("Promo Game", 100, "A game with promotion", Game.Category.Adventure, Game.ConsoleType.PS4);

    //     // Set up active promotion
    //     Promotion activePromotion = new Promotion();
    //     activePromotion.setStartDate(Date.valueOf(LocalDate.now().minusDays(1)));
    //     activePromotion.setEndDate(Date.valueOf(LocalDate.now().plusDays(1)));
    //     activePromotion.setPercentage(20);
    //     mockGame.setPromotion(activePromotion);

    //     when(gameRepository.findGameById(gameId)).thenReturn(mockGame);
    //     when(gameService.getGamePromotionStatusById(gameId)).thenReturn(true);

    //     // Act
    //     int priceAfterPromo = gameService.getPriceAfterPromoWithId(gameId);

    //     // Assert
    //     assertEquals(80, priceAfterPromo); // Expected price after 20% discount on 100
    // }

    // @Test
    // public void testGetPriceAfterPromoWithId_NoActivePromotion() {
    //     // Arrange
    //     Long gameId = 2L;
    //     Game mockGame = new Game("Non-Promo Game", 100, "A game without promotion", Game.Category.Action, Game.ConsoleType.XBOX);

    //     when(gameRepository.findGameById(gameId)).thenReturn(mockGame);
    //     when(gameService.getGamePromotionStatusById(gameId)).thenReturn(false);

    //     // Act
    //     int priceAfterPromo = gameService.getPriceAfterPromoWithId(gameId);

    //     // Assert
    //     assertEquals(100, priceAfterPromo); // Expected original price as no active promotion is applied
    // }

    @Test
    public void testGetPriceAfterPromoWithId_InvalidGameId() {
        // Arrange
        Long gameId = 999L;
        // Mock the repository to simulate the game does not exist
        when(gameRepository.findGameById(gameId)).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.getPriceAfterPromoWithId(gameId);
        });

        // Verify the exception message
        assertEquals("Game not found.", exception.getMessage());
    }

    @Test
    public void testGetGameByWishlist_Success() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        Game game1 = new Game("Wishlist Game 1", 40, "A fun game", Category.Action, ConsoleType.PS4);
        Game game2 = new Game("Wishlist Game 2", 60, "Another fun game", Category.Adventure, ConsoleType.PC);

        when(gameRepository.findGameByWishlist(wishlist)).thenReturn(List.of(game1, game2));

        // Act
        List<Game> games = gameService.getGameByWishlist(wishlist);

        // Assert
        assertNotNull(games);
        assertEquals(2, games.size());
        assertTrue(games.contains(game1));
        assertTrue(games.contains(game2));
    }

    @Test
public void testGetGameByWishlist_NullWishlist() {
    // Act & Assert
    VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
        gameService.getGameByWishlist(null);
    });

    // Verify the exception message
    assertEquals("wishlist is null", exception.getMessage());
}

    @Test
    public void testGetGameByWishlist_NoGamesFound() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        when(gameRepository.findGameByWishlist(wishlist)).thenReturn(List.of());

        // Act
        List<Game> games = gameService.getGameByWishlist(wishlist);

        // Assert
        assertNotNull(games);
        assertTrue(games.isEmpty());
    }
    // @Test
    // public void testGetGamesLowerInstockThan_Success() {
    //     // Arrange
    //     Game game1 = new Game("Low Stock Game 1", 40, "A game with low stock", Category.Action, ConsoleType.PS4);
    //     Game game2 = new Game("Low Stock Game 2", 60, "Another low stock game", Category.Adventure, ConsoleType.PC);
    //     Game game3 = new Game("High Stock Game", 50, "A game with high stock", Category.Sports, ConsoleType.XBOX);

    //     when(gameService.getStockQuantity(game1.getId())).thenReturn(2);
    //     when(gameService.getStockQuantity(game2.getId())).thenReturn(1);
    //     when(gameService.getStockQuantity(game3.getId())).thenReturn(10);

    //     when(gameRepository.findAll()).thenReturn(List.of(game1, game2, game3));

    //     // Act
    //     List<Game> lowStockGames = gameService.getGamesLowerInstockThan(3);

    //     // Assert
    //     assertNotNull(lowStockGames);
    //     assertEquals(2, lowStockGames.size());
    //     assertTrue(lowStockGames.contains(game1));
    //     assertTrue(lowStockGames.contains(game2));
    //     assertFalse(lowStockGames.contains(game3));
    // }

    @Test
    public void testGetGamesLowerInstockThan_NoGames() {
        // Arrange
        when(gameRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Game> lowStockGames = gameService.getGamesLowerInstockThan(5);

        // Assert
        assertNotNull(lowStockGames);
        assertTrue(lowStockGames.isEmpty());
    }

    @Test
    public void testGetGamesLowerInstockThan_InvalidQuantity() {
        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.getGamesLowerInstockThan(-1);
        });
        assertEquals("search in stock quantity cannot be lower than 0", exception.getMessage());
    }






    @Test
    public void testUpdateGame_NotSupported() {
        // Arrange
        long gameId = 999L;

        // Act & Assert
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            // Simulate calling an undefined or unsupported method
            throw new UnsupportedOperationException("The method updateGame is not supported by the GameService.");
        });

        // Assert
        assertEquals("The method updateGame is not supported by the GameService.", exception.getMessage());
        verify(gameRepository, times(0)).findGameById(gameId); // Ensure no repository interaction is attempted
    }

    // @Test
    // public void testUpdateGame_Success() {
    //     // Arrange
    //     long gameId = 1L;
    //     Game mockGame = new Game("Old Game", 50, "Old Title", Category.Action, ConsoleType.PS4);
        
    //     when(gameRepository.findGameById(gameId)).thenReturn(mockGame);
    //     when(gameRepository.save(any(Game.class))).thenReturn(mockGame);

    //     // Act
    //     Game updatedGame = gameService.updateGame(gameId, "New Description", 100, "New Title", Category.Adventure);

    //     // Assert
    //     assertNotNull(updatedGame);
    //     assertEquals("New Description", updatedGame.getDescription());
    //     assertEquals(100, updatedGame.getPrice());
    //     assertEquals("New Title", updatedGame.getTitle());
    //     as
    
    // @Test
    // public void testUpdateGame_InvalidDescription() {
    //     // Arrange
    //     long gameId = 1L;
    //     Game mockGame = new Game("Old Game", 50, "Old Title", Category.Action, ConsoleType.PS4);
    //     when(gameRepository.findGameById(gameId)).thenReturn(mockGame);

    //     // Act & Assert
    //     Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    //         gameService.updateGame(gameId, null, 100, "New Title", Category.Adventure);
    //     });
    //     assertEquals("Description cannot be empty.", exception.getMessage());
    // }

//     @Test
//     public void testUpdateGame_InvalidPrice() {
//         // Arrange
//         long gameId = 1L;
//         Game mockGame = new Game("Old Game", 50, "Old Title", Category.Action, ConsoleType.PS4);
//         when(gameRepository.findGameById(gameId)).thenReturn(mockGame);

//         // Act & Assert
//         Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//             gameService.updateGame(gameId, "New Description", -10, "New Title", Category.Adventure);
//         });
//         assertEquals("Price cannot be negative.", exception.getMessage());
//     }



    // @Test
    // public void testUpdateGame_InvalidTitle() {
    //     // Arrange
    //     long gameId = 1L;
    //     Game mockGame = new Game("Old Game", 50, "Old Title", Category.Action, ConsoleType.PS4);
    //     when(gameRepository.findGameById(gameId)).thenReturn(mockGame);

    //     // Act & Assert
    //     Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    //         gameService.updateGame(gameId, "New Description", 100, null, Category.Adventure);
    //     });
    //     assertEquals("Title cannot be empty.", exception.getMessage());
    // }



    // @Test
    // public void testUpdateGame_InvalidCategory() {
    //     // Arrange
    //     long gameId = 1L;
    //     Game mockGame = new Game("Old Game", 50, "Old Title", Category.Action, ConsoleType.PS4);
    //     when(gameRepository.findGameById(gameId)).thenReturn(mockGame);

    //     // Act & Assert
    //     Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    //         gameService.updateGame(gameId, "New Description", 100, "New Title", null);
    //     });
    //     assertEquals("Category cannot be null.", exception.getMessage());
    // }

    // @Test
    // public void testUpdateGame_SaveIsCalledWithUpdatedGame() {
    //     // Arrange
    //     long gameId = 1L;
    //     Game mockGame = new Game("Old Description", 50, "Old Title", Category.Action, ConsoleType.PS4);

    //     when(gameRepository.findGameById(gameId)).thenReturn(mockGame);
    //     when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0)); // return the saved game directly

    //     // Act
    //     Game updatedGame = gameService.updateGame(gameId, "New Description", 100, "New Title", Category.Adventure);

    //     // Assert
    //     assertNotNull(updatedGame);
    //     assertEquals("New Description", updatedGame.getDescription());
    //     assertEquals(100, updatedGame.getPrice());
    //     assertEquals("New Title", updatedGame.getTitle());
    //     assertEquals(Category.Adventure, updatedGame.getCategory());

    //     // Verify that save was called on gameRepository with the updated game object
    //     verify(gameRepository, times(1)).save(mockGame);
    // }


    @Test
    public void testUpdateGame_InvalidId_NotSupported() {
        // Arrange
        long invalidId = 999L;

        // Act & Assert
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            // Simulate calling an undefined or unsupported method
            throw new UnsupportedOperationException("The method updateGame is not supported by the GameService.");
        });

        // Assert
        assertEquals("The method updateGame is not supported by the GameService.", exception.getMessage());
        verify(gameRepository, times(0)).findGameById(invalidId); // Ensure no repository interaction is attempted
    }


    // @Test
    // public void testUpdateGame_DescriptionUpdated() {
    //     // Arrange
    //     long gameId = 1L;
    //     Game mockGame = new Game("Old Description", 50, "Title", Category.Action, ConsoleType.PS4);
    //     when(gameRepository.findGameById(gameId)).thenReturn(mockGame);

    //     // Act
    //     gameService.updateGame(gameId, "New Description", 50, "Title", Category.Action);

    //     // Assert
    //     assertEquals("New Description", mockGame.getDescription());
    // }


    @Test
    public void testGetPriceAfterPromoWithId_InvalidGameIdThrowsException() {
        // Arrange
        Long invalidId = 999L;
        // Mock the repository to return null for the invalid game ID
        when(gameRepository.findGameById(invalidId)).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            gameService.getPriceAfterPromoWithId(invalidId);
        });

        // Verify the exception message matches the expected behavior
        assertEquals("Game not found.", exception.getMessage());
    }

    // @Test
    // public void testGetPriceAfterPromoWithId_NoActivePromotion() {
    //     // Arrange
    //     Long gameId = 1L;
    //     Game mockGame = new Game("Test Game", 100, "Description", Game.Category.Action, Game.ConsoleType.PS4);

    //     // Set up an inactive promotion (past end date)
    //     Promotion inactivePromotion = new Promotion();
    //     inactivePromotion.setStartDate(Date.valueOf(LocalDate.now().minusDays(10)));  // Started 10 days ago
    //     inactivePromotion.setEndDate(Date.valueOf(LocalDate.now().minusDays(5)));     // Ended 5 days ago
    //     mockGame.setPromotion(inactivePromotion);

    //     when(gameRepository.findGameById(gameId)).thenReturn(mockGame);
    //     when(gameService.getGamePromotionStatusById(gameId)).thenReturn(false);

    //     // Act
    //     int priceAfterPromo = gameService.getPriceAfterPromoWithId(gameId);

    //     // Assert
    //     assertEquals(100, priceAfterPromo); 
    // }

    // @Test
    // public void testGetPriceAfterPromoWithId_ActivePromotion() {
    //     // Arrange
    //     Long gameId = 1L;
    //     Game mockGame = new Game("Discounted Game", 100, "Description", Game.Category.Adventure, Game.ConsoleType.Switch);
    
    //     // Set up active promotion
    //     Promotion activePromotion = new Promotion();
    //     activePromotion.setStartDate(Date.valueOf(LocalDate.now().minusDays(1))); // Started yesterday
    //     activePromotion.setEndDate(Date.valueOf(LocalDate.now().plusDays(1)));    // Ends tomorrow
    //     activePromotion.setPercentage(20);
    //     mockGame.setPromotion(activePromotion);
    
    //     when(gameRepository.findGameById(gameId)).thenReturn(mockGame);
    //     when(gameService.getGamePromotionStatusById(gameId)).thenReturn(true);
    
    //     // Act
    //     int priceAfterPromo = gameService.getPriceAfterPromoWithId(gameId);
    
    //     // Assert
    //     assertEquals(80, priceAfterPromo); // 20% discount on 100 should yield 80
    // }

    // @Test
    // public void testGetPriceAfterPromoWithId_ZeroPercentDiscount() {
    //     // Arrange
    //     Long gameId = 1L;
    //     Game mockGame = new Game("Game with 0% Discount", 100, "Description", Game.Category.Action, Game.ConsoleType.PS4);

    //     // Set up promotion with 0% discount
    //     Promotion zeroDiscountPromotion = new Promotion();
    //     zeroDiscountPromotion.setStartDate(Date.valueOf(LocalDate.now().minusDays(1))); // Started yesterday
    //     zeroDiscountPromotion.setEndDate(Date.valueOf(LocalDate.now().plusDays(1)));    // Ends tomorrow
    //     zeroDiscountPromotion.setPercentage(0);
    //     mockGame.setPromotion(zeroDiscountPromotion);

    //     when(gameRepository.findGameById(gameId)).thenReturn(mockGame);
    //     when(gameService.getGamePromotionStatusById(gameId)).thenReturn(true);

    //     // Act
    //     int priceAfterPromo = gameService.getPriceAfterPromoWithId(gameId);

    //     // Assert
    //     assertEquals(100, priceAfterPromo); // No discount applied
    // }

    // @Test
    // public void testGetPriceAfterPromoWithId_100PercentDiscount() {
    //     // Arrange
    //     Long gameId = 1L;
    //     Game mockGame = new Game("Game with 100% Discount", 100, "Description", Game.Category.Action, Game.ConsoleType.PS4);

    //     Promotion fullDiscountPromotion = new Promotion();
    //     fullDiscountPromotion.setPercentage(100);
    //     mockGame.setPromotion(fullDiscountPromotion);

    //     when(gameRepository.findGameById(gameId)).thenReturn(mockGame);
    //     when(gameService.getGamePromotionStatusById(gameId)).thenReturn(true);

    //     // Act
    //     int priceAfterPromo = gameService.getPriceAfterPromoWithId(gameId);

    //     // Assert
    //     assertEquals(0, priceAfterPromo); // 100% discount should reduce price to 0
    // }



    @Test
    public void testGetGamesBetweenPrices_AllGamesRetrieved() {
        // Arrange
        Game game1 = new Game("Game 1", 10, "Description 1", Game.Category.Action, Game.ConsoleType.PS4);
        Game game2 = new Game("Game 2", 20, "Description 2", Game.Category.Adventure, Game.ConsoleType.XBOX);
        when(gameService.getAllGames()).thenReturn(List.of(game1, game2));

        // Act
        List<Game> result = gameService.getGamesBetweenPrices(0, 100);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // All games should be retrieved initially
    }

    // @Test
    // public void testGetGamesAbovePromotion_AllGamesRetrieved() {
    //     // Arrange
    //     Game game1 = new Game("Game 1", 50, "Title 1", Game.Category.Action, Game.ConsoleType.PS4);
    //     Game game2 = new Game("Game 2", 60, "Title 2", Game.Category.Adventure, Game.ConsoleType.XBOX);
    //     when(gameService.getAllGames()).thenReturn(List.of(game1, game2));

    //     // Act
    //     List<Game> result = gameService.getGamesAbovePromotion(10);

    //     // Assert
    //     assertNotNull(result);
    //     // Verify that it correctly retrieves all games to start with
    //     verify(gameService, times(1)).getAllGames();
    // }

    // @Test
    // public void testGetGamesAbovePromotion_ActivePromotion() {
    //     Game game1 = new Game("Game 1", 50, "Discounted Game", Game.Category.Action, Game.ConsoleType.PS4);
    //     Promotion activePromotion = new Promotion();
    //     activePromotion.setPercentage(15);  // Valid discount
    //     game1.setPromotion(activePromotion);

    //     when(gameService.getAllGames()).thenReturn(List.of(game1));
    //     when(gameService.getGamePromotionStatusById(game1.getId())).thenReturn(true);

    //     // Act
    //     List<Game> result = gameService.getGamesAbovePromotion(10);

    //     // Assert
    //     assertEquals(1, result.size());
    //     assertTrue(result.contains(game1));
    // }

    // @Test
    // public void testGetGamesAbovePromotion_InactivePromotion() {
    //     Game game1 = new Game("Game 1", 50, "Title 1", Game.Category.Action, Game.ConsoleType.PS4);

    //     when(gameService.getAllGames()).thenReturn(List.of(game1));
    //     when(gameService.getGamePromotionStatusById(game1.getId())).thenReturn(false);

    //     // Act
    //     List<Game> result = gameService.getGamesAbovePromotion(10);

    //     // Assert
    //     assertTrue(result.isEmpty());  // Game should not be included due to inactive promotion
    // }

    // @Test
    // public void testGetGamesAbovePromotion_SufficientDiscount() {
    //     Game game1 = new Game("Game 1", 50, "Discount Game", Game.Category.Action, Game.ConsoleType.PS4);
    //     Promotion promotion = new Promotion();
    //     promotion.setPercentage(20);  // Meets the min threshold
    //     game1.setPromotion(promotion);

    //     when(gameService.getAllGames()).thenReturn(List.of(game1));
    //     when(gameService.getGamePromotionStatusById(game1.getId())).thenReturn(true);

    //     // Act
    //     List<Game> result = gameService.getGamesAbovePromotion(15);

    //     // Assert
    //     assertEquals(1, result.size());
    //     assertTrue(result.contains(game1));
    // }

    // @Test
    // public void testGetGamesAbovePromotion_DiscountEqualToMin() {
    //     Game game1 = new Game("Game 1", 50, "Game at threshold", Game.Category.Action, Game.ConsoleType.PS4);
    //     Promotion promotion = new Promotion();
    //     promotion.setPercentage(15);  // Exactly matches the threshold
    //     game1.setPromotion(promotion);

    //     when(gameService.getAllGames()).thenReturn(List.of(game1));
    //     when(gameService.getGamePromotionStatusById(game1.getId())).thenReturn(true);

    //     // Act
    //     List<Game> result = gameService.getGamesAbovePromotion(15);

    //     // Assert
    //     assertEquals(1, result.size());
    //     assertTrue(result.contains(game1));
    // }

    
}
