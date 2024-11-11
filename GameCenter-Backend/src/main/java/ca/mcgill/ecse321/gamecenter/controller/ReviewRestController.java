package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.Review.ManagerReplyRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Review.ReviewRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Review.ReviewResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Review;
import ca.mcgill.ecse321.gamecenter.service.GameService;
import ca.mcgill.ecse321.gamecenter.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReviewRestController {
    @Autowired
    ReviewService reviewService;
    @Autowired
    private GameService gameService;

    @PostMapping("/reviews/{gameId}")
    public ReviewResponseDTO createReview(@Validated @RequestBody ReviewRequestDTO req, @PathVariable int gameId) {
        Game g = gameService.getGameById(gameId);
        return new ReviewResponseDTO(reviewService.createReview(req.getAuthor(), req.getReviewMessage(), req.getRating(), g));
    }

    @GetMapping("/reviews/{gameId}")
    public List<ReviewResponseDTO> getReviewsByGame(@PathVariable int gameId) {
        return reviewService.findReviewsByGame(gameId)
                .stream()
                .map(ReviewResponseDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/reviews/{reviewId}/managerReply")
    public ReviewResponseDTO managerReplyToReview(@Validated @RequestBody ManagerReplyRequestDTO req, @PathVariable int reviewId) {
        Review r = reviewService.managerReplyToReview(reviewId, req.getReply());
        return new ReviewResponseDTO(r);
    }
}
