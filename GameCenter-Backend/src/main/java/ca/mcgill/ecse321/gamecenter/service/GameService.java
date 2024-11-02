package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    public Game createGame(String title, float price, String description, Game.GeneralFeeling generalFeeling, GameCategory category) {
        Game g = new Game(title, price, description, 0, 0, false, generalFeeling, category);
        return gameRepository.save(g);
    }

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
            throw new IllegalArgumentException("There is no Game with category: " + gameCategoryRepository.findGameCategoryById(categoryId));
        }
        return g;
    }

    public List<Game> getGameByCategory(String category) {
        List<Game> g = gameRepository.findGamesByGameCategory(category).orElse(null);
        if (g == null) {
            throw new IllegalArgumentException("There is no Game with category: " + gameCategoryRepository.findGameCategoryByCategory(category));
        }
        return g;
    }

    public List<Game> getGameByPriceRange(Integer minPrice, Integer maxPrice) {
        List<Game> g = gameRepository.findGamesByPriceRange(minPrice, maxPrice).orElse(null);
        if (g == null) {
            throw new IllegalArgumentException("There is no Game within price range: [" + minPrice + ", " + maxPrice + "]");
        }
        return g;
    }

    public List<Game> getGameByRatingRange(Integer minRating, Integer maxRating) {
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


    @Transactional
    public Game createGame(String aTitle, Float aPrice, String aDescription, Float aRating, Integer aRemainingQuantity, boolean aIsOffered, Game.GeneralFeeling aPublicOpinion, GameCategory aCategory) {
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

        if (aRating == null
                || aRating <= 0.0
                || aRating >= 5.0) {
            throw new IllegalArgumentException("Rating is not valid");
        }

        if (aRemainingQuantity == null || aRemainingQuantity < 0) {
            throw new IllegalArgumentException("Remaining Quantity is not valid");
        }

        Game g = new Game(aTitle, aPrice, aDescription, aRating, aRemainingQuantity, aIsOffered, aPublicOpinion, aCategory);
        return gameRepository.save(g);
    }

    @Transactional
    public Game updateGame(Integer oldId, String newTitle, Float newPrice, String newDescription, Float newRating, Integer newRemainingQuantity, boolean newIsOffered, GameCategory newCategory) {
        Game g = gameRepository.findGameById(oldId).orElse(null);
        if (g == null) {
            throw new IllegalArgumentException("There is no Game with id: " + oldId);
        }

        Game testTitle = gameRepository.findGameByTitle(newTitle).orElse(null);
        if (testTitle != null && testTitle.getId() != g.getId()) {
            throw new IllegalArgumentException("There already exists a Game with title: " + newTitle);
        }

        Game testDescription= gameRepository.findGameByDescription(newDescription).orElse(null);
        if (testDescription != null && testDescription.getId() != g.getId()) {
            throw new IllegalArgumentException("There already exists a Game with description: " + newDescription);
        }

        if (newPrice == null || newPrice <= 0.0) {
            throw new IllegalArgumentException("Price is not valid");
        }

        if (newRating == null
                || newRating <= 0.0
                || newRating >= 5.0) {
            throw new IllegalArgumentException("Rating is not valid");
        }

        if (newRemainingQuantity == null || newRemainingQuantity < 0) {
            throw new IllegalArgumentException("Remaining Quantity is not valid");
        }

        g.setTitle(newTitle);
        g.setPrice(newPrice);
        g.setDescription(newDescription);
        g.setRating(newRating);
        g.setRemainingQuantity(newRemainingQuantity);
        g.setIsOffered(newIsOffered);
        g.setCategory(newCategory);

        return gameRepository.save(g);
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
