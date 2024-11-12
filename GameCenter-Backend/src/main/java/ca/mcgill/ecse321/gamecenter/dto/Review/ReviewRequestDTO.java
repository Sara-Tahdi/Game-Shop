package ca.mcgill.ecse321.gamecenter.dto.Review;

import ca.mcgill.ecse321.gamecenter.model.Review;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewRequestDTO {
    @NotBlank(message = "Author is required")
    private String author;
    @NotBlank(message = "Review message is required")
    private String reviewMessage;
    @NotNull(message = "Rating is required")
    private Review.Rating rating;

    public ReviewRequestDTO(String author, String reviewMessage, Review.Rating rating) {
        this.author = author;
        this.reviewMessage = reviewMessage;
        this.rating = rating;
    }

    public String getAuthor() { return author; }
    public String getReviewMessage() { return reviewMessage; }
    public Review.Rating getRating() { return rating; }
}
