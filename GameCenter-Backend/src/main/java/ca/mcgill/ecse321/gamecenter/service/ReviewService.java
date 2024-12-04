package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Review;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private GameRepository gameRepository;

    private int ratingToNumber(Review.Rating rating) {
        switch (rating) {
            case ONE: {return 1;}
            case TWO: {return 2;}
            case THREE: {return 3;}
            case FOUR: {return 4;}
            case FIVE: {return 5;}
            default: {
                return 0;
            }
        }
    }

    public Review findReviewById(int id) {
        Review review = reviewRepository.findReviewById(id).orElse(null);
        if (review == null) {throw new IllegalArgumentException("No review with id: " + id);}
        return review;
    }

    @Transactional
    public Review createReview(
            String author,
            String reviewContent,
            Review.Rating stars,
            Game game)
    {
        Review review = new Review(author, reviewContent, null, stars, game);
        float currRating = game.getRating();
        float numReviews = (float) reviewRepository.findReviewsByGameId(game.getId()).orElse(new ArrayList<>()).size();
        float newRating = ((currRating * numReviews) + ratingToNumber(stars))/(numReviews + 1);
        game.setRating(newRating);
        gameRepository.save(game);
        return reviewRepository.save(review);
    }

    public List<Review> findReviewsByGame(int gameId) {
        Game game = gameRepository.findGameById(gameId).orElse(null);
        if (game == null) {throw new IllegalArgumentException("No game with id: " + gameId);}

        List<Review> reviews = reviewRepository.findReviewsByGameId(gameId).orElse(null);
        if (reviews == null) {throw new IllegalArgumentException("Game with id: " + gameId + " has no reviews");}
        return reviews;
    }

    public Review managerReplyToReview(int reviewId, String reply) {
        Review r = reviewRepository.findReviewById(reviewId).orElse(null);
        if (r == null) {throw new IllegalArgumentException("No review with id: " + reviewId);}
        r.setManagerReply(reply);
        return reviewRepository.save(r);
    }
}
