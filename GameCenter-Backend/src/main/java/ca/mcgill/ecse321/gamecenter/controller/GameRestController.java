package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.Game.GameRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Game.GameResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.service.GameCategoryService;
import ca.mcgill.ecse321.gamecenter.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GameRestController {
    @Autowired
    private GameService gameService;
    @Autowired
    private GameCategoryService gameCategoryService;

    @PostMapping(value = "/games/create")
    public GameResponseDTO createGame(@Validated @RequestBody GameRequestDTO gameToCreate) {
        GameCategory c = gameCategoryService.getGameCategoryById(gameToCreate.getCategoryId());
        Game g = gameService.createGame(
                gameToCreate.getTitle(),
                gameToCreate.getPrice(),
                gameToCreate.getDescription(),
                gameToCreate.getRating(),
                gameToCreate.getRemainingQuantity(),
                gameToCreate.getIsOffered(),
                gameToCreate.getPublicOpinion(),
                c
        );
        return new GameResponseDTO(g);
    }

    @PutMapping("/games/update/{id}")
    public GameResponseDTO updateGame(
            @PathVariable int id,
            @RequestParam(required = false) String newTitle,
            @RequestParam(required = false) Float newPrice,
            @RequestParam(required = false) String newDescription,
            @RequestParam(required = false) Float newRating,
            @RequestParam(required = false) Integer newRemainingQuantity,
            @RequestParam(required = false) Boolean newIsOffered,
            @RequestParam(required = false) Game.GeneralFeeling newPublicOpinion,
            @RequestParam(required = false) Integer newCategoryId) {

        Game existingGame = gameService.getGameById(id);

        String titleToUpdate = newTitle != null ? newTitle : existingGame.getTitle();
        Float priceToUpdate = newPrice != null ? newPrice : existingGame.getPrice();
        String descriptionToUpdate = newDescription != null ? newDescription : existingGame.getDescription();
        Float ratingToUpdate = newRating != null ? newRating : existingGame.getRating();
        Integer quantityToUpdate = newRemainingQuantity != null ? newRemainingQuantity : existingGame.getRemainingQuantity();
        Boolean isOfferedToUpdate = newIsOffered != null ? newIsOffered : existingGame.getIsOffered();GameCategory categoryToUpdate = newCategoryId != null ?
                gameCategoryService.getGameCategoryById(newCategoryId) :
                existingGame.getCategory();

        Game game = gameService.updateGame(
                id,
                titleToUpdate,
                priceToUpdate,
                descriptionToUpdate,
                ratingToUpdate,
                quantityToUpdate,
                isOfferedToUpdate,
                categoryToUpdate
        );

        return new GameResponseDTO(game);
    }

    @GetMapping(value = "/games")
    public List<GameResponseDTO> getGames() {
        List<Game> games = gameService.getAllGame();
        return games.stream()
                .map(GameResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/games/id/{id}")
    public GameResponseDTO getGameById(@PathVariable int id) {
        Game g = gameService.getGameById(id);
        return new GameResponseDTO(g);
    }

    @GetMapping(value = "/games/title/{title}")
    public GameResponseDTO getGameByTitle(@PathVariable String title) {
        Game g = gameService.getGameByTitle(title);
        return new GameResponseDTO(g);
    }

    @GetMapping(value = "/games/description/{description}")
    public GameResponseDTO getGameByDescription(@PathVariable String description) {
        Game g = gameService.getGameByDescription(description);
        return new GameResponseDTO(g);
    }

    @GetMapping(value = "/games/category/{category}")
    public List<GameResponseDTO> getGamesByCategory(@PathVariable GameCategory category) {
        List<Game> games = gameService.getGameByCategory(category.getCategory());
        return games.stream()
                .map(GameResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/games/available")
    public List<GameResponseDTO> getAllAvailableGames() {
        List<Game> games = gameService.getAllAvailableGames();
        return games.stream()
                .map(GameResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/games/available/category/{category}")
    public List<GameResponseDTO> getAllAvailableGamesByCategory(@PathVariable String category) {
        List<Game> games = gameService.getAllAvailableGamesByCategory(category);
        return games.stream()
                .map(GameResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/games/available/price")
    public List<GameResponseDTO> getAllAvailableGamesByPriceRange(
            @RequestParam float minPrice,
            @RequestParam float maxPrice) {
        List<Game> games = gameService.getAllAvailableGamesByPriceRange(minPrice, maxPrice);
        return games.stream()
                .map(GameResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/games/available/rating")
    public List<GameResponseDTO> getAllAvailableGamesByRatingRange(
            @RequestParam float minRating,
            @RequestParam float maxRating) {
        List<Game> games = gameService.getAllAvailableGamesByRatingRange(minRating, maxRating);
        return games.stream()
                .map(GameResponseDTO::new)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/games/{id}/offer")
    public GameResponseDTO makeGameOffered(@PathVariable int id) {
        Game game = gameService.makeGameOffered(id);
        return new GameResponseDTO(game);
    }

    @PutMapping(value = "/games/{id}/unoffer")
    public GameResponseDTO makeGameNotOffered(@PathVariable int id) {
        Game game = gameService.makeGameNotOffered(id);
        return new GameResponseDTO(game);
    }
}
