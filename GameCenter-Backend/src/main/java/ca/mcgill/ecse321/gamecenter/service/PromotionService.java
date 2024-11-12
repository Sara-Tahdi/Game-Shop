package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Promotion;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private GameRepository gameRepository;


    public List<Promotion> getAllPromotion() {
        List<Promotion> p = promotionRepository.findPromotionByPromotionType(Promotion.class).orElse(null);
        if (p == null) {
            throw new IllegalArgumentException("There are no Promotions");
        }
        return p;
    }

    public Promotion getPromotionById(int id) {
        Promotion p = promotionRepository.findPromotionById(id).orElse(null);
        if (p== null) {
            throw new IllegalArgumentException("There is no Promotion with id: " + id);
        }
        return p;
    }

    public List<Promotion> getPromotionByGameId(int gameId) {
        return promotionRepository.findPromotionsByGameId(gameId).orElse(null);
    }

    @Transactional
    public Promotion createPromotion(Float aNewPrice, LocalDate aStartDate, LocalDate aEndDate, Game aGame) {

        Date sqlStartDate = java.sql.Date.valueOf(aStartDate);
        Date sqlEndDate = java.sql.Date.valueOf(aEndDate);

        if (aNewPrice < 0) {
            throw new IllegalArgumentException("New price is not valid");
        }

        if (!sqlStartDate.before(sqlEndDate)) {
            throw new IllegalArgumentException("Start and End Dates are not valid");
        }

        Game existingGame = gameRepository.findGameById(aGame.getId()).orElse(null);
        if (existingGame == null){
            throw new IllegalArgumentException("Game does not exist in the database");
        }

        Promotion promotion = new Promotion(aNewPrice, sqlStartDate, sqlEndDate, aGame);
        return promotionRepository.save(promotion);
    }

    @Transactional
    public Promotion updatePromotion(Integer id, Float newPrice, LocalDate newStartDate, LocalDate newEndDate, Game newGame) {
        Promotion promotion = promotionRepository.findPromotionById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no Promotion with id: " + id));

        if (newPrice != null) {
            if (newPrice < 0) {
                throw new IllegalArgumentException("Price must be positive");
            }
            promotion.setNewPrice(newPrice);
        }

        LocalDate startDateToUse = newStartDate != null ? newStartDate : promotion.getStartDate().toLocalDate();
        LocalDate endDateToUse = newEndDate != null ? newEndDate : promotion.getEndDate().toLocalDate();

        if (!startDateToUse.isBefore(endDateToUse)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        if (newStartDate != null) {
            promotion.setStartDate(java.sql.Date.valueOf(newStartDate));
        }
        if (newEndDate != null) {
            promotion.setEndDate(java.sql.Date.valueOf(newEndDate));
        }

        if (newGame != null) {
            Game existingGame = gameRepository.findGameById(newGame.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Game not found with id: " + newGame.getId()));
            promotion.setGame(existingGame);
        }

        return promotionRepository.save(promotion);
    }

}
