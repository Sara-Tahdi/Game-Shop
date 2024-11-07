package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Review;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private GameRepository gameRepository;

    public Review findReviewById(int id) {
        Review review = reviewRepository.findReviewById(id).orElse(null);
        if (review == null) {throw new IllegalArgumentException("No review with id: " + id);}
        return review;
    }

    @Transactional
    public Review createReview(
            String author,
            String reviewContent,
            String managerReply,
            Review.Rating stars,
            Game game)
    {
        // TODO: Change the umple code so that it doesn't take thumbs up/down in the review constructor
        Review review = new Review(author, reviewContent, managerReply, stars, game);
        return reviewRepository.save(review);
    }

    public List<Review> findReviewsByGame(int gameId) {
        Game game = gameRepository.findGameById(gameId).orElse(null);
        if (game == null) {throw new IllegalArgumentException("No game with id: " + gameId);}

        List<Review> reviews = reviewRepository.findReviewsByGameId(gameId).orElse(null);
        if (reviews == null) {throw new IllegalArgumentException("Game with id: " + gameId + " has no reviews");}
        return reviews;
    }
}
