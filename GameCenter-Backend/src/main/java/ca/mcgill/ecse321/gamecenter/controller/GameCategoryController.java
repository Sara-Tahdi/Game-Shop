package ca.mcgill.ecse321.gamecenter.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.gamecenter.dto.GameCategory.GameCategoryRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.GameCategory.GameCategoryResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.service.GameCategoryService;

@RestController
@RequestMapping("/gameCategory")
public class GameCategoryController {

    @Autowired
    private GameCategoryService gameCategoryService;

    // Endpoint to create a GameCategory
    @PostMapping("/create")
    public GameCategoryResponseDTO createGameCategory(@RequestBody GameCategoryRequestDTO gameCategoryRequestDTO) {
        GameCategory gameCategory = convertToModel(gameCategoryRequestDTO);
        GameCategory createdGameCategory = gameCategoryService.createGameCategory(gameCategory.getCategory());
        return new GameCategoryResponseDTO(createdGameCategory);
    }

    // Endpoint to update an existing GameCategory
    @PostMapping("/{id}")
    public GameCategoryResponseDTO updateGameCategory(@PathVariable int id, @RequestBody GameCategoryRequestDTO gameCategoryRequestDTO) {
        GameCategory updatedGameCategory = gameCategoryService.updateGameCategory(id, gameCategoryRequestDTO.getCategory());
        return new GameCategoryResponseDTO(updatedGameCategory);
    }

    // Endpoint to retrieve a specific GameCategory by ID
    @GetMapping("/{id}")
    public GameCategoryResponseDTO getGameCategoryById(@PathVariable int id) {
        GameCategory gameCategory = gameCategoryService.getGameCategory(id)
                .orElseThrow(() -> new IllegalArgumentException("GameCategory with ID " + id + " not found."));
        return new GameCategoryResponseDTO(gameCategory);
    }

    // Endpoint to retrieve all GameCategories
    @GetMapping
    public List<GameCategoryResponseDTO> getAllGameCategories() {
        List<GameCategory> gameCategories = gameCategoryService.getAllGameCategories();
        return gameCategories.stream()
                .map(GameCategoryResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Helper method to convert GameCategoryRequestDTO to GameCategory model
    private GameCategory convertToModel(GameCategoryRequestDTO dto) {
        GameCategory gameCategory = new GameCategory();
        gameCategory.setCategory(dto.getCategory());
        return gameCategory;
    }
}
