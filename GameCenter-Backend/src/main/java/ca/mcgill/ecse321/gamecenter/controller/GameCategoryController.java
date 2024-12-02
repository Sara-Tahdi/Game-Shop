package ca.mcgill.ecse321.gamecenter.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import ca.mcgill.ecse321.gamecenter.dto.GameCategory.GameCategoryRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.GameCategory.GameCategoryResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.service.GameCategoryService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/gameCategory")
public class GameCategoryController {

    @Autowired
    private GameCategoryService gameCategoryService;

    // Endpoint to create a GameCategory
    @PostMapping("/create")
    public ResponseEntity<?> createGameCategory(@RequestBody GameCategoryRequestDTO gameCategoryRequestDTO) {
        try {
            GameCategory gameCategory = convertToModel(gameCategoryRequestDTO);
            GameCategory createdGameCategory = gameCategoryService.createGameCategory(gameCategory.getCategory());
            return ResponseEntity.ok().body(new GameCategoryResponseDTO(createdGameCategory));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to update an existing GameCategory
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGameCategory(@PathVariable int id, @RequestBody GameCategoryRequestDTO gameCategoryRequestDTO) {
        try {
            GameCategory updatedGameCategory = gameCategoryService.updateGameCategory(id, gameCategoryRequestDTO.getCategory());
            return ResponseEntity.ok().body(new GameCategoryResponseDTO(updatedGameCategory));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to retrieve a specific GameCategory by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getGameCategoryById(@PathVariable int id) {
        try {
            GameCategory gameCategory = gameCategoryService.getGameCategory(id)
                    .orElseThrow(() -> new IllegalArgumentException("GameCategory with ID " + id + " not found."));
            return ResponseEntity.ok().body(new GameCategoryResponseDTO(gameCategory));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to retrieve all GameCategories
    @GetMapping
    public ResponseEntity<?> getAllGameCategories() {
        try {
            List<GameCategory> gameCategories = gameCategoryService.getAllGameCategories();
            return ResponseEntity.ok().body(gameCategories.stream()
                    .map(GameCategoryResponseDTO::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Helper method to convert GameCategoryRequestDTO to GameCategory model
    private GameCategory convertToModel(GameCategoryRequestDTO dto) {
        GameCategory gameCategory = new GameCategory();
        gameCategory.setCategory(dto.getCategory());
        return gameCategory;
    }
}
