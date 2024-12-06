package ca.mcgill.ecse321.videogamessystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;
import ca.mcgill.ecse321.videogamessystem.model.Customer;
import ca.mcgill.ecse321.videogamessystem.model.Game;
import ca.mcgill.ecse321.videogamessystem.model.Review;
import ca.mcgill.ecse321.videogamessystem.repository.ReviewRepository;
import ca.mcgill.ecse321.videogamessystem.repository.CustomerRepository;
import ca.mcgill.ecse321.videogamessystem.repository.GameRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GameRepository gameRepository;

    /**
     * @param gameRating
     * @param reviewContent
     * @param customerId
     * @param parentReviewId
     * @return
     */
    @Transactional
    public Review createReview(int gameRating, String reviewContent, Long customerId, Long parentReviewId, Long gameId) {
        if (gameRating < 1 || gameRating > 5) {
            throw new VideoGamesSystemException(HttpStatus.BAD_REQUEST, "Game rating must be between 1 and 5.");
        }
        if (reviewContent == null || reviewContent.trim().isEmpty()) {
            throw new VideoGamesSystemException(HttpStatus.BAD_REQUEST, "Review content cannot be empty.");
        }
    
        Date reviewDate = Date.valueOf(LocalDate.now());
    
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new VideoGamesSystemException(HttpStatus.NOT_FOUND, "Customer not found."));
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new VideoGamesSystemException(HttpStatus.NOT_FOUND, "Game not found."));
    
        Review review = new Review(gameRating, reviewContent, reviewDate);
        review.setCustomer(customer);
        review.setGame(game);
    
        if (parentReviewId != null) {
            Review parentReview = reviewRepository.findReviewById(parentReviewId);
            if (parentReview == null) {
                throw new VideoGamesSystemException(HttpStatus.NOT_FOUND, "Parent review not found.");
            }
            review.setParentReview(parentReview);
        }
    
        return reviewRepository.save(review);
    }
    
    /**
     * @param id
     * @return
     */
    @Transactional
    public Review getReviewById(Long id) {
        Review review = reviewRepository.findReviewById(id);
        if (review == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND, "Review not found.");
        }
        return review;
    }

    // /**
    //  * @param reviewDate
    //  * @return
    //  */
    // @Transactional
    // public List<Review> getReviewsByReviewDate(Date reviewDate) {
    //     return reviewRepository.findReviewByReviewDate(reviewDate);
    // }

    // /**
    //  * @param gameRating
    //  * @return
    //  */
    // @Transactional
    // public List<Review> getReviewsByGameRating(int gameRating) {
    //     return reviewRepository.findReviewByGameRating(gameRating);
    // }


    /**
     * @param parentReviewId
     * @return
     */
    @Transactional
    public List<Review> getReviewsByParentReview(Long parentReviewId) {
        Review parentReview = reviewRepository.findReviewById(parentReviewId);
        if (parentReview == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND, "Parent review not found.");
        }
        return reviewRepository.findReviewByParentReview(parentReview);
    }

    /**
     * @param customerId
     * @return
     */
    @Transactional
    public List<Review> getReviewsByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> 
            new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Customer not found."));
        return reviewRepository.findReviewByCustomer(customer);
    }

    /**
     * @param id
     * @return
     */
    @Transactional
    public Review deleteReview(Long id) {
        Review review = reviewRepository.findReviewById(id);
        if (review == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND, "Review not found.");
        }

        reviewRepository.delete(review);
        return review;
    }

    /**
     * @return
     */
    @Transactional
    public List<Review> getAllReviews() {
        return toList(reviewRepository.findAll());
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


    /**
     * Retrieves reviews associated with a specific game.
     * @param gameId the ID of the game
     * @return a list of reviews for the specified game
     */
    @Transactional
    public List<Review> getReviewsByGame(Long gameId) {
        if (gameId == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND, "Game ID cannot be null.");
        }
        
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new VideoGamesSystemException(HttpStatus.NOT_FOUND, "Game not found."));
        
        return reviewRepository.findReviewByGame(game);
    }

    
}