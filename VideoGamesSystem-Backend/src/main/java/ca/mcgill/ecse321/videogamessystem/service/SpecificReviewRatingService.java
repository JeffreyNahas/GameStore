package ca.mcgill.ecse321.videogamessystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.ArrayList;

import ca.mcgill.ecse321.videogamessystem.model.Review;
import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;
import ca.mcgill.ecse321.videogamessystem.model.Customer;
import ca.mcgill.ecse321.videogamessystem.model.SpecificReviewRating;
import ca.mcgill.ecse321.videogamessystem.model.SpecificReviewRating.ReviewRating;
import ca.mcgill.ecse321.videogamessystem.repository.SpecificReviewRatingRepository;
import ca.mcgill.ecse321.videogamessystem.repository.ReviewRepository;
import ca.mcgill.ecse321.videogamessystem.repository.CustomerRepository;

@Service
public class SpecificReviewRatingService {

    @Autowired
    private SpecificReviewRatingRepository specificReviewRatingRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * @param reviewRating
     * @param reviewId
     * @param customerId
     * @return
     */
    @Transactional
    public SpecificReviewRating createSpecificReviewRating(ReviewRating reviewRating, Long reviewId, Long customerId) {
        if (reviewRating == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Review rating cannot be null.");
        }

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> 
            new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Review not found."));
        
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> 
            new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Customer not found."));

        SpecificReviewRating specificReviewRating = new SpecificReviewRating(reviewRating);
        specificReviewRating.setReview(review);
        specificReviewRating.setCustomer(customer);

        return specificReviewRatingRepository.save(specificReviewRating);
    }

    /**
     * @param id
     * @return
     */
    @Transactional
    public SpecificReviewRating getSpecificReviewRatingById(Long id) {
        SpecificReviewRating specificReviewRating = specificReviewRatingRepository.findSpecificReviewRatingById(id);
        if (specificReviewRating == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Specific review rating not found.");
        }
        return specificReviewRating;
    }

    /**
     * @param reviewId
     * @return
     */
    @Transactional
    public List<SpecificReviewRating> getSpecificReviewRatingsByReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> 
            new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Review not found."));
        return specificReviewRatingRepository.findSpecificReviewRatingByReview(review);
    }

    /**
     * @param id
     * @param newReviewRating
     * @return
     */
    @Transactional
    public SpecificReviewRating updateReviewRating(Long id, ReviewRating newReviewRating) {
        SpecificReviewRating specificReviewRating = specificReviewRatingRepository.findSpecificReviewRatingById(id);
        if (specificReviewRating == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Specific review rating not found.");
        }
        if (newReviewRating == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"New review rating cannot be null.");
        }

        specificReviewRating.setReviewRating(newReviewRating);
        return specificReviewRatingRepository.save(specificReviewRating);
    }

    /**
     * @param id
     * @return
     */
    @Transactional
    public SpecificReviewRating deleteSpecificReviewRating(Long id) {
        SpecificReviewRating specificReviewRating = specificReviewRatingRepository.findSpecificReviewRatingById(id);
        if (specificReviewRating == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Specific review rating not found.");
        }

        specificReviewRatingRepository.delete(specificReviewRating);
        return specificReviewRating;
    }

    /**
     * @return
     */
    @Transactional
    public List<SpecificReviewRating> getAllSpecificReviewRatings() {
        return toList(specificReviewRatingRepository.findAll());
    }
    // GET ALL LIKES AND DISLIKES INSTEAD OF ALL 

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
}

