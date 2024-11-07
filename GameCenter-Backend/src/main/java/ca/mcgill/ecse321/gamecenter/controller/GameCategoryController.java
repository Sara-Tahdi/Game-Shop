package ca.mcgill.ecse321.gamecenter.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.gamecenter.dto.GameCategoryDTO;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.service.GameCategoryService;

@RestController
@RequestMapping("/gameCategory")
public class GameCategoryController {
    @Autowired
    private GameCategoryService gameCategoryService;
    
    @PostMapping("/create")
    public GameCategoryDTO createGameCategory(@RequestBody GameCategoryDTO GamecategoryDTO) {
        GameCategory gameCategory = convertToModel(GamecategoryDTO);
        return new GameCategoryDTO(gameCategoryService.createGameCategory(gameCategory.getCategory()));
    }

    // Endpoint to update an existing GameCategory
    @PostMapping("/{id}")
    public GameCategoryDTO updateGameCategory(@PathVariable int id, @RequestBody GameCategoryDTO gameCategoryDTO) {
        GameCategory updatedgameCategory = gameCategoryService.updateGameCategory(id, gameCategoryDTO.getCategory());
        return new GameCategoryDTO(updatedgameCategory);
    }

    // Endpoint to retrieve a specific GameCategory by ID
    @GetMapping("/{id}")
    public GameCategoryDTO getGameCategoryById(@PathVariable int id) {
        GameCategory gameCategory = gameCategoryService.getGameCategory(id)
                .orElseThrow(() -> new IllegalArgumentException("GameCategory with ID " + id + " not found."));
        return new GameCategoryDTO(gameCategory);
    }

    // Endpoint to retrieve all GameCategories
    @GetMapping
    public List<GameCategoryDTO> getAllGameCategories() {
        return gameCategoryService.getAllGameCategories().stream()
                .map(GameCategoryDTO::new)
                .collect(Collectors.toList());
    }


     // Helper method to convert GameCategoryDTO to GameCategory model
    private GameCategory convertToModel(GameCategoryDTO dto) {
        GameCategory gameCategory = new GameCategory();
        gameCategory.setId(dto.getId());
        gameCategory.setCategory(dto.getCategory());
        return gameCategory;
    }
}
