package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Promotion;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
        List<Promotion> promotions = promotionRepository.findPromotionsByGameId(gameId).orElse(null);
        if (promotions == null || promotions.isEmpty()) {
            throw new IllegalArgumentException("There is no Promotion for Game with id: " + gameId);
        }
        return promotions;
    }

    @Transactional
    public Promotion createPromotion(Float aNewPrice, Date aStartDate, Date aEndDate, Game aGame) {
        if (aNewPrice < 0) {
            throw new IllegalArgumentException("New price is not valid");
        }

        if (!aStartDate.before(aEndDate)) {
            throw new IllegalArgumentException("Start and End Dates are not valid");
        }

        Game existingGame = gameRepository.findGameById(aGame.getId()).orElse(null);
        if (existingGame == null){
            throw new IllegalArgumentException("Game does not exist in the database");
        }

        Promotion promotion = new Promotion(aNewPrice, aStartDate, aEndDate, aGame);
        return promotionRepository.save(promotion);
    }

    @Transactional
    public Promotion updatePromotion(Integer oldId, Float newPrice, Date newStartDate, Date newEndDate, Game newGame) {
        Promotion promotion = promotionRepository.findPromotionById(oldId).orElse(null);
        if (promotion == null) {
            throw new IllegalArgumentException("There is no Promotion with id: " + oldId);
        }

        if (newPrice != null) {
            if (newPrice < 0) {
                throw new IllegalArgumentException("Price is not valid");
            }
            promotion.setNewPrice(newPrice);
        }

        if (newStartDate != null || newEndDate != null) {
            Date startDate = newStartDate != null ? newStartDate : promotion.getStartDate();
            Date endDate = newEndDate != null ? newEndDate : promotion.getEndDate();

            if (!startDate.before(endDate)) {
                throw new IllegalArgumentException("Start and End Dates are not valid");
            }

            if (newStartDate != null) promotion.setStartDate(newStartDate);
            if (newEndDate != null) promotion.setEndDate(newEndDate);
        }

        if (newGame != null) {
            Game existingGame = gameRepository.findGameById(newGame.getId()).orElse(null);
            if (existingGame == null) {
                throw new IllegalArgumentException("Game does not exist in the database");
            }
            promotion.setGame(newGame);
        }

        return promotionRepository.save(promotion);
    }

}
