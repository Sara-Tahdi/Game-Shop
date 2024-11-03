package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.Game.GameRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Game.GameResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
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

    @PostMapping(value = "/games/create")
    public GameResponseDTO createGame(@Validated @RequestBody GameRequestDTO gameToCreate) {
        Game g = gameService.createGame(
                gameToCreate.getTitle(),
                gameToCreate.getPrice(),
                gameToCreate.getDescription(),
                gameToCreate.getRating(),
                gameToCreate.getRemainingQuantity(),
                gameToCreate.getIsOffered(),
                gameToCreate.getPublicOpinion(),
                gameToCreate.getCategory()
        );
        return new GameResponseDTO(g);
    }

    @PutMapping(value = "/games/update")
    public GameResponseDTO updateGame(@Validated @RequestBody GameRequestDTO gameToUpdate) {
        Game g = gameService.createGame(
                gameToUpdate.getTitle(),
                gameToUpdate.getPrice(),
                gameToUpdate.getDescription(),
                gameToUpdate.getRating(),
                gameToUpdate.getRemainingQuantity(),
                gameToUpdate.getIsOffered(),
                gameToUpdate.getPublicOpinion(),
                gameToUpdate.getCategory()
        );
        return new GameResponseDTO(g);
    }

    @GetMapping(value = "/games")
    public List<GameResponseDTO> getGames() {
        List<Game> games = gameService.getAllGame();
        return games.stream()
                .map(GameResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/games/{id}")
    public GameResponseDTO getGameById(@PathVariable int id) {
        Game g = gameService.getGameById(id);
        return new GameResponseDTO(g);
    }

    @GetMapping(value = "/games/{title}")
    public GameResponseDTO getGameByTitle(@PathVariable String title) {
        Game g = gameService.getGameByTitle(title);
        return new GameResponseDTO(g);
    }

    @GetMapping(value = "/games/{description}")
    public GameResponseDTO getGameByDescription(@PathVariable String description) {
        Game g = gameService.getGameByDescription(description);
        return new GameResponseDTO(g);
    }

    @GetMapping(value = "/games/{category}")
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
