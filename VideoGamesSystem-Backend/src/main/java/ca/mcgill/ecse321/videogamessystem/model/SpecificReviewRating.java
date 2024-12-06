package ca.mcgill.ecse321.videogamessystem.model;

import jakarta.persistence.Id;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

// line 102 "model.ump"
// line 158 "model.ump"
@Entity
public class SpecificReviewRating {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  // SpecificReviewRating Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ReviewRating reviewRating;

  // SpecificReviewRating Associations
  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Review review;
  
  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Customer customer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SpecificReviewRating(ReviewRating aReviewRating) {
    reviewRating = aReviewRating;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public SpecificReviewRating(){}

  public boolean setReviewRating(ReviewRating aReviewRating) {
    boolean wasSet = false;
    reviewRating = aReviewRating;
    wasSet = true;
    return wasSet;
  }

  public ReviewRating getReviewRating() {
    return reviewRating;
  }

  public Long getId(){
    return id;
  }

  public Review getReview() {
    return review;
  }

  public Customer getCustomer() {
    return customer;
  }

  public boolean setReview(Review aNewReview) {
    boolean wasSet = false;
    if (aNewReview != null) {
      review = aNewReview;
      wasSet = true;
    }
    return wasSet;
  }

  public boolean setCustomer(Customer aNewCustomer) {
    boolean wasSet = false;
    if (aNewCustomer != null) {
      customer = aNewCustomer;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete() {
    review = null;
    customer = null;
  }

  @Override
  public String toString() {
    return "SpecificReviewRating{" +
           "id=" + id +
           ", reviewRating=" + reviewRating +
           ", review=" + (review != null ? Integer.toHexString(System.identityHashCode(review)) : "null") +
           ", customer=" + (customer != null ? Integer.toHexString(System.identityHashCode(customer)) : "null") +
           '}';
  }
  //Enumeration
  public enum ReviewRating {
    Like,
    Dislike;

    @Override
    public String toString() {
      return name();
    }
  }
}