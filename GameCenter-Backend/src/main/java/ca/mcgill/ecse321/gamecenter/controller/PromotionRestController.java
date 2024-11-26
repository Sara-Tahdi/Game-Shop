package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.Promotion.PromotionRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Promotion.PromotionResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Promotion;
import ca.mcgill.ecse321.gamecenter.service.GameService;
import ca.mcgill.ecse321.gamecenter.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PromotionRestController {

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private GameService gameService;

    @PostMapping("/promotions/create")
    public ResponseEntity<?> createPromotion(@Validated @RequestBody PromotionRequestDTO promotionToCreate) {
        try {
            Game game = gameService.getGameById(promotionToCreate.getGameId());
            Promotion promotion = promotionService.createPromotion(
                    promotionToCreate.getNewPrice(),
                    promotionToCreate.getStartDate(),
                    promotionToCreate.getEndDate(),
                    game
            );
            return ResponseEntity.ok().body(new PromotionResponseDTO(promotion));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/promotions/{id}")
    public ResponseEntity<?> getPromotionById(@PathVariable int id) {
        try {
            Promotion promotion = promotionService.getPromotionById(id);
            return ResponseEntity.ok().body(new PromotionResponseDTO(promotion));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping(value = "/promotions")
    public ResponseEntity<?> getPromotions() {
        try {
            List<Promotion> promotions = promotionService.getAllPromotion();
            return ResponseEntity.ok().body(promotions.stream()
                    .map(PromotionResponseDTO::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/promotions/game/{gameId}")
    public ResponseEntity<?> getPromotionsByGameId(@PathVariable int gameId) {
        try {
            List<Promotion> promotions = promotionService.getPromotionByGameId(gameId);
            return ResponseEntity.ok().body(promotions.stream()
                    .map(PromotionResponseDTO::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/promotions/update/{id}")
    public ResponseEntity<?> updatePromotion(@Validated @RequestBody PromotionRequestDTO promotionToUpdate, @PathVariable int id) {
        try {
            Game game = gameService.getGameById(promotionToUpdate.getGameId());
            Promotion promotion = promotionService.updatePromotion(
                    id,
                    promotionToUpdate.getNewPrice(),
                    promotionToUpdate.getStartDate(),
                    promotionToUpdate.getEndDate(),
                    game
            );
            return ResponseEntity.ok().body(new PromotionResponseDTO(promotion));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
