package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.Review.ManagerReplyRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Review.ReviewRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Review.ReviewResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Review;
import ca.mcgill.ecse321.gamecenter.service.GameService;
import ca.mcgill.ecse321.gamecenter.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewRestController {
    @Autowired
    ReviewService reviewService;
    @Autowired
    private GameService gameService;

    @PostMapping("/reviews/{gameId}")
    public ResponseEntity<?> createReview(@Validated @RequestBody ReviewRequestDTO req, @PathVariable int gameId) {
        try {
            Game g = gameService.getGameById(gameId);
            return ResponseEntity.ok().body(new ReviewResponseDTO(
                reviewService.createReview(
                        req.getAuthor(),
                        req.getReviewMessage(),
                        req.getRating(),
                        g)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reviews/{gameId}")
    public ResponseEntity<?> getReviewsByGame(@PathVariable int gameId) {
        try {
            return ResponseEntity.ok().body(reviewService.findReviewsByGame(gameId)
                    .stream()
                    .map(ReviewResponseDTO::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reviews/{reviewId}/managerReply")
    public ResponseEntity<?> managerReplyToReview(@Validated @RequestBody ManagerReplyRequestDTO req, @PathVariable int reviewId) {
        try {
            Review r = reviewService.managerReplyToReview(reviewId, req.getReply());
            return ResponseEntity.ok().body(new ReviewResponseDTO(r));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
