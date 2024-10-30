package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameCategoryService {
    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    public GameCategory createGameCategory(String categoryName) {
        GameCategory g = new GameCategory(categoryName);
        return gameCategoryRepository.save(g);
    }
}
