package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.model.Review;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ReviewServiceTests {
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    GameCategoryRepository gameCategoryRepository;
    @Mock
    GameRepository gameRepository;

    @InjectMocks
    ReviewService reviewService;
    @InjectMocks
    GameCategoryService gameCategoryService;
    @InjectMocks
    GameService gameService;

    @Test
    public void testFindExistingReviewById() {
        String category = "Adventure";
        GameCategory gameCategory = new GameCategory(category);
        when(gameCategoryRepository.save(any(GameCategory.class))).thenReturn(gameCategory);
        GameCategory savedCategory = gameCategoryService.createGameCategory(category);

        String title = "Minecraft";
        float price = 26.99f;
        String description = "block game";
        Game.GeneralFeeling generalFeeling = Game.GeneralFeeling.VERYPOSITIVE;
        Game g = new Game(title, price, description, 0, 0, false, generalFeeling, savedCategory, "");
        when(gameRepository.save(any(Game.class))).thenReturn(g);
        when(gameRepository.findGameById(g.getId())).thenReturn(Optional.of(g));
        Game createdGame = gameService.createGame(title, price, description, generalFeeling, gameCategory, "");

        String author = "awesomesauce3247";
        String reviewContent = "yes yes this good game, i like";
        String managerReply = "";
        Review.Rating stars = Review.Rating.TWO;
        Review r = new Review(author, reviewContent, managerReply, stars, g);

        when(reviewRepository.save(any(Review.class))).thenReturn(r);
        when(reviewRepository.findReviewById(r.getId())).thenReturn(Optional.of(r));
        Review foundReview = reviewService.findReviewById(r.getId());
        assertNotNull(foundReview);
        assertEquals(foundReview.getGame().getId(), createdGame.getId());

        assertEquals(foundReview.getReview(), r.getReview());
        assertEquals(foundReview.getAuthor(), r.getAuthor());
    }

    @Test
    public void testFindNonExistingReviewById() {
        String category = "Adventure";
        GameCategory gameCategory = new GameCategory(category);
        when(gameCategoryRepository.save(any(GameCategory.class))).thenReturn(gameCategory);
        GameCategory savedCategory = gameCategoryService.createGameCategory(category);

        String title = "Minecraft";
        float price = 26.99f;
        String description = "block game";
        Game.GeneralFeeling generalFeeling = Game.GeneralFeeling.VERYPOSITIVE;
        Game g = new Game(title, price, description, 0, 0, false, generalFeeling, savedCategory, "");

        String author = "awesomesauce3247";
        String reviewContent = "yes yes this good game, i like";
        String managerReply = "";
        Review.Rating stars = Review.Rating.TWO;
        Review r = new Review(author, reviewContent, managerReply, stars, g);
        when(reviewRepository.save(any(Review.class))).thenReturn(r);
        when(reviewRepository.findReviewById(r.getId())).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> reviewService.findReviewById(r.getId()));
        assertEquals(e.getMessage(), "No review with id: " + r.getId());
    }

    @Test
    public void testCreateReview() {
        String category = "Adventure";
        GameCategory gameCategory = new GameCategory(category);
        when(gameCategoryRepository.save(any(GameCategory.class))).thenReturn(gameCategory);
        GameCategory savedCategory = gameCategoryService.createGameCategory(category);

        String title = "Minecraft";
        float price = 26.99f;
        String description = "block game";
        Game.GeneralFeeling generalFeeling = Game.GeneralFeeling.VERYPOSITIVE;
        Game g = new Game(title, price, description, 0, 0, false, generalFeeling, savedCategory, "");
        when(gameRepository.save(any(Game.class))).thenReturn(g);
        when(gameRepository.findGameById(g.getId())).thenReturn(Optional.of(g));

        String author = "awesomesauce3247";
        String reviewContent = "yes yes this good game, i like";
        String managerReply = "";
        Review.Rating stars = Review.Rating.TWO;
        Review r = new Review(author, reviewContent, managerReply, stars, g);

        when(reviewRepository.save(any(Review.class))).thenReturn(r);
        Review createdReview = reviewService.createReview(author, reviewContent, stars, g);

        assertNotNull(createdReview);
        assertEquals(createdReview.getId(), r.getId());
    }

    @Test
    public void testFindReviewsByGame() {
        String category = "Adventure";
        GameCategory gameCategory = new GameCategory(category);
        when(gameCategoryRepository.save(any(GameCategory.class))).thenReturn(gameCategory);
        GameCategory savedCategory = gameCategoryService.createGameCategory(category);

        String title = "Minecraft";
        float price = 26.99f;
        String description = "block game";
        Game.GeneralFeeling generalFeeling = Game.GeneralFeeling.VERYPOSITIVE;
        Game g = new Game(title, price, description, 0, 0, false, generalFeeling, savedCategory, "");
        when(gameRepository.save(any(Game.class))).thenReturn(g);
        when(gameRepository.findGameById(g.getId())).thenReturn(Optional.of(g));
        Game createdGame = gameService.createGame(title, price, description, generalFeeling, gameCategory, "");

        String author = "awesomesauce3247";
        String reviewContent = "yes yes this good game, i like";
        String managerReply = "";
        Review.Rating stars = Review.Rating.TWO;
        Review r = new Review(author, reviewContent, managerReply, stars, g);

        when(reviewRepository.save(any(Review.class))).thenReturn(r);
        when(reviewRepository.findReviewById(r.getId())).thenReturn(Optional.of(r));
        Review createdReview = reviewService.createReview(author, reviewContent, stars, g);
        assertNotNull(createdReview);

        when(reviewRepository.findReviewsByGameId(createdGame.getId())).thenReturn(Optional.of(List.of(createdReview)));

        List<Review> foundReviews = reviewService.findReviewsByGame(createdGame.getId());
        assertNotNull(foundReviews);
        assertEquals(foundReviews.size(), 1);
        assertEquals(foundReviews.getFirst().getId(), createdReview.getId());
    }

    @Test
    public void testFindReviewsForGameWithNoReviews() {
        String category = "Adventure";
        GameCategory gameCategory = new GameCategory(category);
        when(gameCategoryRepository.save(any(GameCategory.class))).thenReturn(gameCategory);
        GameCategory savedCategory = gameCategoryService.createGameCategory(category);

        String title = "Minecraft";
        float price = 26.99f;
        String description = "block game";
        Game.GeneralFeeling generalFeeling = Game.GeneralFeeling.VERYPOSITIVE;
        Game g = new Game(title, price, description, 0, 0, false, generalFeeling, savedCategory, "");
        when(gameRepository.save(any(Game.class))).thenReturn(g);
        when(gameRepository.findGameById(g.getId())).thenReturn(Optional.of(g));
        Game createdGame = gameService.createGame(title, price, description, generalFeeling, gameCategory, "");

        when(reviewRepository.findReviewsByGameId(createdGame.getId())).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> reviewService.findReviewsByGame(createdGame.getId()));
        assertEquals(e.getMessage(), "Game with id: " + g.getId() + " has no reviews");
    }

    @Test
    public void testFindReviewsForNonexistentGame() {
        String category = "Adventure";
        GameCategory gameCategory = new GameCategory(category);
        when(gameCategoryRepository.save(any(GameCategory.class))).thenReturn(gameCategory);
        GameCategory savedCategory = gameCategoryService.createGameCategory(category);

        String title = "Minecraft";
        float price = 26.99f;
        String description = "block game";
        Game.GeneralFeeling generalFeeling = Game.GeneralFeeling.VERYPOSITIVE;
        Game g = new Game(title, price, description, 0, 0, false, generalFeeling, savedCategory, "");
        when(gameRepository.save(any(Game.class))).thenReturn(g);
        when(gameRepository.findGameById(g.getId())).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> reviewService.findReviewsByGame(g.getId()));
        assertEquals(e.getMessage(), "No game with id: " + g.getId());
    }
}
