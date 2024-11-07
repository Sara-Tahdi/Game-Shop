package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.Promotion.PromotionRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Promotion.PromotionResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Promotion;
import ca.mcgill.ecse321.gamecenter.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/promotions")
public class PromotionRestController {

    @Autowired
    private PromotionService promotionService;

    @PostMapping("/promotions/create")
    public PromotionResponseDTO createPromotion(@Validated @RequestBody PromotionRequestDTO promotionToCreate) {
        Promotion promotion = promotionService.createPromotion(
                promotionToCreate.getNewPrice(),
                promotionToCreate.getStartDate(),
                promotionToCreate.getEndDate(),
                promotionToCreate.getGame()
        );
        return new PromotionResponseDTO(promotion);
    }

    @GetMapping("/promotions/{id}")
    public PromotionResponseDTO getPromotionById(@PathVariable int id) {
        Promotion promotion = promotionService.getPromotionById(id);
        return new PromotionResponseDTO(promotion);
    }

    @GetMapping("/game/{gameId}")
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
            @RequestParam(required = false) java.sql.Date newStartDate,
            @RequestParam(required = false) java.sql.Date newEndDate,
            @RequestParam(required = false) Game newGame) {

        Promotion promotion = promotionService.updatePromotion(id, newPrice, newStartDate, newEndDate, newGame);
        return new PromotionResponseDTO(promotion);
    }
}