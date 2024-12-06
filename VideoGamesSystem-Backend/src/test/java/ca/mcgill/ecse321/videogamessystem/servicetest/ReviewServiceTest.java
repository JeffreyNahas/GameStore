package ca.mcgill.ecse321.videogamessystem.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;
import ca.mcgill.ecse321.videogamessystem.model.Customer;
import ca.mcgill.ecse321.videogamessystem.model.Game;
import ca.mcgill.ecse321.videogamessystem.model.Review;
import ca.mcgill.ecse321.videogamessystem.repository.CustomerRepository;
import ca.mcgill.ecse321.videogamessystem.repository.GameRepository;
import ca.mcgill.ecse321.videogamessystem.repository.ReviewRepository;
import ca.mcgill.ecse321.videogamessystem.service.ReviewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;
    private Customer customer;
    private Game game;

    @BeforeEach
    public void setup() {
        customer = new Customer();
        game = new Game();
        review = new Review(5, "Amazing game!", Date.valueOf(LocalDate.now()));
        review.setCustomer(customer);
        review.setGame(game);
    }

    @Test
    public void testCreateReview_Success() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review createdReview = reviewService.createReview(5, "Amazing game!", 1L, null, 1L);

        assertNotNull(createdReview);
        assertEquals(5, createdReview.getGameRating());
        assertEquals("Amazing game!", createdReview.getReviewContent());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void testCreateReview_InvalidRating() {
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            reviewService.createReview(6, "Amazing game!", 1L, null, 1L);
        });
        assertEquals("Game rating must be between 1 and 5.", exception.getMessage());
    }

    @Test
    public void testCreateReview_InvalidCustomer() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            reviewService.createReview(5, "Amazing game!", 1L, null, 1L);
        });
        assertEquals("Customer not found.", exception.getMessage());
    }

    @Test
    public void testCreateReview_InvalidGame() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(gameRepository.findById(anyLong())).thenReturn(Optional.empty());
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            reviewService.createReview(5, "Amazing game!", 1L, null, 1L);
        });
        assertEquals("Game not found.", exception.getMessage());
    }

    @Test
    public void testCreateReview_WithParentReview() {
        // Arrange
        Review parentReview = new Review(4, "Parent review", Date.valueOf(LocalDate.now()));
        Customer customer = new Customer();
        Game game = new Game();
    
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        when(reviewRepository.findReviewById(anyLong())).thenReturn(parentReview);
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));
    
        // Act
        Review createdReview = reviewService.createReview(5, "Child review!", 1L, 2L, 3L);
    
        // Assert
        assertNotNull(createdReview);
        assertEquals(parentReview, createdReview.getParentReview());
        assertEquals("Child review!", createdReview.getReviewContent());
        assertEquals(5, createdReview.getGameRating());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }
    

    @Test
    public void testGetReviewById_Found() {
        when(reviewRepository.findReviewById(anyLong())).thenReturn(review);

        Review foundReview = reviewService.getReviewById(1L);

        assertNotNull(foundReview);
        assertEquals(5, foundReview.getGameRating());
        assertEquals("Amazing game!", foundReview.getReviewContent());
    }

    @Test
    public void testGetReviewById_NotFound() {
        when(reviewRepository.findReviewById(anyLong())).thenReturn(null);

        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            reviewService.getReviewById(1L);
        });
        assertEquals("Review not found.", exception.getMessage());
    }

    @Test
    public void testGetAllReviews_WithMultipleReviews() {
        Review review1 = new Review(4, "Review 1", Date.valueOf(LocalDate.now()));
        Review review2 = new Review(5, "Review 2", Date.valueOf(LocalDate.now()));
        when(reviewRepository.findAll()).thenReturn(List.of(review1, review2));

        List<Review> reviews = reviewService.getAllReviews();

        assertNotNull(reviews);
        assertEquals(2, reviews.size());
    }

    @Test
    public void testGetAllReviews_EmptyList() {
        when(reviewRepository.findAll()).thenReturn(Collections.emptyList());

        List<Review> reviews = reviewService.getAllReviews();

        assertNotNull(reviews);
        assertTrue(reviews.isEmpty());
    }

    @Test
    public void testGetReviewsByGame_Success() {
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        when(reviewRepository.findReviewByGame(any(Game.class))).thenReturn(List.of(review));

        List<Review> reviews = reviewService.getReviewsByGame(1L);

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals("Amazing game!", reviews.get(0).getReviewContent());
    }

    @Test
    public void testGetReviewsByGame_NotFound() {
        when(gameRepository.findById(anyLong())).thenReturn(Optional.empty());

        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            reviewService.getReviewsByGame(1L);
        });
        assertEquals("Game not found.", exception.getMessage());
    }

    @Test
    public void testGetReviewsByCustomer_Success() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(reviewRepository.findReviewByCustomer(any(Customer.class))).thenReturn(List.of(review));

        List<Review> reviews = reviewService.getReviewsByCustomer(1L);

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals("Amazing game!", reviews.get(0).getReviewContent());
    }

    @Test
    public void testGetReviewsByCustomer_NotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            reviewService.getReviewsByCustomer(1L);
        });
        assertEquals("Customer not found.", exception.getMessage());
    }

    @Test
    public void testDeleteReview_Success() {
        when(reviewRepository.findReviewById(anyLong())).thenReturn(review);
        doNothing().when(reviewRepository).delete(any(Review.class));

        Review deletedReview = reviewService.deleteReview(1L);

        assertNotNull(deletedReview);
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    public void testDeleteReview_NotFound() {
        when(reviewRepository.findReviewById(anyLong())).thenReturn(null);

        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            reviewService.deleteReview(1L);
        });
        assertEquals("Review not found.", exception.getMessage());
    }
}
