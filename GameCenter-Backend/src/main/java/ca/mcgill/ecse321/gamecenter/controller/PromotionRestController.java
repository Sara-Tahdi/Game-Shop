package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.Promotion.PromotionRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Promotion.PromotionResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Promotion;
import ca.mcgill.ecse321.gamecenter.service.GameService;
import ca.mcgill.ecse321.gamecenter.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PromotionRestController {

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private GameService gameService;

    @PostMapping("/promotions/create")
    public PromotionResponseDTO createPromotion(@Validated @RequestBody PromotionRequestDTO promotionToCreate) {
        Game game = gameService.getGameById(promotionToCreate.getGameId());
        Promotion promotion = promotionService.createPromotion(
                promotionToCreate.getNewPrice(),
                promotionToCreate.getStartDate(),
                promotionToCreate.getEndDate(),
                game
        );
        return new PromotionResponseDTO(promotion);
    }

    @GetMapping("/promotions/{id}")
    public PromotionResponseDTO getPromotionById(@PathVariable int id) {
        Promotion promotion = promotionService.getPromotionById(id);
        return new PromotionResponseDTO(promotion);
    }
    @GetMapping(value = "/promotions")
    public List<PromotionResponseDTO> getPromotions() {
        List<Promotion> promotions = promotionService.getAllPromotion();
        return promotions.stream()
                .map(PromotionResponseDTO::new)
                .collect(Collectors.toList());
    }


    @GetMapping("/promotions/game/{gameId}")
    public List<PromotionResponseDTO> getPromotionsByGameId(@PathVariable int gameId) {
        List<Promotion> promotions = promotionService.getPromotionByGameId(gameId);
        return promotions.stream()
                .map(PromotionResponseDTO::new)
                .collect(Collectors.toList());
    }

    @PutMapping("/promotions/update/{id}")
    public PromotionResponseDTO updatePromotion(
            @PathVariable int id,
            @RequestParam(required = false) Float newPrice,
            @RequestParam(required = false) LocalDate newStartDate,
            @RequestParam(required = false) LocalDate newEndDate,
            @RequestParam(required = false) Integer newGameId) {

        Promotion existingPromotion = promotionService.getPromotionById(id);

        Float priceToUpdate = newPrice != null ? newPrice : existingPromotion.getNewPrice();
        LocalDate startDateToUpdate = newStartDate != null ? newStartDate : existingPromotion.getStartDate().toLocalDate();
        LocalDate endDateToUpdate = newEndDate != null ? newEndDate : existingPromotion.getEndDate().toLocalDate();
        Game gameToUpdate = newGameId != null ? gameService.getGameById(newGameId) : existingPromotion.getGame();

        Promotion promotion = promotionService.updatePromotion(
                id,
                priceToUpdate,
                startDateToUpdate,
                endDateToUpdate,
                gameToUpdate
        );
        return new PromotionResponseDTO(promotion);
    }
}