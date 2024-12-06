package ca.mcgill.ecse321.videogamessystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;
import ca.mcgill.ecse321.videogamessystem.model.Game;
import ca.mcgill.ecse321.videogamessystem.model.SpecificOrder;
import ca.mcgill.ecse321.videogamessystem.model.SpecificGame;
import ca.mcgill.ecse321.videogamessystem.repository.SpecificGameRepository;
import ca.mcgill.ecse321.videogamessystem.repository.SpecificOrderRepository;
import ca.mcgill.ecse321.videogamessystem.repository.GameRepository;

@Service
public class SpecificGameService {

    @Autowired
    private SpecificGameRepository specificGameRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private SpecificOrderRepository specificOrderRepository;


    /**
     * @param serialNumber
     * @param availability
     * @param gameId
     * @return
     */
    @Transactional
    public SpecificGame createSpecificGame(int serialNumber, boolean availability, Long gameId) {
        if (specificGameRepository.findSpecificGameBySerialNumber(serialNumber) != null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND, "A specific game with this serial number already exists.");
        }

        Game game = gameRepository.findById(gameId).orElseThrow(() -> 
            new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Game not found."));

        SpecificGame specificGame = new SpecificGame(serialNumber, availability);
        specificGame.setGame(game);
        return specificGameRepository.save(specificGame);
    }

    // /**
    //  * @param id
    //  * @param stockQuantity
    //  * @return
    //  */
    // @Transactional
    // public void generateSpecificGamesFromStockQuantity(Long GameId, int stockQuantity){
    //     Game game = gameRepository.findGameById(GameId);
    //     if (game == null) {
    //         throw new VideoGamesSystemException(HttpStatus.CONFLICT, "Game not found.");
    //     }
    //         // Validate stock quantity
    //     if (stockQuantity <= 0) {
    //         throw new IllegalArgumentException("Stock quantity must be greater than zero.");
    //     }
    //     Random random = new Random();
    //     for (int i = 1; i <= stockQuantity; i++){
    //         int randomSerialNumber = random.nextInt(Integer.MAX_VALUE);
    //         specificGameRepository.save(createSpecificGame(randomSerialNumber, true, GameId));
    //     }
    // }

        /**
     * @param gameId
     * @param stockQuantity
     * @return
     */
    @Transactional
    public List<SpecificGame> generateSpecificGamesFromStockQuantity(Long gameId, int stockQuantity) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> 
            new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Game not found."));

        if (stockQuantity <= 0) {
            throw new IllegalArgumentException("Stock quantity must be greater than zero.");
        }

