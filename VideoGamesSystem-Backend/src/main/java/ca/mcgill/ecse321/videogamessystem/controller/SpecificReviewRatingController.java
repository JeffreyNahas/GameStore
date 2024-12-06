package ca.mcgill.ecse321.videogamessystem.controller;

import ca.mcgill.ecse321.videogamessystem.dto.SpecificReviewRatingDto.SpecificReviewRatingRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.SpecificReviewRatingDto.SpecificReviewRatingResponseDto;
import ca.mcgill.ecse321.videogamessystem.model.SpecificReviewRating;
import ca.mcgill.ecse321.videogamessystem.service.SpecificReviewRatingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class SpecificReviewRatingController {

    @Autowired
    private SpecificReviewRatingService specificReviewRatingService;

    /**
     * Create a new Specific Review Rating.
     * 
     * @param requestDto SpecificReviewRatingRequestDto containing rating, review, and customer information.
     * @return SpecificReviewRatingResponseDto with the created review rating details.
     */
    @PostMapping("/specificReviewRatings")
    public SpecificReviewRatingResponseDto createSpecificReviewRating(@Valid @RequestBody SpecificReviewRatingRequestDto requestDto) {
        SpecificReviewRating specificReviewRating = specificReviewRatingService.createSpecificReviewRating(
            requestDto.getReviewRating(), requestDto.getReviewId(), requestDto.getCustomerId()
        );
        return new SpecificReviewRatingResponseDto(specificReviewRating);
    }

    /**
     * Get a specific review rating by ID.
     * 
     * @param id the ID of the specific review rating.
     * @return SpecificReviewRatingResponseDto with the requested review rating details.
     */
    @GetMapping("/specificReviewRatings/{id}")
    public SpecificReviewRatingResponseDto getSpecificReviewRatingById(@PathVariable Long id) {
        SpecificReviewRating specificReviewRating = specificReviewRatingService.getSpecificReviewRatingById(id);
        return new SpecificReviewRatingResponseDto(specificReviewRating);
    }

    /**
     * Get all specific review ratings.
     * 
     * @return List of SpecificReviewRatingResponseDto with details of all specific review ratings.
     */
    @GetMapping("/specificReviewRatings")
    public List<SpecificReviewRatingResponseDto> getAllSpecificReviewRatings() {
        List<SpecificReviewRating> specificReviewRatings = specificReviewRatingService.getAllSpecificReviewRatings();
        return specificReviewRatings.stream()
            .map(SpecificReviewRatingResponseDto::new)
            .collect(Collectors.toList());
    }

    /**
     * Get all specific review ratings for a particular review.
     * 
     * @param reviewId the ID of the review.
     * @return List of SpecificReviewRatingResponseDto with details of specific review ratings for the review.
     */
    @GetMapping("/specificReviewRatings/review/{reviewId}")
    public List<SpecificReviewRatingResponseDto> getSpecificReviewRatingsByReview(@PathVariable Long reviewId) {
        List<SpecificReviewRating> specificReviewRatings = specificReviewRatingService.getSpecificReviewRatingsByReview(reviewId);
        return specificReviewRatings.stream()
            .map(SpecificReviewRatingResponseDto::new)
            .collect(Collectors.toList());
    }

    /**
     * Update the rating of a specific review rating.
     * 
     * @param id the ID of the specific review rating to update.
     * @param newReviewRating the new review rating.
     * @return SpecificReviewRatingResponseDto with the updated review rating details.
     */
    @PutMapping("/specificReviewRatings/{id}")
    public SpecificReviewRatingResponseDto updateSpecificReviewRating(
        @PathVariable Long id, @RequestParam SpecificReviewRating.ReviewRating newReviewRating
    ) {
        SpecificReviewRating specificReviewRating = specificReviewRatingService.updateReviewRating(id, newReviewRating);
        return new SpecificReviewRatingResponseDto(specificReviewRating);
    }

    /**
     * Delete a specific review rating by ID.
     * 
     * @param id the ID of the specific review rating to delete.
     * @return SpecificReviewRatingResponseDto with the details of the deleted review rating.
     */
    @DeleteMapping("/specificReviewRatings/{id}")
    public SpecificReviewRatingResponseDto deleteSpecificReviewRating(@PathVariable Long id) {
        SpecificReviewRating specificReviewRating = specificReviewRatingService.deleteSpecificReviewRating(id);
        return new SpecificReviewRatingResponseDto(specificReviewRating);
    }
}
