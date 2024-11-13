package ca.mcgill.ecse321.gamecenter.dto.Review;

import ca.mcgill.ecse321.gamecenter.dto.Game.GameResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Review;

public class ReviewResponseDTO {
    private int id;
    private String author;
    private String reviewMessage;
    private String managerReply;
    private Review.Rating rating;
    private GameResponseDTO game;

    public ReviewResponseDTO() {}

    public ReviewResponseDTO(Review r) {
        this.id = r.getId();
        this.author = r.getAuthor();
        this.reviewMessage = r.getReview();
        this.managerReply = r.getManagerReply();
        this.rating = r.getStars();
        this.game = new GameResponseDTO(r.getGame());
    }

    public int getId() { return this.id; }
    public String getAuthor() { return this.author; }
    public String getReviewMessage() { return this.reviewMessage; }
    public String getManagerReply() { return this.managerReply; }
    public Review.Rating getRating() { return this.rating; }
    public GameResponseDTO getGame() { return this.game; }
}
