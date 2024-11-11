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
    public PromotionResponseDTO updatePromotion(@Validated @RequestBody PromotionRequestDTO promotionToUpdate, @PathVariable int id) {
        Game game = gameService.getGameById(promotionToUpdate.getGameId());
        Promotion promotion = promotionService.updatePromotion(
                id,
                promotionToUpdate.getNewPrice(),
                promotionToUpdate.getStartDate(),
                promotionToUpdate.getEndDate(),
                game
        );
        return new PromotionResponseDTO(promotion);
    }
}