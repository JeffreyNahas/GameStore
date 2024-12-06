package ca.mcgill.ecse321.videogamessystem.dto.ReviewDto;

import ca.mcgill.ecse321.videogamessystem.model.Customer;
import ca.mcgill.ecse321.videogamessystem.model.Game;
import ca.mcgill.ecse321.videogamessystem.model.Review;
import java.sql.Date;

public class ReviewResponseDto {

    private Long id;
    private int gameRating;
    private String reviewContent;
    private Date reviewDate;
    private Customer customer;
    private String customerUsername;
    private Game game;
    private String gameTitle;
    private Review parentReview;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.gameRating = review.getGameRating();
        this.reviewContent = review.getReviewContent();
        this.reviewDate = review.getReviewDate();
        this.customer = review.getCustomer();
        this.customerUsername = review.getCustomer() != null ? review.getCustomer().getUserName() : null;
        this.game = review.getGame();
        this.gameTitle = review.getGame() != null ? review.getGame().getTitle() : null;
        this.parentReview = review.getParentReview();
    }

    protected ReviewResponseDto() {}

    // Getters
    public Long getId() {
        return id;
    }

    public int getGameRating() {
        return gameRating;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public Game getGame() {
        return game;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public Review getParentReview() {
        return parentReview;
    }

    public Long getParentReviewId() {
        return parentReview != null ? parentReview.getId() : null;
    }
}
