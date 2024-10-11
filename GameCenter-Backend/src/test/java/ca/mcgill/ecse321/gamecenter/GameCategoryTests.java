package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GameCategoryTests {

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
    }


    @Test
    public void testPersistAndLoadGameCategory() {
        GameCategory gameCategory = new GameCategory();
        gameCategory.setCategory("Action");
        gameCategory = gameCategoryRepository.save(gameCategory);
        assertNotNull(gameCategory);

        int id = gameCategory.getId();
        String category = gameCategory.getCategory();

        gameCategory = gameCategoryRepository.findGameCategoryById(id).orElse(null);
        assertNotNull(gameCategory);
        assertEquals(id, gameCategory.getId());
        assertEquals(category, gameCategory.getCategory());
    }

    @Test
    void testLoadAndFetchGameCategoryByCategory() {
        GameCategory gameCategory = new GameCategory();
        gameCategory.setCategory("Action");
        gameCategory = gameCategoryRepository.save(gameCategory);
        assertNotNull(gameCategory);

        Game game = new Game();
        game.setTitle("GTA 6");
        game.setCategories(gameCategory);
        game = gameRepository.save(game);

        GameCategory gameCategoryFromDb = gameCategoryRepository.findGameCategoryByCategory(gameCategory.getCategory()).orElse(null);
        assertNotNull(gameCategoryFromDb);

        assertEquals(gameCategory.getId(), gameCategoryFromDb.getId());
        assertEquals(gameCategory.getCategory(), gameCategoryFromDb.getCategory());
    }

}
