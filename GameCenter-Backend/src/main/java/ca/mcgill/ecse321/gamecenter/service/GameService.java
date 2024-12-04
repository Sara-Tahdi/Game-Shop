package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public List<Game> getAllGame() {
        List<Game> g = gameRepository.findGameByGameType(Game.class).orElse(null);
        if (g == null) {
            throw new IllegalArgumentException("There are no Games");
        }
        return g;
    }

    public Game getGameById(int id) {
        Game g = gameRepository.findGameById(id).orElse(null);
        if (g == null) {
            throw new IllegalArgumentException("There is no Game with id: " + id);
        }
        return g;
    }

    public Game getGameByTitle(String title) {
        Game g = gameRepository.findGameByTitle(title).orElse(null);
        if (g == null) {
            throw new IllegalArgumentException("There is no Game with title: " + title);
        }
        return g;
    }

    public List<Game> getGameByCategoryId(int categoryId) {
        List<Game> g = gameRepository.findGamesByGameCategoryId(categoryId).orElse(null);
        if (g == null) {
            throw new IllegalArgumentException("There is no Game with category whose ID is: " + categoryId);
        }
        return g;
    }

    public List<Game> getGameByCategory(String category) {
        List<Game> g = gameRepository.findGamesByGameCategory(category).orElse(null);
        if (g == null) {
            throw new IllegalArgumentException("There is no Game with category: " + category);
        }
        return g;
    }

    public List<Game> getGameByPriceRange(Float minPrice, Float maxPrice) {
        if (minPrice == null || maxPrice == null || minPrice < 0 || maxPrice < minPrice) {
            throw new IllegalArgumentException("Invalid price range");
        }

        List<Game> g = gameRepository.findGamesByPriceRange(minPrice, maxPrice).orElse(null);
        if (g == null) {
            throw new IllegalArgumentException("There is no Game within price range: [" + minPrice + ", " + maxPrice + "]");
        }
        return g;
    }

    public List<Game> getGameByRatingRange(Float minRating, Float maxRating) {
        if (minRating == null || maxRating == null) {
            throw new IllegalArgumentException("Rating range cannot be null");
        }

        if (minRating < 0 || maxRating > 5 || minRating > maxRating) {
            throw new IllegalArgumentException("Invalid rating range. Ratings must be between 0 and 5, and minimum rating must not exceed maximum rating");
        }

        List<Game> g = gameRepository.findGamesByRatingRange(minRating, maxRating).orElse(null);
        if (g == null) {
            throw new IllegalArgumentException("There is no Game within rating range: [" + minRating + ", " + maxRating + "]");
        }
        return g;
    }

    public Game getGameByDescription(String description) {
        Game g = gameRepository.findGameByDescription(description).orElse(null);
        if (g == null) {
            throw new IllegalArgumentException("There is no Game with description: " + description);
        }
        return g;
    }

    public List<Game> getAllAvailableGames() {
        List<Game> games = gameRepository.findAllAvailableGames().orElse(null);
        if (games == null || games.isEmpty()) {
            throw new IllegalArgumentException("There are no available games");
        }
        return games;
    }

    public List<Game> getAllAvailableGamesByCategory(String category) {
        List<Game> games = gameRepository.findAllAvailableGamesByGameCategory(category).orElse(null);
        if (games == null || games.isEmpty()) {
            throw new IllegalArgumentException("There are no available games in category: " + category);
        }
        return games;
    }

    public List<Game> getAllAvailableGamesByPriceRange(float minPrice, float maxPrice) {
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }

        List<Game> games = gameRepository.findAllAvailableGamesByPriceRange(minPrice, maxPrice).orElse(null);
        if (games == null || games.isEmpty()) {
            throw new IllegalArgumentException("There are no available games within price range: [" + minPrice + ", " + maxPrice + "]");
        }
        return games;
    }

    public List<Game> getAllAvailableGamesByRatingRange(float minRating, float maxRating) {
        if (minRating > maxRating) {
            throw new IllegalArgumentException("Minimum rating cannot be greater than maximum rating");
        }

        if (minRating < 0 || maxRating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }

        List<Game> games = gameRepository.findAllAvailableGamesByRatingRange(minRating, maxRating).orElse(null);
        if (games == null || games.isEmpty()) {
            throw new IllegalArgumentException("There are no available games within rating range: [" + minRating + ", " + maxRating + "]");
        }
        return games;
    }


    @Transactional
    public Game createGame(String aTitle, Float aPrice, String aDescription, Game.GeneralFeeling aPublicOpinion, GameCategory aCategory) {
        Game ref = gameRepository.findGameByTitle(aTitle).orElse(null);
        if (ref != null) {
            throw new IllegalArgumentException("Game already exists with title: " + aTitle);
        }

        ref = gameRepository.findGameByDescription(aDescription).orElse(null);
        if (ref != null) {
            throw new IllegalArgumentException("Game already exists with description: " + aTitle);
        }

        List<Game> refList = gameRepository.findGamesByGameCategory(aCategory.getCategory()).orElse(null);
        ref = findGameByTitle(refList, aTitle);
        if (ref != null) {
            throw new IllegalArgumentException("Game already exists with description: " + aDescription + " and title: " + aTitle);
        }

        if (aPrice == null || aPrice <= 0.0) {
            throw new IllegalArgumentException("Price is not valid");
        }

        Game g = new Game(aTitle, aPrice, aDescription, 0, 0, false, aPublicOpinion, aCategory, "");
        return gameRepository.save(g);
    }

    @Transactional
    public Game updateGame(
            Integer id,
            String title,
            Float price,
            String description,
            Float rating,
            Integer remainingQuantity,
            Boolean isOffered,
            GameCategory category) {

        Game game = gameRepository.findGameById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no Game with id: " + id));

        Game gameTitle = gameRepository.findGameByTitle(title).orElse(null);
        if (gameTitle != null && gameTitle.getId() != id) {
            throw new IllegalArgumentException("There already exists a Game with title: " + title);
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price is not valid");
        }
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating is not valid");
        }
        if (remainingQuantity < 0) {
            throw new IllegalArgumentException("Remaining quantity is not valid");
        }
        if (title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is not valid");
        }

        game.setTitle(title);
        game.setPrice(price);
        game.setDescription(description);
        game.setRating(rating);
        game.setRemainingQuantity(remainingQuantity);
        game.setIsOffered(isOffered);
        game.setCategory(category);

        return gameRepository.save(game);
    }

    @Transactional
    public Game makeGameOffered(int gameId) {
        Game game = gameRepository.findGameById(gameId).orElse(null);
        if (game == null) {
            throw new IllegalArgumentException("There is no Game with id: " + gameId);
        }

        if (game.getIsOffered()) {
            throw new IllegalArgumentException("Game is already offered");
        }

        if (game.getRemainingQuantity() <= 0) {
            throw new IllegalArgumentException("Cannot offer game with no remaining quantity");
        }

        game.setIsOffered(true);
        return gameRepository.save(game);
    }

    @Transactional
    public Game makeGameNotOffered(int gameId) {
        Game game = gameRepository.findGameById(gameId).orElse(null);
        if (game == null) {
            throw new IllegalArgumentException("There is no Game with id: " + gameId);
        }

        if (!game.getIsOffered()) {
            throw new IllegalArgumentException("Game is already not offered");
        }

        game.setIsOffered(false);
        return gameRepository.save(game);
    }

    //helper method
    public static Game findGameByTitle(List<Game> games, String aTitle) {
        if (games == null || aTitle == null) {
            return null;
        }

        for (Game game : games) {
            if (aTitle.equals(game.getTitle())) {
                return game;
            }
        }

        return null;
    }

}
