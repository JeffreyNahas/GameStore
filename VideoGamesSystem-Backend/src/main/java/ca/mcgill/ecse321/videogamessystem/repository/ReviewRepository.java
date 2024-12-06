package ca.mcgill.ecse321.videogamessystem.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.videogamessystem.model.Customer;
import ca.mcgill.ecse321.videogamessystem.model.Game;
import ca.mcgill.ecse321.videogamessystem.model.Review;


public interface ReviewRepository extends CrudRepository<Review, Long>{

    Review findReviewById(Long id);

    List<Review> findReviewByParentReview(Review parentReview);

    List<Review> findReviewByCustomer(Customer customer);

    List<Review> findReviewByGame(Game game);
}

