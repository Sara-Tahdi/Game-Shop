import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.service.GameCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class GameCategoryServiceTest {

    @MockBean
    private GameCategoryRepository gameCategoryRepository;

    @Autowired
    private GameCategoryService gameCategoryService;

    @Test
    void testCreateGameCategory() {
        String category = "Arcade";
        GameCategory gameCategory = new GameCategory(category);

        when(gameCategoryRepository.save(any(GameCategory.class))).thenReturn(gameCategory);

        GameCategory createdCategory = gameCategoryService.createGameCategory(category);

        assertNotNull(createdCategory);
        assertEquals("Arcade", createdCategory.getCategory());
        verify(gameCategoryRepository, times(1)).save(any(GameCategory.class));
    }

    @Test
    void testUpdateExistingGameCategory() {
        int categoryId = 1;
        String newCategoryName = "Puzzle";
        GameCategory existingCategory = new GameCategory("OldCategory");
        existingCategory.setId(categoryId);

        when(gameCategoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(gameCategoryRepository.save(existingCategory)).thenReturn(existingCategory);

        GameCategory updatedCategory = gameCategoryService.updateGameCategory(categoryId, newCategoryName);

        assertNotNull(updatedCategory);
        assertEquals("Puzzle", updatedCategory.getCategory());
        assertEquals(categoryId, updatedCategory.getId());
        verify(gameCategoryRepository, times(1)).save(existingCategory);
    }

    @Test
    void testUpdateNonExistentGameCategory() {
        int categoryId = 99;
        String newCategoryName = "Sports";

        when(gameCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gameCategoryService.updateGameCategory(categoryId, newCategoryName);
        });

        assertEquals("GameCategory with id " + categoryId + " not found.", exception.getMessage());
        verify(gameCategoryRepository, never()).save(any(GameCategory.class));
    }

    @Test
    void testCreateGameCategoryWithInvalidName() {
        // Attempt to create a category with an empty name
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            gameCategoryService.createGameCategory("");
        });
        assertEquals("Category name cannot be null or empty.", exception1.getMessage());

        // Attempt to create a category with a null name
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            gameCategoryService.createGameCategory(null);
        });
        assertEquals("Category name cannot be null or empty.", exception2.getMessage());

        verify(gameCategoryRepository, never()).save(any(GameCategory.class));
    }

    @Test
    void testUpdateGameCategoryWithInvalidName() {
        int categoryId = 1;
        GameCategory existingCategory = new GameCategory("OldCategory");
        existingCategory.setId(categoryId);

        when(gameCategoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        // Attempt to update with an empty category name
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            gameCategoryService.updateGameCategory(categoryId, "");
        });
        assertEquals("Category name cannot be null or empty.", exception1.getMessage());

        // Attempt to update with a null category name
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            gameCategoryService.updateGameCategory(categoryId, null);
        });
        assertEquals("Category name cannot be null or empty.", exception2.getMessage());

        verify(gameCategoryRepository, never()).save(existingCategory);
    }

    @Test
    void testGetAllGameCategories() {
        GameCategory category1 = new GameCategory("Arcade");
        GameCategory category2 = new GameCategory("Puzzle");

        when(gameCategoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<GameCategory> allCategories = gameCategoryService.getAllGameCategories();

        assertEquals(2, allCategories.size());
        assertEquals("Arcade", allCategories.get(0).getCategory());
        assertEquals("Puzzle", allCategories.get(1).getCategory());
        verify(gameCategoryRepository, times(1)).findAll();
    }
}
