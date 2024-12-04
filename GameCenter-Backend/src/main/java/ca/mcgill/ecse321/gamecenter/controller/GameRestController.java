package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.Game.GameRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Game.GameResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Game.GameUpdateRequestDTO;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.service.GameCategoryService;
import ca.mcgill.ecse321.gamecenter.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class GameRestController {
    @Autowired
    private GameService gameService;
    @Autowired
    private GameCategoryService gameCategoryService;

    @PostMapping(value = "/games/create")
    public ResponseEntity<?> createGame(@Validated @RequestBody GameRequestDTO gameToCreate) {
        try {
            GameCategory c = gameCategoryService.getGameCategoryById(gameToCreate.getCategoryId());
            Game g = gameService.createGame(
                    gameToCreate.getTitle(),
                    gameToCreate.getPrice(),
                    gameToCreate.getDescription(),
                    gameToCreate.getPublicOpinion(),
                    c,
                    gameToCreate.getImageUrl()
            );
            return ResponseEntity.ok().body(new GameResponseDTO(g));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/games/update/{id}")
    public ResponseEntity<?> updateGame(@Validated @RequestBody GameUpdateRequestDTO gameToUpdate, @PathVariable int id) {
        try {
            GameCategory categoryToUpdate = gameCategoryService.getGameCategoryById(gameToUpdate.getCategoryId());
            Game game = gameService.updateGame(
                    id,
                    gameToUpdate.getTitle(),
                    gameToUpdate.getPrice(),
                    gameToUpdate.getDescription(),
                    gameToUpdate.getRating(),
                    gameToUpdate.getRemainingQuantity(),
                    gameToUpdate.getIsOffered(),
                    categoryToUpdate
            );
            return ResponseEntity.ok().body(new GameResponseDTO(game));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/games")
    public ResponseEntity<?> getGames() {
        try {
            List<Game> games = gameService.getAllGame();
            return ResponseEntity.ok().body(games.stream()
                    .map(GameResponseDTO::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/games/id/{id}")
    public ResponseEntity<?> getGameById(@PathVariable int id) {
        try {
            Game g = gameService.getGameById(id);
            return ResponseEntity.ok().body(new GameResponseDTO(g));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/games/title/{title}")
    public ResponseEntity<?> getGameByTitle(@PathVariable String title) {
        try {
            Game g = gameService.getGameByTitle(title);
            return ResponseEntity.ok().body(new GameResponseDTO(g));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/games/description/{description}")
    public ResponseEntity<?> getGameByDescription(@PathVariable String description) {
        try {
            Game g = gameService.getGameByDescription(description);
            return ResponseEntity.ok().body(new GameResponseDTO(g));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/games/category/{category}")
    public ResponseEntity<?> getGamesByCategory(@PathVariable GameCategory category) {
        try {
            List<Game> games = gameService.getGameByCategory(category.getCategory());
            return ResponseEntity.ok().body(games.stream()
                    .map(GameResponseDTO::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/games/available")
    public ResponseEntity<?> getAllAvailableGames() {
        try {
            List<Game> games = gameService.getAllAvailableGames();
            return ResponseEntity.ok().body(games.stream()
                    .map(GameResponseDTO::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/games/available/category/{category}")
    public ResponseEntity<?> getAllAvailableGamesByCategory(@PathVariable String category) {
        try {
            List<Game> games = gameService.getAllAvailableGamesByCategory(category);
            return ResponseEntity.ok().body(games.stream()
                    .map(GameResponseDTO::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/games/available/price")
    public ResponseEntity<?> getAllAvailableGamesByPriceRange(
            @RequestParam float minPrice,
            @RequestParam float maxPrice) {
        try {
            List<Game> games = gameService.getAllAvailableGamesByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok().body(games.stream()
                    .map(GameResponseDTO::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/games/available/rating")
    public ResponseEntity<?> getAllAvailableGamesByRatingRange(
            @RequestParam float minRating,
            @RequestParam float maxRating) {
        try {
            List<Game> games = gameService.getAllAvailableGamesByRatingRange(minRating, maxRating);
            return ResponseEntity.ok().body(games.stream()
                    .map(GameResponseDTO::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/games/{id}/offer")
    public ResponseEntity<?> makeGameOffered(@PathVariable int id) {
        try {
            Game game = gameService.makeGameOffered(id);
            return ResponseEntity.ok().body(new GameResponseDTO(game));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/games/{id}/unoffer")
    public ResponseEntity<?> makeGameNotOffered(@PathVariable int id) {
        try {
            Game game = gameService.makeGameNotOffered(id);
            return ResponseEntity.ok().body(new GameResponseDTO(game));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
