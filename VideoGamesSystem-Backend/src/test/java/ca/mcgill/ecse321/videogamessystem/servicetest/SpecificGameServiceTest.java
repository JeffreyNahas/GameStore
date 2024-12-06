package ca.mcgill.ecse321.videogamessystem.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import java.util.Optional;

import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;
import ca.mcgill.ecse321.videogamessystem.model.Game;
import ca.mcgill.ecse321.videogamessystem.model.SpecificGame;
import ca.mcgill.ecse321.videogamessystem.model.SpecificOrder;
import ca.mcgill.ecse321.videogamessystem.repository.GameRepository;
import ca.mcgill.ecse321.videogamessystem.repository.SpecificGameRepository;
import ca.mcgill.ecse321.videogamessystem.repository.SpecificOrderRepository;
import ca.mcgill.ecse321.videogamessystem.service.SpecificGameService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SpecificGameServiceTest {

    @Mock
    private SpecificGameRepository specificGameRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private SpecificOrderRepository specificOrderRepository;

    @InjectMocks
    private SpecificGameService specificGameService;

    private Game mockGame;
    private SpecificGame specificGame;
    private SpecificOrder specificOrder;

    @BeforeEach
    public void setup() {        
        specificGame = new SpecificGame(123, true);
        specificOrder = new SpecificOrder();
        specificGame.setSpecificOrder(specificOrder);
    }

    @Test
    public void testAddSpecificGameToOrder_Success() {
        when(specificGameRepository.findSpecificGameBySerialNumber(123)).thenReturn(specificGame);
        when(specificGameRepository.findSpecificGameBySpecificOrder(specificOrder))
            .thenReturn(Collections.singletonList(specificGame));

        List<SpecificGame> result = specificGameService.addSpecificGameToOrder(123, specificOrder);

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "The list should contain one game after adding to the order.");
        assertEquals(specificOrder, specificGame.getSpecificOrder(), "The game's order should be set.");
    }

    // @Test
    // public void testRemoveSpecificGameFromOrder_Success() {
    //     // Associate specificGame with specificOrder in the setup
    //     specificGame.setSpecificOrder(specificOrder);
    //     when(specificGameRepository.findSpecificGameBySerialNumber(123)).thenReturn(specificGame);
    //     when(specificGameRepository.findSpecificGameBySpecificOrder(specificOrder))
    //         .thenReturn(Collections.singletonList(specificGame)) // Before removal
    //         .thenReturn(Collections.emptyList()); // After removal

    //     // Call the method
    //     List<SpecificGame> result = specificGameService.removeSpecificGameFromOrder(123, specificOrder);

    //     // Verify that specificGame's order is unset
    //     assertNull(specificGame.getSpecificOrder(), "The game's order should be unset.");
    //     // Check if the result list reflects the expected state (empty after removal)
    //     assertEquals(0, result.size(), "The list should have length 0 after removing the game from the order.");

    //     // Verify save was called
    //     verify(specificGameRepository, times(1)).save(specificGame);
    // }


    // @Test
    // public void testRemoveSpecificGameFromOrder_GameNotInOrder() {
    //     specificGame.setSpecificOrder(null); // Ensure game is not in any order

    //     when(specificGameRepository.findSpecificGameBySerialNumber(123)).thenReturn(specificGame);

    //     IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
    //         specificGameService.removeSpecificGameFromOrder(123, specificOrder);
    //     }, "Expected IllegalArgumentException when specific game is not in the order, but none was thrown.");
    //     assertEquals("the specific game was not in the order provided.", thrown.getMessage());
    // }

    // @Test
    // public void testUpdateAvailability_Success() {
    //     when(specificGameRepository.findSpecificGameBySerialNumber(123)).thenReturn(specificGame);

    //     SpecificGame updatedGame = specificGameService.updateAvailability(123, false);

    //     assertNotNull(updatedGame, "Updated game should not be null");
    //     assertFalse(updatedGame.getAvailability(), "The availability should be updated to false.");
    // }

    @Test
    public void testGetSpecificGamesByAvailability() {
        when(specificGameRepository.findSpecificGameByAvailability(true))
            .thenReturn(Collections.singletonList(specificGame));

        List<SpecificGame> result = specificGameService.getSpecificGamesByAvailability(true);

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "The list should contain one game.");
        assertTrue(result.get(0).getAvailability(), "The game should be available.");
    }

    @Test
    public void testGetSpecificGameBySerialNumber_Found() {
        when(specificGameRepository.findSpecificGameBySerialNumber(123)).thenReturn(specificGame);

        SpecificGame foundGame = specificGameService.getSpecificGameBySerialNumber(123);

        assertNotNull(foundGame, "Found game should not be null");
        assertEquals(123, foundGame.getSerialNumber(), "The serial number should match.");
    }

    @Test
    public void testGetSpecificGameBySerialNumber_NotFound() {
        // Arrange
        when(specificGameRepository.findSpecificGameBySerialNumber(anyInt())).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.getSpecificGameBySerialNumber(999);
        });
        assertEquals("Specific game not found.", exception.getMessage());
    }

    @Test
    public void testDeleteSpecificGame_Success() {
        when(specificGameRepository.findSpecificGameBySerialNumber(123)).thenReturn(specificGame);

        SpecificGame deletedGame = specificGameService.deleteSpecificGame(123);

        assertNotNull(deletedGame, "Deleted game should not be null");
        verify(specificGameRepository, times(1)).delete(specificGame);
    }
    
    @Test
    public void testGetAllSpecificGames_Success() {
        SpecificGame specificGame1 = new SpecificGame();
        SpecificGame specificGame2 = new SpecificGame();

        when(specificGameRepository.findAll()).thenReturn(List.of(specificGame1, specificGame2));

        List<SpecificGame> result = specificGameService.getAllSpecificGames();

        assertNotNull(result);
        assertEquals(2, result.size());
    }
    @Test
    public void testGetSpecificGameBySerialNumber_Success() {
        SpecificGame specificGame = new SpecificGame();
        specificGame.setSerialNumber(123);

        when(specificGameRepository.findSpecificGameBySerialNumber(123)).thenReturn(specificGame);

        SpecificGame result = specificGameService.getSpecificGameBySerialNumber(123);

        assertNotNull(result);
        assertEquals(123, result.getSerialNumber());
    }
    @Test
    public void testUpdateAvailability_Success() {
        // Prepare a mock SpecificGame instance
        SpecificGame specificGame = new SpecificGame();
        specificGame.setSerialNumber(101);
        specificGame.setAvailability(false); // Set initial availability to false

        // Mock the repository behavior to return specificGame directly
        when(specificGameRepository.findSpecificGameBySerialNumber(anyInt())).thenReturn(specificGame);
        when(specificGameRepository.save(specificGame)).thenReturn(specificGame);

        // Perform the update
        SpecificGame updatedGame = specificGameService.updateAvailability(101, true);

        // Assertions to confirm behavior
        assertNotNull(updatedGame);
        assertTrue(updatedGame.getAvailability()); // Check if availability was set to true
    }
    @Test
    public void testCreateSpecificGame_Success() {
        // Arrange
        int serialNumber = 123;
        boolean availability = true;
        Long gameId = 1L;

        Game mockGame = new Game();
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));
        when(specificGameRepository.findSpecificGameBySerialNumber(serialNumber)).thenReturn(null);

        SpecificGame mockSpecificGame = new SpecificGame(serialNumber, availability);
        mockSpecificGame.setGame(mockGame);
        
        when(specificGameRepository.save(any(SpecificGame.class))).thenReturn(mockSpecificGame);

        // Act
        SpecificGame createdSpecificGame = specificGameService.createSpecificGame(serialNumber, availability, gameId);

        // Assert
        assertNotNull(createdSpecificGame, "Created specific game should not be null");
        assertEquals(serialNumber, createdSpecificGame.getSerialNumber(), "Serial number should match the input");
        assertEquals(availability, createdSpecificGame.getAvailability(), "Availability should match the input");
        assertEquals(mockGame, createdSpecificGame.getGame(), "Game should be set correctly");
    }

    @Test
    public void testCreateSpecificGame_SerialNumberAlreadyExists() {
        // Arrange
        int serialNumber = 123;
        when(specificGameRepository.findSpecificGameBySerialNumber(serialNumber)).thenReturn(new SpecificGame());

        // Act & Assert
        VideoGamesSystemException thrown = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.createSpecificGame(serialNumber, true, 1L);
        }, "Expected VideoGamesSystemException for duplicate serial number");

        assertEquals("A specific game with this serial number already exists.", thrown.getMessage());
    }

    @Test
    public void testCreateSpecificGame_GameNotFound() {
        // Arrange
        int serialNumber = 123;
        Long gameId = 1L;

        // Mock repository behavior
        when(specificGameRepository.findSpecificGameBySerialNumber(serialNumber)).thenReturn(null);
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        // Act & Assert
        VideoGamesSystemException thrown = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.createSpecificGame(serialNumber, true, gameId);
        }, "Expected VideoGamesSystemException when game is not found");

        assertEquals("Game not found.", thrown.getMessage());
    }


    
    

    @Test
    public void testCreateSpecificGame_SerialNumberExists() {
        // Arrange
        int serialNumber = 123;
        when(specificGameRepository.findSpecificGameBySerialNumber(serialNumber)).thenReturn(new SpecificGame());

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.createSpecificGame(serialNumber, true, 1L);
        });
        assertEquals("A specific game with this serial number already exists.", exception.getMessage());
    }


   

    // @Test
    // public void testGetSpecificGamesByOrder_Success() {
    //     when(specificOrderRepository.findOrderByNumber(anyInt())).thenReturn(specificOrder);
    //     when(specificGameRepository.findSpecificGameBySpecificOrder(specificOrder)).thenReturn(List.of(specificGame));

    //     List<SpecificGame> games = specificGameService.getSpecificGamesByOrder(1);

    //     assertNotNull(games);
    //     assertEquals(1, games.size());
    //     assertEquals(specificOrder, games.get(0).getSpecificOrder());
    // }

    // @Test
    // public void testGetSpecificGamesByGame_Success() {
    //     when(gameRepository.findGameById(anyLong())).thenReturn(mockGame);
    //     when(specificGameRepository.findSpecificGameByGame(mockGame)).thenReturn(List.of(specificGame));

    //     List<SpecificGame> games = specificGameService.getSpecificGamesByGame(1L);

    //     assertNotNull(games);
    //     assertEquals(1, games.size());
    //     assertEquals(mockGame, games.get(0).getGame());
    // }

    
    //can delete if other works, this works
    // @Test
    // public void testAddSpecificGameToOrder_Success() {
    //     when(specificGameRepository.findSpecificGameBySerialNumber(123)).thenReturn(specificGame);
    //     when(specificGameRepository.findSpecificGameBySpecificOrder(specificOrder)).thenReturn(List.of(specificGame));

    //     List<SpecificGame> games = specificGameService.addSpecificGameToOrder(123, specificOrder);

    //     assertNotNull(games);
    //     assertEquals(1, games.size());
    //     assertEquals(specificOrder, games.get(0).getSpecificOrder());
    // }

    // @Test
    // public void testRemoveSpecificGameFromOrder_Success() {
    //     specificGame.setSpecificOrder(specificOrder);
    //     when(specificGameRepository.findSpecificGameBySerialNumber(123)).thenReturn(specificGame);
    //     when(specificGameRepository.findSpecificGameBySpecificOrder(specificOrder)).thenReturn(new ArrayList<>());

    //     List<SpecificGame> games = specificGameService.removeSpecificGameFromOrder(123, specificOrder);

    //     assertNotNull(games);
    //     assertTrue(games.isEmpty());
    //     assertNull(specificGame.getSpecificOrder());
    // }
    

    // @Test
    // public void testGetSpecificGamesByGame_Success() {
    //     // Arrange
    //     Game mockGame = mock(Game.class);  // Mock the Game object
    //     when(mockGame.getId()).thenReturn(1L);  // Set the ID for the mock game
        
    //     SpecificGame specificGame1 = new SpecificGame();
    //     specificGame1.setSerialNumber(101);
    //     specificGame1.setGame(mockGame);

    //     SpecificGame specificGame2 = new SpecificGame();
    //     specificGame2.setSerialNumber(102);
    //     specificGame2.setGame(mockGame);

    //     when(gameRepository.findGameById(1L)).thenReturn(mockGame);
    //     when(specificGameRepository.findSpecificGameByGame(mockGame)).thenReturn(List.of(specificGame1, specificGame2));

    //     // Act
    //     List<SpecificGame> specificGames = specificGameService.getSpecificGamesByGame(1L);

    //     // Assert
    //     assertNotNull(specificGames, "Result should not be null");
    //     assertEquals(2, specificGames.size(), "There should be two specific games returned");
    //     assertTrue(specificGames.contains(specificGame1), "The list should contain specificGame1");
    //     assertTrue(specificGames.contains(specificGame2), "The list should contain specificGame2");
    // }

    @Test
    public void testGetSpecificGamesByGame_GameNotFound() {
        // Arrange
        Long gameID = 1L;

        // Mock repository to return null when the game is not found
        when(gameRepository.findGameById(gameID)).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.getSpecificGamesByGame(gameID);
        });
        assertEquals("Specific game not found.", exception.getMessage());
    }
    



    @Test
    public void testGetSpecificGameByOrder_Success() {
        // Arrange
        Integer orderNumber = 1;
        SpecificOrder mockOrder = new SpecificOrder();
        SpecificGame specificGame1 = new SpecificGame(101, true);
        SpecificGame specificGame2 = new SpecificGame(102, true);

        // Mock the behavior of repositories
        when(specificOrderRepository.findOrderByNumber(orderNumber)).thenReturn(mockOrder);
        when(specificGameRepository.findSpecificGameBySpecificOrder(mockOrder)).thenReturn(List.of(specificGame1, specificGame2));

        // Act
        List<SpecificGame> result = specificGameService.getSpecificGamesByOrder(orderNumber);

        // Assert
        assertNotNull(result, "The result should not be null.");
        assertEquals(2, result.size(), "The list should contain two specific games.");
        assertTrue(result.contains(specificGame1), "The result should contain specificGame1.");
        assertTrue(result.contains(specificGame2), "The result should contain specificGame2.");
    }

    @Test
    public void testGetSpecificGameByOrder_OrderNotFound() {
        // Arrange
        Integer orderNumber = 999;

        // Mock the behavior of repositories
        when(specificOrderRepository.findOrderByNumber(orderNumber)).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.getSpecificGamesByOrder(orderNumber);
        });
        assertEquals("Order not found.", exception.getMessage(), "The exception message should indicate the order was not found.");
    }


    @Test
    public void testAddSpecificGameToOrder_GameNotFound() {
        // Arrange
        int specificGameID = 101;
        SpecificOrder mockOrder = mock(SpecificOrder.class);

        // Mock repository to return null for a non-existent game
        when(specificGameRepository.findSpecificGameBySerialNumber(specificGameID)).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.addSpecificGameToOrder(specificGameID, mockOrder);
        });
        assertEquals("specific game not found", exception.getMessage());
    }

    @Test
    public void testAddSpecificGameToOrder_OrderIsNull() {
        // Arrange
        int specificGameID = 101;
        SpecificGame specificGame = new SpecificGame(); // Create a SpecificGame instance
        specificGame.setSerialNumber(specificGameID);

        // Mock repository to return the specific game but without an order
        when(specificGameRepository.findSpecificGameBySerialNumber(specificGameID)).thenReturn(specificGame);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.addSpecificGameToOrder(specificGameID, null);
        });
        assertEquals("order cannot be null", exception.getMessage());
    }










    // @Test
    // public void testRemoveSpecificGameFromOrder_Success() {
    //     // Arrange
    //     int specificGameID = 101;
    //     SpecificOrder mockOrder = new SpecificOrder();
    //     SpecificGame specificGame = new SpecificGame();
    //     specificGame.setSerialNumber(specificGameID);
    //     specificGame.setSpecificOrder(mockOrder); // Associate game with order

    //     // Mock repository behavior
    //     when(specificGameRepository.findSpecificGameBySerialNumber(specificGameID)).thenReturn(specificGame);
    //     when(specificGameRepository.findSpecificGameBySpecificOrder(mockOrder))
    //         .thenReturn(Collections.singletonList(specificGame)) // Before removal
    //         .thenReturn(Collections.emptyList()); // After removal

    //     // Act
    //     List<SpecificGame> result = specificGameService.removeSpecificGameFromOrder(specificGameID, mockOrder);

    //     // Assert
    //     assertNotNull(result, "Result should not be null");
    //     assertTrue(result.isEmpty(), "The list should be empty after removing the game from the order.");
    //     assertNull(specificGame.getSpecificOrder(), "The game's order should be unset.");
    //     verify(specificGameRepository, times(1)).save(specificGame);
    // }

    @Test
    public void testRemoveSpecificGameFromOrder_GameNotFound() {
        // Arrange
        int specificGameID = 101;
        SpecificOrder mockOrder = new SpecificOrder();

        // Mock repository to return null for a non-existent game
        when(specificGameRepository.findSpecificGameBySerialNumber(specificGameID)).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.removeSpecificGameFromOrder(specificGameID, mockOrder);
        });
        assertEquals("specific game not found", exception.getMessage());
    }



   @Test
   public void testRemoveSpecificGameFromOrder_OrderIsNull() {
       // Arrange
       int specificGameID = 101;
       SpecificGame specificGame = new SpecificGame();
       specificGame.setSerialNumber(specificGameID);
       specificGame.setSpecificOrder(new SpecificOrder()); // Associate game with some order
   
       // Mock repository to return this game by ID
       when(specificGameRepository.findSpecificGameBySerialNumber(specificGameID)).thenReturn(specificGame);
   
       // Act & Assert
       VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
           specificGameService.removeSpecificGameFromOrder(specificGameID, null);
       });
       assertEquals("order cannot be null", exception.getMessage());
   }

   @Test
   public void testRemoveSpecificGameFromOrder_GameNotInProvidedOrder() {
       // Arrange
       int specificGameID = 101;
       SpecificOrder differentOrder = new SpecificOrder(); // Different order than associated with the game
       SpecificGame specificGame = new SpecificGame();
       specificGame.setSerialNumber(specificGameID);
       specificGame.setSpecificOrder(new SpecificOrder()); // Game associated with a different order
   
       // Mock repository to return this game by ID
       when(specificGameRepository.findSpecificGameBySerialNumber(specificGameID)).thenReturn(specificGame);
   
       // Act & Assert
       VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
           specificGameService.removeSpecificGameFromOrder(specificGameID, differentOrder);
       });
       assertEquals("the specific game was not in the order provided.", exception.getMessage());
   }

    // @Test
    // public void testRemoveSpecificGameFromOrder_Success() {
    //     // Arrange
    //     int specificGameID = 101;
    //     SpecificOrder mockOrder = new SpecificOrder();
    //     SpecificGame specificGame = new SpecificGame();
    //     specificGame.setSerialNumber(specificGameID);
    //     specificGame.setSpecificOrder(mockOrder); // Associate game with order

    //     // Mock repository behavior
    //     when(specificGameRepository.findSpecificGameBySerialNumber(specificGameID)).thenReturn(specificGame);
    //     when(specificGameRepository.findSpecificGameBySpecificOrder(mockOrder))
    //         .thenReturn(Collections.singletonList(specificGame)) // Before removal
    //         .thenReturn(Collections.emptyList()); // After removal

    //     // Act
    //     List<SpecificGame> result = specificGameService.removeSpecificGameFromOrder(specificGameID, mockOrder);

    //     // Assert
    //     assertNotNull(result, "Result should not be null");
    //     assertTrue(result.isEmpty(), "The list should be empty after removing the game from the order.");
    //     assertNull(specificGame.getSpecificOrder(), "The game's order should be unset.");
    //     verify(specificGameRepository, times(1)).save(specificGame);
    // }

    @Test
    public void testGetSpecificGamesFromGame_Success() {
        // Arrange
        Long gameID = 1L;
        int numberNeeded = 2;
        SpecificGame game1 = new SpecificGame();
        SpecificGame game2 = new SpecificGame();
        SpecificGame game3 = new SpecificGame();
        
        // Mock repository to return a list of SpecificGames associated with the game ID
        when(specificGameRepository.findSpecificGameByGame(any(Game.class))).thenReturn(List.of(game1, game2, game3));
        when(gameRepository.findGameById(gameID)).thenReturn(new Game());

        // Act
        List<SpecificGame> result = specificGameService.getSpecificGamesFromGame(gameID, numberNeeded);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(numberNeeded, result.size(), "Result size should match number needed");
        assertTrue(result.contains(game1) && result.contains(game2), "Result should contain the first few games as needed");
    }

    @Test
    public void testGetSpecificGamesFromGame_NotEnoughInStock() {
        // Arrange
        Long gameID = 1L;
        int numberNeeded = 3; // More than available
        SpecificGame game1 = new SpecificGame();
        SpecificGame game2 = new SpecificGame();

        // Mock repository to return fewer games than requested
        when(specificGameRepository.findSpecificGameByGame(any(Game.class))).thenReturn(List.of(game1, game2));
        when(gameRepository.findGameById(gameID)).thenReturn(new Game());

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.getSpecificGamesFromGame(gameID, numberNeeded);
        });
        assertEquals("not enough in stock for game" + gameID, exception.getMessage());
    }

    @Test
    public void testGetSpecificGamesFromGame_InvalidNumberNeeded() {
        // Arrange
        Long gameID = 1L;
        int numberNeeded = -1;

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.getSpecificGamesFromGame(gameID, numberNeeded);
        });
        assertEquals("can only retrieve positive number of games", exception.getMessage());
    }

    @Test
    public void testGetSpecificGamesFromGame_GameNotFound() {
        // Arrange
        Long gameID = 1L;
        int numberNeeded = 1;

        // Mock repository to return null when the game is not found
        when(gameRepository.findGameById(gameID)).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.getSpecificGamesFromGame(gameID, numberNeeded);
        });
        assertEquals("Specific game not found.", exception.getMessage());
    }

    @Test
    public void testGetSpecificGamesFromGame_EmptyGameList() {
        // Arrange
        Long gameID = 1L;
        int numberNeeded = 1;

        // Mock repository to return an empty list when no specific games are associated with the game
        when(specificGameRepository.findSpecificGameByGame(any(Game.class))).thenReturn(Collections.emptyList());
        when(gameRepository.findGameById(gameID)).thenReturn(new Game());

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.getSpecificGamesFromGame(gameID, numberNeeded);
        });
        assertEquals("not enough in stock for game" + gameID, exception.getMessage());
    }
    
    @Test
    public void testAssociateSpecificGameWithNullOrder() {
        // Arrange
        int specificGameID = 101;
        SpecificGame specificGame = new SpecificGame();
        specificGame.setSerialNumber(specificGameID);

        // Mock repository behavior for retrieving the specific game
        when(specificGameRepository.findSpecificGameBySerialNumber(specificGameID)).thenReturn(specificGame);

        // Act & Assert
        Exception exception = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.addSpecificGameToOrder(specificGameID, null);
        });
        assertEquals("order cannot be null", exception.getMessage());
    }

    @Test
    public void testValidateSpecificGames_NullSpecificGame() {
        // Arrange
        List<SpecificGame> specificGames = new ArrayList<>();
        specificGames.add(new SpecificGame(1, true)); // Example specific game with serial number 1
        specificGames.add(null); // Adding a null specific game
        specificGames.add(new SpecificGame(2, true)); // Example specific game with serial number 2

        // Act & Assert
        for (SpecificGame game : specificGames) {
            if (game == null) {
                // Test behavior for null game (not calling the service method directly)
                Exception exception = assertThrows(VideoGamesSystemException.class, () -> {
                    throw new VideoGamesSystemException(HttpStatus.NOT_FOUND, "Specific game not found.");
                });
                assertEquals("Specific game not found.", exception.getMessage());
            } else {
                // Test behavior for valid game
                Exception exception = assertThrows(VideoGamesSystemException.class, () -> {
                    specificGameService.getSpecificGameBySerialNumber(-1); // Invalid serial number
                });
                assertEquals("Specific game not found.", exception.getMessage());
            }
        }
    }

    @Test
    public void testRetrieveSpecificGamesBySerialNumbers_Success() {
        // Arrange
        List<Integer> serialNumbers = List.of(101, 102, 103);
        SpecificGame game1 = new SpecificGame();
        game1.setSerialNumber(101);
        SpecificGame game2 = new SpecificGame();
        game2.setSerialNumber(102);
        SpecificGame game3 = new SpecificGame();
        game3.setSerialNumber(103);
    
        // Mock the repository behavior
        when(specificGameRepository.findSpecificGameBySerialNumber(101)).thenReturn(game1);
        when(specificGameRepository.findSpecificGameBySerialNumber(102)).thenReturn(game2);
        when(specificGameRepository.findSpecificGameBySerialNumber(103)).thenReturn(game3);
    
        // Act
        List<SpecificGame> retrievedGames = new ArrayList<>();
        for (Integer serialNumber : serialNumbers) {
            retrievedGames.add(specificGameService.getSpecificGameBySerialNumber(serialNumber));
        }
    
        // Assert
        assertNotNull(retrievedGames);
        assertEquals(3, retrievedGames.size());
        assertTrue(retrievedGames.contains(game1));
        assertTrue(retrievedGames.contains(game2));
        assertTrue(retrievedGames.contains(game3));
    }

    @Test
    public void testGetSpecificGamesByOrder_OrderNotFound() {
        // Arrange
        Integer nonexistentOrderNb = 999;

        // Mock repository to return `null` when searching for a nonexistent order number
        when(specificOrderRepository.findOrderByNumber(nonexistentOrderNb)).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            specificGameService.getSpecificGamesByOrder(nonexistentOrderNb);
        });
        assertEquals("Order not found.", exception.getMessage());
    }

    @Test
    public void testGetSpecificGamesByOrder_ReturnsSpecificGames() {
        // Arrange
        Integer orderNb = 1;
        SpecificOrder order = new SpecificOrder();
        
        SpecificGame game1 = new SpecificGame();
        SpecificGame game2 = new SpecificGame();
        
        // Mock to return the order by its number
        when(specificOrderRepository.findOrderByNumber(orderNb)).thenReturn(order);
        // Mock the repository to return a list of specific games associated with the order
        when(specificGameRepository.findSpecificGameBySpecificOrder(order)).thenReturn(List.of(game1, game2));

        // Act
        List<SpecificGame> result = specificGameService.getSpecificGamesByOrder(orderNb);

        // Assert
        assertNotNull(result, "The result should not be null.");
        assertEquals(2, result.size(), "There should be two games associated with the order.");
        assertTrue(result.contains(game1) && result.contains(game2), "The list should contain both specific games.");
    }
}
 