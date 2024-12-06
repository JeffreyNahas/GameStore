package ca.mcgill.ecse321.videogamessystem.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;
import ca.mcgill.ecse321.videogamessystem.model.Game;
import ca.mcgill.ecse321.videogamessystem.model.Promotion;
import ca.mcgill.ecse321.videogamessystem.repository.GameRepository;
import ca.mcgill.ecse321.videogamessystem.repository.PromotionRepository;
import jakarta.transaction.Transactional;

@Service
public class PromotionService {
    
    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private GameRepository gameRepository;

    /**
     * Creates a new promotion.
     * 
     * @param percentage the discount percentage for the promotion
     * @param startDate the start date of the promotion
     * @param endDate the end date of the promotion
     * @return the created promotion
     */
    @Transactional
    public Promotion createPromotion(int percentage, Date startDate, Date endDate) {
        // Date now = Date.valueOf(LocalDate.now());
        if (endDate == null || startDate == null) {
            throw new VideoGamesSystemException(HttpStatus.BAD_REQUEST, "Start and end dates must be provided.");
        }
        // if (startDate.before(now) || endDate.before(now)) {
        //     throw new VideoGamesSystemException(HttpStatus.CONFLICT, "Dates must be today or in the future.");
        // }
        if (startDate.after(endDate)) {
            throw new VideoGamesSystemException(HttpStatus.CONFLICT, "Start date must be before end date.");
        }
        

        if (percentage <= 0 || percentage > 100) {
            throw new VideoGamesSystemException(HttpStatus.CONFLICT, "Percentage must be between 1 and 100.");
        }

        Promotion promotion = new Promotion(percentage, startDate, endDate);
        return promotionRepository.save(promotion);
    }

    /**
     * Retrieves a promotion by its ID.
     * 
     * @param id the ID of the promotion
     * @return the promotion with the specified ID
     */
    @Transactional
    public Promotion getPromotionById(Long id) {
        Promotion promotion = this.promotionRepository.findPromotionById(id);
        if (promotion == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND, "Promotion not found.");
        }
        return promotion;
    }

    /**
     * Retrieves all promotions.
     * 
     * @return a list of all promotions
     */
    @Transactional
    public List<Promotion> getAllPromotion() {
        return toList(this.promotionRepository.findAll());
    }

    /**
     * Deletes a promotion by its ID and removes its association with any games.
     * 
     * @param id the ID of the promotion to delete
     * @return the deleted promotion
     */
    @Transactional
    public Promotion deletePromotion(Long id) {
        Promotion existingPromotion = this.getPromotionById(id);
        List<Game> games = gameRepository.findGameByPromotion(existingPromotion);
        for (Game game : games) {
            game.setPromotion(null);
            gameRepository.save(game);
        }
        promotionRepository.delete(existingPromotion);
        return existingPromotion;
    }

    /**
     * Checks if a promotion is currently active.
     * 
     * @param id the ID of the promotion
     * @return true if the promotion is active, false otherwise
     */
    public boolean promotionAvailable(Long id) {
        Promotion promotion = promotionRepository.findPromotionById(id);
        if (promotion == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND, "Promotion not found.");
        }

        Date endDate = promotion.getEndDate();
        Date startDate = promotion.getStartDate();
        Date now = Date.valueOf(LocalDate.now());

        return (startDate.before(now) && endDate.after(now)) || startDate.equals(now) || endDate.equals(now);
    }

    /**
     * Converts an {@code Iterable} to a {@code List}.
     * 
     * @param iterable the {@code Iterable} to convert
     * @param <T> the type of elements in the iterable
     * @return a {@code List} containing the elements of the {@code Iterable}
     */
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
}
