package ca.mcgill.ecse321.videogamessystem.controller;

import ca.mcgill.ecse321.videogamessystem.dto.ReviewDto.ReviewRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.ReviewDto.ReviewResponseDto;
import ca.mcgill.ecse321.videogamessystem.model.Review;
import ca.mcgill.ecse321.videogamessystem.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * Create a new review.
     * 
     * @param request the review request DTO containing the review details
     * @return the response DTO with the created review details
     */
    @PostMapping("/reviews")
    public ReviewResponseDto createReview(@Valid @RequestBody ReviewRequestDto request) {
        Review review = reviewService.createReview(
                request.getGameRating(),
                request.getReviewContent(),
                request.getCustomerId(),
                request.getParentReviewId() != null ? request.getParentReviewId() : null,
                request.getGameId()
        );
        return new ReviewResponseDto(review);
    }

    /**
     * Retrieve a review by its ID.
     * 
     * @param id the ID of the review
     * @return the response DTO with the review details
     */
    @GetMapping("/reviews/{id}")
    public ReviewResponseDto getReviewById(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        return new ReviewResponseDto(review);
    }

    /**
     * Retrieve all reviews.
     * 
     * @return a list of response DTOs with the details of all reviews
     */
    @GetMapping("/reviews")
    public List<ReviewResponseDto> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return reviews.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    /**
     * Retrieve reviews by customer ID.
     * 
     * @param customerId the ID of the customer
     * @return a list of response DTOs with the details of the reviews
     */
    @GetMapping("/reviews/customer/{customerId}")
    public List<ReviewResponseDto> getReviewsByCustomer(@PathVariable Long customerId) {
        List<Review> reviews = reviewService.getReviewsByCustomer(customerId);
        return reviews.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    /**
     * Retrieve reviews by parent review ID.
     * 
     * @param parentReviewId the ID of the parent review
     * @return a list of response DTOs with the details of the child reviews
     */
    @GetMapping("/reviews/parent/{parentReviewId}")
    public List<ReviewResponseDto> getReviewsByParentReview(@PathVariable Long parentReviewId) {
        List<Review> reviews = reviewService.getReviewsByParentReview(parentReviewId);
        return reviews.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    @GetMapping("/reviews/game/{gameId}")
    public List<ReviewResponseDto> getReviewByGame(@PathVariable Long gameId){
        List<Review> reviews = reviewService.getReviewsByGame(gameId);
        return reviews.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }
    
    /**
     * Delete a review by its ID.
     * 
     * @param id the ID of the review to delete
     */
    @DeleteMapping("/reviews/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
