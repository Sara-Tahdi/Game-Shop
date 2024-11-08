package ca.mcgill.ecse321.gamecenter.dto.Review;

import ca.mcgill.ecse321.gamecenter.model.Review;
import jakarta.validation.constraints.NotBlank;

public class ReviewRequestDTO {
    @NotBlank(message = "Author is required")
    private String author;
    @NotBlank(message = "Review message is required")
    private String reviewMessage;
    @NotBlank(message = "Rating is required")
    private Review.Rating rating;
    @NotBlank(message = "Game is required")
    private int gameId;

    public ReviewRequestDTO(String author, String reviewMessage, Review.Rating rating, int gameId) {
        this.author = author;
        this.reviewMessage = reviewMessage;
        this.rating = rating;
        this.gameId = gameId;
    }

    public String getAuthor() { return author; }
    public String getReviewMessage() { return reviewMessage; }
    public Review.Rating getRating() { return rating; }
    public int getGameId() { return gameId; }
}