        List<SpecificGame> specificGames = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < stockQuantity; i++) {
            int randomSerialNumber = random.nextInt(Integer.MAX_VALUE);
            SpecificGame specificGame = new SpecificGame(randomSerialNumber, true);
            specificGame.setGame(game);
            specificGames.add(specificGameRepository.save(specificGame));
        }

        return specificGames;
    }

    /**
     * @param serialNumber
     * @return
     */
    @Transactional
    public SpecificGame getSpecificGameBySerialNumber(int serialNumber) {
        SpecificGame specificGame = specificGameRepository.findSpecificGameBySerialNumber(serialNumber);
        if (specificGame == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Specific game not found.");
        }
        return specificGame;
    }

    /**
     * @param availability
     * @return
     */
    @Transactional
    public List<SpecificGame> getSpecificGamesByAvailability(boolean availability) {
        return specificGameRepository.findSpecificGameByAvailability(availability);
    }


    /**
     * @param orderNb
     * @return
     */
    @Transactional
    public List<SpecificGame> getSpecificGamesByOrder(Integer orderNb) {
        SpecificOrder order = specificOrderRepository.findOrderByNumber(orderNb);
        if (order == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Order not found.");
        }
        return specificGameRepository.findSpecificGameBySpecificOrder(order);
    }


    /**
     * @param gameId
     * @return
     */
    @Transactional
    public List<SpecificGame> getSpecificGamesByGame(Long gameId) {
        Game game = gameRepository.findGameById(gameId);
        if (game == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Specific game not found.");
        }
        return specificGameRepository.findSpecificGameByGame(game);
    }


    /**
     * @param serialNumber
     * @param newAvailability
     * @return
     */
    @Transactional
    public SpecificGame updateAvailability(int serialNumber, boolean newAvailability) {
        SpecificGame specificGame = specificGameRepository.findSpecificGameBySerialNumber(serialNumber);
        if (specificGame == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Specific game not found.");
        }

        specificGame.setAvailability(newAvailability);
        return specificGameRepository.save(specificGame);
    }

   

    /**
     * @param serialNumber
     * @return
     */
    @Transactional
    public SpecificGame deleteSpecificGame(int serialNumber) {
        SpecificGame specificGame = specificGameRepository.findSpecificGameBySerialNumber(serialNumber);
        if (specificGame == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Specific game not found.");
        }

        specificGameRepository.delete(specificGame);
        return specificGame;
    }

    /**
     * @return
     */
    @Transactional
    public List<SpecificGame> getAllSpecificGames() {
        return toList(specificGameRepository.findAll());
    }

    /**
     * Converts an {@code Iterable} to a {@code List}.
     * @param iterable the {@code Iterable} to convert
     * @param <T>      the type of elements in the iterable
     * @return a {@code List} containing the elements of the {@code Iterable}
     */
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }


    // add specific game to order
    /**
     * @param specificGameID
     * @param order
     * @return
     */
    public List<SpecificGame> addSpecificGameToOrder(int specificGameID, SpecificOrder order) {
        SpecificGame specificGame = specificGameRepository.findSpecificGameBySerialNumber(specificGameID);
        if (specificGame == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"specific game not found");
        }
        if (order == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"order cannot be null");
        }

        // Set the specific order for the game
        specificGame.setSpecificOrder(order);

        // Save the updated specific game with the new order
        specificGameRepository.save(specificGame);

        // Return the list of specific games associated with the order
        return specificGameRepository.findSpecificGameBySpecificOrder(order);
    }


    /**
     * @param gameID
     * @param number_needed
     * @return
     */
    public List<SpecificGame> getSpecificGamesFromGame(Long gameID, int number_needed){
        if (number_needed < 0) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"can only retrieve positive number of games");
        }
        List<SpecificGame> games = this.getSpecificGamesByGame(gameID);
        if (games.size() < number_needed){
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"not enough in stock for game" + gameID);
        }
        int i = 0;
        List <SpecificGame> first_few = new ArrayList<>();
        for (SpecificGame each_game: games){
            if (each_game == null){
                continue;
            }
            first_few.add(each_game);
            i++;
            if(i==number_needed){
                break;
            }
        }
        return first_few;
    }

    /**
     * @param specificGameID
     * @param order
     * @return
     */

    @PersistenceContext
    private EntityManager entityManager;

    public List<SpecificGame> removeSpecificGameFromOrder(int specificGameID, SpecificOrder order) {
        // Retrieve the specific game by serial number
        SpecificGame specificCopy = specificGameRepository.findSpecificGameBySerialNumber(specificGameID);
        if (specificCopy == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"specific game not found");
        }
        if (order == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"order cannot be null");
        }

        // Check if the specific game is indeed associated with the given order
        if (!order.equals(specificCopy.getSpecificOrder())) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"the specific game was not in the order provided.");
        }

        // Unset the order from the specific game and save the change
        specificCopy.setSpecificOrder(null);
        specificGameRepository.save(specificCopy);

        // Flush and refresh entities
        entityManager.flush();
        entityManager.refresh(order);
        
        // Return the updated list of games associated with the order (should no longer
        // include this game)
        return specificGameRepository.findSpecificGameBySpecificOrder(order);
    }

    

}


