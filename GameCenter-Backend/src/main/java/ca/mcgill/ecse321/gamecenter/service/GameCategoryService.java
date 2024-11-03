package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Service
public class GameCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(GameCategoryService.class);

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    public GameCategory createGameCategory(String category) {
        GameCategory gameCategory = new GameCategory(category);
        logger.info("Creating new GameCategory with category: {}", category);
        return gameCategoryRepository.save(gameCategory);
    }

    public GameCategory updateGameCategory(int id, String category) {
        Optional<GameCategory> existingCategory = gameCategoryRepository.findById(id);
        
        if (existingCategory.isPresent()) {
            GameCategory gameCategory = existingCategory.get();
            gameCategory.setCategory(category);
            logger.info("Updating GameCategory with id: {} to category: {}", id, category);
            return gameCategoryRepository.save(gameCategory);
        } else {
            throw new IllegalArgumentException("GameCategory with id " + id + " not found.");
        }
    }
    
    public Optional<GameCategory> getGameCategory(int id) {
        return gameCategoryRepository.findById(id);
    }
}