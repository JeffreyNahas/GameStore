package ca.mcgill.ecse321.videogamessystem.dto.ReviewDto;

import java.sql.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class ReviewRequestDto {

    @NotBlank(message = "Review content cannot be blank")
    private String reviewContent;

    @NotNull(message = "Game rating cannot be null")
    @Min(value = 1, message = "Game rating must be at least 1")
    @Max(value = 5, message = "Game rating must be no more than 5")
    private int gameRating;

    @NotNull(message = "Review date cannot be null")
    private Date reviewDate;

    @NotNull(message = "Customer ID cannot be null")
    @Positive(message = "Customer ID must be positive")
    private Long customerId;

    private Long parentReviewId;

    @NotNull(message = "Game ID cannot be null")
    @Positive(message = "Game ID must be positive")
    private Long gameId;

    // Constructor with all attributes
    public ReviewRequestDto(String reviewContent, int gameRating, Date reviewDate, Long customerId, Long parentReviewId, Long gameId) {
        this.reviewContent = reviewContent;
        this.gameRating = gameRating;
        this.reviewDate = reviewDate;
        this.customerId = customerId;
        this.parentReviewId = parentReviewId;
        this.gameId = gameId;
    }
    // Getters and Setters
    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public int getGameRating() {
        return gameRating;
    }


    public void setGameRating(int gameRating) {
        this.gameRating = gameRating;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getParentReviewId() {
        return parentReviewId;
    }

    public void setParentReviewId(Long parentReviewId) {
        this.parentReviewId = parentReviewId;
    }
    

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }


    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

}
