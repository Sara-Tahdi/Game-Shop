package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameTests {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @BeforeEach

    @AfterEach
    public void clear() {
        reviewRepository.deleteAll();
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
    }

    @Test
    void testPersistAndLoadGame(){
        Game game = new Game();
        game.setTitle("Super Mario Bros");
        game.setPrice(49.99f);
        game.setDescription("Wa-hoo!");
        game.setRating(4.9f);
        game.setRemainingQuantity(50);
        game.setIsOffered(true);
        game.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
        game = gameRepository.save(game);
        assertNotNull(game);
        assertTrue(game.isIsOffered());

        int id = game.getId();
        String title = game.getTitle();
        float price = game.getPrice();
        String description = game.getDescription();
        float rating = game.getRating();
        int remainingQuantity = game.getRemainingQuantity();
        boolean isOffered = game.isIsOffered();
        Game.GeneralFeeling publicOpinion = game.getPublicOpinion();

        Game gameFromDb = gameRepository.findGameById(id).orElse(null);
        assertNotNull(gameFromDb);

        assertEquals(id, gameFromDb.getId());
        assertEquals(title, gameFromDb.getTitle());
        assertEquals(price, gameFromDb.getPrice());
        assertEquals(description, gameFromDb.getDescription());
        assertEquals(rating, gameFromDb.getRating());
        assertEquals(remainingQuantity, gameFromDb.getRemainingQuantity());
        assertEquals(isOffered, gameFromDb.isIsOffered());
        assertEquals(publicOpinion, gameFromDb.getPublicOpinion());
    }

    @Test
    void testLoadAndFetchGameByTitle(){
        Game game = new Game();
        game.setTitle("Super Mario Bros");
        game.setPrice(49.99f);
        game.setDescription("Wa-hoo!");
        game.setRating(4.9f);
        game.setRemainingQuantity(50);
        game.setIsOffered(true);
        game.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
        game = gameRepository.save(game);
        assertNotNull(game);
        assertTrue(game.isIsOffered());

        // Get by title tests
        Game gameFromDb = gameRepository.findGameByTitle(game.getTitle()).orElse(null);
        assertNotNull(gameFromDb);

        assertTrue(gameFromDb.isIsOffered());
        assertEquals(game.getTitle(), gameFromDb.getTitle());
    }

    @Test
    void testLoadAndFetchGameByDescription(){
        Game game = new Game();
        game.setTitle("Super Mario Bros");
        game.setPrice(49.99f);
        game.setDescription("Wa-hoo!");
        game.setRating(4.9f);
        game.setRemainingQuantity(50);
        game.setIsOffered(true);
        game.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
        game = gameRepository.save(game);
        assertNotNull(game);
        assertTrue(game.isIsOffered());

        // Get by description tests
        Game gameFromDb = gameRepository.findGameByDescription(game.getDescription()).orElse(null);
        assertNotNull(gameFromDb);

        assertTrue(gameFromDb.isIsOffered());
        assertEquals(game.getDescription(), gameFromDb.getDescription());
    }

    @Test
    void testUpdateGameByQuantity(){
        Game game = new Game();
        game.setTitle("Super Mario Bros");
        game.setPrice(49.99f);
        game.setDescription("Wa-hoo!");
        game.setRating(4.9f);
        game.setRemainingQuantity(50);
        game.setIsOffered(true);
        game.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
        game = gameRepository.save(game);
        assertNotNull(game);
        assertTrue(game.isIsOffered());

        // Update quantity tests
        gameRepository.updateGameByQuantity(game.getId(), 40);
        Game gameFromDb = gameRepository.findGameById(game.getId()).orElse(null);
        assertNotNull(gameFromDb);

        assertTrue(gameFromDb.isIsOffered());
        assertNotEquals(game.getRemainingQuantity(), gameFromDb.getRemainingQuantity());
    }

    @Test
    void testLoadAndFetchGamesByCategory(){

        GameCategory platformer = new GameCategory();
        platformer.setCategory("Platformer");
        platformer = gameCategoryRepository.save(platformer);

        GameCategory jrpg = new GameCategory();
        jrpg.setCategory("JRPG");
        jrpg = gameCategoryRepository.save(jrpg);

        Game marioBros = new Game();
        marioBros.setTitle("Super Mario Bros");
        marioBros.setCategories(platformer);
        marioBros.setIsOffered(true);
        marioBros.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
        marioBros = gameRepository.save(marioBros);
        assertNotNull(marioBros);
        assertTrue(marioBros.isIsOffered());

        Game rayman = new Game();
        rayman.setTitle("Rayman Legends");
        rayman.setCategories(platformer);
        rayman.setIsOffered(true);
        rayman.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
        rayman = gameRepository.save(rayman);
        assertNotNull(rayman);
        assertTrue(rayman.isIsOffered());

        Game finalFantasy = new Game();
        finalFantasy.setTitle("Final Fantasy VII");
        finalFantasy.setCategories(jrpg);
        finalFantasy.setIsOffered(true);
        finalFantasy.setPublicOpinion(Game.GeneralFeeling.POSITIVE);
        finalFantasy = gameRepository.save(finalFantasy);
        assertNotNull(finalFantasy);
        assertTrue(finalFantasy.isIsOffered());

        List<Game> platformersFromDb = gameRepository.findGamesByGameCategoryId(platformer.getId()).orElse(null);
        assertNotNull(platformersFromDb);
        assertEquals(2, platformersFromDb.size());
        assertEquals(marioBros.getId(), platformersFromDb.getFirst().getId());
        assertEquals(rayman.getId(), platformersFromDb.get(1).getId());
    }
}
