package ca.mcgill.ecse321.gamecenter;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.gamecenter.model.Review;
import ca.mcgill.ecse321.gamecenter.model.Review.Rating;
import ca.mcgill.ecse321.gamecenter.repository.ReviewRepository;

@SpringBootTest
public class ReviewTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        reviewRepository.deleteAll();
    }

    // Test for reading a Review by ID
    @Test
    public void testGetReviewById() {
        String author = "Jane Doe";
        String reviewText = "Not bad!";
        Rating stars = Rating.THREE;

        Review review = new Review();
        review.setAuthor(author);
        review.setReview(reviewText);
        review.setStars(stars);

        review = reviewRepository.save(review);

        Review savedReview = reviewRepository.findReviewById(review.getId()).orElse(null);
        assertNotNull(savedReview);
        assertEquals(savedReview.getAuthor(), author);
        assertEquals(savedReview.getReview(), reviewText);
        assertEquals(savedReview.getStars(), stars);
    }

    // Test for updating a Review
    @Test
    public void testUpdateReview() {
        String author = "Alice";
        String reviewText = "Could be better.";
        Rating stars = Rating.TWO;

        Review review = new Review();
        review.setAuthor(author);
        review.setReview(reviewText);
        review.setStars(stars);

        review = reviewRepository.save(review);

        // Update the review
        String newReviewText = "Actually, it's growing on me.";
        Rating newStars = Rating.FOUR;

        review.setReview(newReviewText);
        review.setStars(newStars);
        review = reviewRepository.save(review);

        Review updatedReview = reviewRepository.findReviewById(review.getId()).orElse(null);
        assertNotNull(updatedReview);
        assertEquals(updatedReview.getReview(), newReviewText);
        assertEquals(updatedReview.getStars(), newStars);
    }
}
