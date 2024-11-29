
package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class GameCategoryService {

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    public GameCategory createGameCategory(String category) {
        // Validate category input
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty.");
        }

        // Check to see if category with the same name already exists
        if (gameCategoryRepository.findGameCategoryByCategory(category).isPresent()) {
            throw new IllegalArgumentException("Category with name " + category + " already exists.");
        }
        
        GameCategory gameCategory = new GameCategory(category);
        return gameCategoryRepository.save(gameCategory);
    }

    public GameCategory updateGameCategory(int id, String category) {
        // Validate category input
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty.");
        }
        // Check to see if category with the same name already exists
        if (gameCategoryRepository.findGameCategoryByCategory(category).isPresent()) {
            throw new IllegalArgumentException("Category with name " + category + " already exists.");
        }
        
        Optional<GameCategory> existingCategory = gameCategoryRepository.findById(id);
        
        if (existingCategory.isPresent()) {
            GameCategory gameCategory = existingCategory.get();
            gameCategory.setCategory(category);
            return gameCategoryRepository.save(gameCategory);
        } else {
            throw new IllegalArgumentException("GameCategory with id " + id + " not found.");
        }
    }
    // Add this method to retrieve all categories
    public List<GameCategory> getAllGameCategories() {
    
    return gameCategoryRepository.getAll().orElse(null);
    }
    
    public Optional<GameCategory> getGameCategory(int id) {
        return gameCategoryRepository.findById(id);
    }


    public GameCategory getGameCategoryById(int id) {
        return gameCategoryRepository.findGameCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Game category not found with id: " + id));
    }
}
