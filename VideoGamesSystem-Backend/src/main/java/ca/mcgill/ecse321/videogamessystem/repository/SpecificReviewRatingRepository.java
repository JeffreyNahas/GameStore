package ca.mcgill.ecse321.videogamessystem.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.videogamessystem.model.Review;
import ca.mcgill.ecse321.videogamessystem.model.SpecificReviewRating;

import java.util.List;

public interface SpecificReviewRatingRepository extends CrudRepository<SpecificReviewRating, Long>{
    SpecificReviewRating findSpecificReviewRatingById(Long id);

    List<SpecificReviewRating> findSpecificReviewRatingByReview(Review review);
}
