package ca.mcgill.ecse321.videogamessystem.dto.SpecificReviewRatingDto;

import ca.mcgill.ecse321.videogamessystem.model.SpecificReviewRating.ReviewRating;

import jakarta.validation.constraints.NotNull;

public class SpecificReviewRatingRequestDto {

    @NotNull(message = "Review rating cannot be null.")
    private ReviewRating reviewRating;

    // @NotNull(message = "Review cannot be null.")
    // private Review review;

    @NotNull(message = "Review ID cannot be null.")
    private Long reviewId;

    // @NotNull(message = "Customer cannot be null.")
    // private Customer customer;

    @NotNull(message = "Customer ID cannot be null.")
    private Long customerId;

    // @NotNull(message = "Customer username cannot be null.")
    // private String customerUserName;

    // Constructor with all attributes
    public SpecificReviewRatingRequestDto(ReviewRating reviewRating, Long reviewId, Long customerId) {
        this.reviewRating = reviewRating;
        // this.review = review;
        this.reviewId = reviewId;
        // this.customer = customer;
        this.customerId = customerId;
        // this.customerUserName = customerUserName;
    }

    // Getters and Setters
    public ReviewRating getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(ReviewRating reviewRating) {
        this.reviewRating = reviewRating;
    }

    // public Review getReview() {
    //     return review;
    // }

    // public void setReview(Review review) {
    //     this.review = review;
    // }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    // public Customer getCustomer() {
    //     return customer;
    // }

    // public void setCustomer(Customer customer) {
    //     this.customer = customer;
    // }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    // public String getCustomerUserName() {
    //     return customerUserName;
    // }

    // public void setCustomerUserName(String customerUserName) {
    //     this.customerUserName = customerUserName;
    // }
}