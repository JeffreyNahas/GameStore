package ca.mcgill.ecse321.videogamessystem.controller;

import ca.mcgill.ecse321.videogamessystem.dto.GameDto.GameRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.GameDto.GameResponseDto;
import ca.mcgill.ecse321.videogamessystem.dto.SpecificGameDto.SpecificGameResponseDto;
import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;
import ca.mcgill.ecse321.videogamessystem.model.Game;
import ca.mcgill.ecse321.videogamessystem.model.Promotion;
import ca.mcgill.ecse321.videogamessystem.model.SpecificGame;
import ca.mcgill.ecse321.videogamessystem.service.GameService;
import ca.mcgill.ecse321.videogamessystem.service.PromotionService;
import ca.mcgill.ecse321.videogamessystem.service.SpecificGameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private SpecificGameService specificGameService;

    @Autowired
    private PromotionService promotionService;
    /**
     * Creates a new game with the specified details.
     *
     * @param gameRequestDto the data transfer object containing game details.
     * @return a GameResponseDto with the created game details.
     */
    @PostMapping("/games")
    public GameResponseDto createGame(@Valid @RequestBody GameRequestDto gameRequestDto) {
        Game game = gameService.createGame(
                gameRequestDto.getDescription(),
                gameRequestDto.getPrice(),
                gameRequestDto.getTitle(),
                gameRequestDto.getCategory(),
                gameRequestDto.getConsoleType()
        );
        return new GameResponseDto(game);
    }
    /**
     * Generates specific games from the given stock quantity when creating a game.
     *
     * @param gameId the ID of the game for which specific games are being generated.
     * @param stockQuantity the number of specific games to generate.
     * @return a ResponseEntity containing a list of SpecificGameResponseDtos representing the generated specific games.
     */
    @PostMapping("/games/{gameId}/specific-games")
    public ResponseEntity<List<SpecificGameResponseDto>> generateSpecificGames(
            @PathVariable Long gameId,
            @RequestParam int stockQuantity) {
        try {
            List<SpecificGame> specificGames = specificGameService.generateSpecificGamesFromStockQuantity(gameId, stockQuantity);
            List<SpecificGameResponseDto> responseDtos = specificGames.stream()
                    .map(SpecificGameResponseDto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDtos);
        } catch (IllegalArgumentException | VideoGamesSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Retrieves a game by its unique ID.
     *
     * @param id the ID of the game to retrieve.
     * @return a GameResponseDto with the details of the requested game.
     */
    @GetMapping("/games/{id}")
    public GameResponseDto getGameById(@PathVariable Long id) {
        Game game = gameService.getGameById(id);
        return new GameResponseDto(game);
    }

    /**
     * Retrieves all games.
     *
     * @return a list of GameResponseDto containing details of all games.
     */
    @GetMapping("/games")
    public List<GameResponseDto> getAllGames() {
        List<Game> games = gameService.getAllGames();
        return games.stream().map(GameResponseDto::new).collect(Collectors.toList());
    }

    /**
     * Retrieves the stock quantity for a specific game.
     *
     * @param id the ID of the game.
     * @return the stock quantity of the game.
     */
    @GetMapping("/games/{id}/stock")
    public int getStockQuantity(@PathVariable Long id) {
        return gameService.getStockQuantity(id);
    }

    // /**
    //  * Generates specific games from the given stock quantity when creating a game.
    //  *
    //  * @param gameId the ID of the game for which specific games are being generated.
    //  * @param stockQuantity the number of specific games to generate.
    //  * @return a ResponseEntity indicating the status of the request.
    //  */
    // @PostMapping("/games/{gameId}/generate-specific-games")
    // public ResponseEntity<Void> generateSpecificGames(@PathVariable Long gameId, @RequestParam int stockQuantity) {
    //     try {
    //         specificGameService.generateSpecificGamesFromStockQuantity(gameId, stockQuantity);
    //         return ResponseEntity.status(HttpStatus.CREATED).build();  // Return status 201 (Created)
    //     } catch (IllegalArgumentException | VideoGamesSystemException e) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  // Return status 400 (Bad Request)
    //     }
    // }

    /**
     * Deletes a game by its unique ID.
     *
     * @param id the ID of the game to delete.
     * @return a GameResponseDto containing details of the deleted game.
     */
    @DeleteMapping("/games/{id}")
    public GameResponseDto deleteGame(@PathVariable Long id) {
        Game deletedGame = gameService.deleteGame(id);
        return new GameResponseDto(deletedGame);
    }

    /**
     * Retrieves games based on their title.
     *
     * @param title the title to filter games by.
     * @return a list of GameResponseDto containing details of games with the specified title.
     */
    @GetMapping("/games/title/{title}")
    public List<GameResponseDto> getGamesByTitle(@PathVariable String title) {
        List<Game> games = gameService.getGamesByTitle(title);
        return games.stream().map(GameResponseDto::new).collect(Collectors.toList());
    }

    /**
     * Retrieves games based on a specified category.
     *
     * @param category the category to filter games by.
     * @return a list of GameResponseDto containing details of games within the specified category.
     */
    @GetMapping("/games/category/{category}")
    public List<GameResponseDto> getGamesByCategory(@PathVariable Game.Category category) {
        List<Game> games = gameService.getGamesByCategory(category);
        return games.stream().map(GameResponseDto::new).collect(Collectors.toList());
    }

        /**
     * Adds a promotion to a game.
     *
     * @param id the ID of the game to update.
     * @param promotionId the ID of the promotion to add.
     * @return a GameResponseDto with the updated game details.
     */
    @PutMapping("/games/{id}/promotion/{promotionId}")
    public GameResponseDto addPromotionToGame(@PathVariable Long id, @PathVariable Long promotionId) {
        Promotion promotion = promotionService.getPromotionById(promotionId);
        Game updatedGame = gameService.updatePromotion(id, promotion);
        return new GameResponseDto(updatedGame);
    }

    /**
     * Retrieves games based on a specified console type.
     *
     * @param consoleType the console type to filter games by.
     * @return a list of GameResponseDto containing details of games compatible with the specified console type.
     */
    @GetMapping("/games/console/{consoleType}")
    public List<GameResponseDto> getGamesByConsoleType(@PathVariable Game.ConsoleType consoleType) {
        List<Game> games = gameService.getGamesByConsoleType(consoleType);
        return games.stream().map(GameResponseDto::new).collect(Collectors.toList());
    }
    /** 
    * Retrieves the price after applying a promotion for a specific game.
    *
    * @param id the ID of the game.
    * @return the price after applying the promotion.
    */
    @GetMapping("/games/{id}/price-after-promo")
    public ResponseEntity<Integer> getPriceAfterPromo(@PathVariable Long id) {
        int priceAfterPromo = gameService.getPriceAfterPromoWithId(id);
        return ResponseEntity.ok(priceAfterPromo);
    }

    /**
     * Adds a game to a wishlist.
     *
     * @param gameId the ID of the game to add.
     * @param wishlistId the ID of the wishlist to add the game to.
     * @return a GameResponseDto with the updated game details after adding it to the wishlist.
     */
    @PutMapping("/games/{gameId}/wishlist/{wishlistId}")
    public GameResponseDto addGameToWishlist(@PathVariable Long gameId, @PathVariable Long wishlistId) {
        Game game = gameService.addGameToWishlist(gameId, wishlistId);
        return new GameResponseDto(game);
    }

    /**
     * Retrieves games within a specified price range.
     *
     * @param min the minimum price.
     * @param max the maximum price.
     * @return a list of GameResponseDto containing details of games within the specified price range.
     */
    @GetMapping("/games/price")
    public List<GameResponseDto> getGamesBetweenPrices(@RequestParam int min, @RequestParam int max) {
        List<Game> games = gameService.getGamesBetweenPrices(min, max);
        return games.stream().map(GameResponseDto::new).collect(Collectors.toList());
    }

        /**
     * Retrieves specific games associated with a given game ID.
     *
     * @param gameId the ID of the game.
     * @return a list of SpecificGameResponseDto containing details of specific games associated with the game.
     */
    @GetMapping("/games/{gameId}/specific-games")
    public List<SpecificGameResponseDto> getSpecificGamesByGame(@PathVariable Long gameId) {
        List<SpecificGame> specificGames = specificGameService.getSpecificGamesByGame(gameId);
        return specificGames.stream()
                .map(SpecificGameResponseDto::new)
                .collect(Collectors.toList());
        }
}
