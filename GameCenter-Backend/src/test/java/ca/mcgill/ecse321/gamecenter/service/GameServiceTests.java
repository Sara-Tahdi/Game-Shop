package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.repository.GameCategoryRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GameServiceTests {
    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameCategoryRepository gameCategoryRepository;  // Add this line
    @InjectMocks
    private GameService gameService;
    

    /* Getter Tests */

    @Test
    void testGetAllGamesError() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.getAllGame());
        assertEquals("There are no Games", e.getMessage());
    }

    @Test
    void testGetAllUsers() {
        String title1 = "Rayman Legends";
        float price1 = 79.99F;
        String description1 = "A fun platformer!";
        float rating1 = 4.5F;
        int remainingQuantity1 = 20;
        boolean isOffered1 = true;
        Game.GeneralFeeling publicOpinion1 = Game.GeneralFeeling.POSITIVE;
        GameCategory category1 = new GameCategory("Platformer");
        Game g1 = new Game(title1, price1, description1, rating1, remainingQuantity1, isOffered1, publicOpinion1, category1, "");
        when(gameRepository.save(any(Game.class))).thenReturn(g1);
        Game createdGame1 = gameService.createGame(title1, price1, description1, publicOpinion1, category1);

        String title2 = "Sonic '06";
        float price2 = 19.99F;
        String description2 = "Sonic revisited in 3D!";
        float rating2 = 1.1F;
        int remainingQuantity2 = 3;
        boolean isOffered2 = false;
        Game.GeneralFeeling publicOpinion2 = Game.GeneralFeeling.VERYNEGATIVE;
        Game g2 = new Game(title2, price2, description2, rating2, remainingQuantity2, isOffered2, publicOpinion2, category1, "");
        when(gameRepository.save(any(Game.class))).thenReturn(g2);
        Game createdGame2 = gameService.createGame(title2, price2, description2,  publicOpinion2, category1);


        String title3 = "GTA 5";
        float price3 = 59.99F;
        String description3 = "We got GTA 5 before GTA 6!";
        float rating3 = 4.9F;
        int remainingQuantity3 = 30;
        boolean isOffered3 = true;
        Game.GeneralFeeling publicOpinion3 = Game.GeneralFeeling.VERYPOSITIVE;
        GameCategory category3 = new GameCategory("Open World");
        Game g3 = new Game(title3, price3, description3, rating3, remainingQuantity3, isOffered3, publicOpinion3, category3, "");
        when(gameRepository.save(any(Game.class))).thenReturn(g3);
        Game createdGame3 = gameService.createGame(title3, price3, description3, publicOpinion3, category3);

        when(gameRepository.findGameByGameType(Game.class)).thenReturn(Optional.of(List.of(
                createdGame1, createdGame2, createdGame3
        )));
        List<Game> games = gameService.getAllGame();

        assertNotNull(games);
        assertEquals(3, games.size());
    }

    @Test
    void testCreateGameSimpleInvalidPrice() {
        String title = "Rayman Legends";
        float invalidPrice = -79.99F;
        String description = "A fun platformer!";
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");

        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () ->
                gameService.createGame(title, invalidPrice, description, publicOpinion, category));
        assertEquals("Price is not valid", e1.getMessage());

        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () ->
                gameService.createGame(title, 0F, description, publicOpinion, category));
        assertEquals("Price is not valid", e2.getMessage());
    }

    @Test
    void testCreateGameExistingTitle() {
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");

        Game existingGame = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        when(gameRepository.findGameByTitle(title)).thenReturn(Optional.of(existingGame));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.createGame(title, price, description, publicOpinion, category));
        assertEquals("Game already exists with title: " + title, e.getMessage());
    }

    @Test
    void testCreateGameExistingDescription() {
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");

        when(gameRepository.findGameByTitle(title)).thenReturn(Optional.empty());
        Game existingGame = new Game("Other Game", price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        when(gameRepository.findGameByDescription(description)).thenReturn(Optional.of(existingGame));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.createGame(title, price, description, publicOpinion, category));
        assertEquals("Game already exists with description: " + title, e.getMessage());
    }

    @Test
    void testCreateGameExistingTitleInCategory() {
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");

        when(gameRepository.findGameByTitle(title)).thenReturn(Optional.empty());
        when(gameRepository.findGameByDescription(description)).thenReturn(Optional.empty());

        List<Game> categoryGames = new ArrayList<>();
        categoryGames.add(new Game(title, price, "Different description", rating, remainingQuantity, isOffered, publicOpinion, category, ""));
        when(gameRepository.findGamesByGameCategory(category.getCategory())).thenReturn(Optional.of(categoryGames));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.createGame(title, price, description, publicOpinion, category));
        assertEquals("Game already exists with description: " + description + " and title: " + title, e.getMessage());
    }

    @Test
    void testCreateGameInvalidPrice() {
        String title = "Rayman Legends";
        String description = "A fun platformer!";
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");

        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () ->
                gameService.createGame(title, null, description, publicOpinion, category));
        assertEquals("Price is not valid", e1.getMessage());

        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () ->
                gameService.createGame(title, -79.99F, description, publicOpinion, category));
        assertEquals("Price is not valid", e2.getMessage());
    }
    
    @Test
    void testGetGameById(){
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");
        Game g = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        g.setId(23);
        
        when(gameRepository.save(any(Game.class))).thenReturn(g);
        gameService.createGame(title, price, description, publicOpinion, category);

        when(gameRepository.findGameById(g.getId())).thenReturn(Optional.of(g));
        Game gg = gameService.getGameById(g.getId());

        assertInstanceOf(Game.class, gg);
        assertEquals(title, gg.getTitle());
        assertEquals(23, gg.getId());
        assertEquals(79.99F, gg.getPrice());
        assertEquals("A fun platformer!", gg.getDescription());
        assertEquals(4.5F, gg.getRating());
        assertEquals(20, gg.getRemainingQuantity());
        assertTrue(gg.getIsOffered());
        assertEquals(Game.GeneralFeeling.POSITIVE, gg.getPublicOpinion());
        assertEquals("Platformer", gg.getCategory().getCategory());
    }

    @Test
    void testGetGameByIdFail(){
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameById(2));
        assertEquals("There is no Game with id: " + 2, e.getMessage());
    }


    @Test
    void testGetGameByTitle(){
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");
        Game g = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        g.setId(23);

        when(gameRepository.save(any(Game.class))).thenReturn(g);
        gameService.createGame(title, price, description, publicOpinion, category);

        when(gameRepository.findGameByTitle(g.getTitle())).thenReturn(Optional.of(g));
        Game gg = gameService.getGameByTitle(g.getTitle());

        assertInstanceOf(Game.class, gg);
        assertEquals(title, gg.getTitle());
        assertEquals(23, gg.getId());
        assertEquals(79.99F, gg.getPrice());
        assertEquals("A fun platformer!", gg.getDescription());
        assertEquals(4.5F, gg.getRating());
        assertEquals(20, gg.getRemainingQuantity());
        assertTrue(gg.getIsOffered());
        assertEquals(Game.GeneralFeeling.POSITIVE, gg.getPublicOpinion());
        assertEquals("Platformer", gg.getCategory().getCategory());
    }

    @Test
    void testGetGameByTitleFail(){
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameByTitle("Rayman Legends"));
        assertEquals("There is no Game with title: " + "Rayman Legends", e.getMessage());
    }

    @Test
    void testGetGameByCategoryId(){
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");
        category.setId(23);
        Game g = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        g.setId(23);

        when(gameCategoryRepository.findGameCategoryById(2)).thenReturn(Optional.empty());

        when(gameRepository.save(any(Game.class))).thenReturn(g);
        gameService.createGame(title, price, description, publicOpinion, category);

        List<Game> gameList = new ArrayList<>();
        gameList.add(g);

        when(gameRepository.findGamesByGameCategoryId(g.getCategory().getId())).thenReturn(Optional.of(gameList));
        List<Game> gg = gameService.getGameByCategoryId(g.getCategory().getId());

        assertNotNull(gg);
        Game game = gg.getFirst();
        assertInstanceOf(Game.class, game);
        assertEquals(title, game.getTitle());
        assertEquals(23, game.getId());
        assertEquals(23, game.getCategory().getId());
        assertEquals(79.99F, game.getPrice());
        assertEquals("A fun platformer!", game.getDescription());
        assertEquals(4.5F, game.getRating());
        assertEquals(20, game.getRemainingQuantity());
        assertTrue(game.getIsOffered());
        assertEquals(Game.GeneralFeeling.POSITIVE, game.getPublicOpinion());
        assertEquals("Platformer", game.getCategory().getCategory());
    }

    @Test
    void testGetGameByCategoryIdFail(){
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameByCategoryId(2));
        assertEquals("There is no Game with category whose ID is: 2" , e.getMessage());
    }

    @Test
    void testGetGameByCategory(){
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");
        category.setId(23);
        Game g = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        g.setId(23);

        when(gameCategoryRepository.findGameCategoryByCategory("Platformer")).thenReturn(Optional.empty());

        when(gameRepository.save(any(Game.class))).thenReturn(g);
        gameService.createGame(title, price, description, publicOpinion, category);

        List<Game> gameList = new ArrayList<>();
        gameList.add(g);

        when(gameRepository.findGamesByGameCategory(g.getCategory().getCategory())).thenReturn(Optional.of(gameList));
        List<Game> gg = gameService.getGameByCategory(g.getCategory().getCategory());

        assertNotNull(gg);
        Game game = gg.getFirst();
        assertInstanceOf(Game.class, game);
        assertEquals(title, game.getTitle());
        assertEquals(23, game.getId());
        assertEquals(23, game.getCategory().getId());
        assertEquals(79.99F, game.getPrice());
        assertEquals("A fun platformer!", game.getDescription());
        assertEquals(4.5F, game.getRating());
        assertEquals(20, game.getRemainingQuantity());
        assertTrue(game.getIsOffered());
        assertEquals(Game.GeneralFeeling.POSITIVE, game.getPublicOpinion());
        assertEquals("Platformer", game.getCategory().getCategory());
    }

    @Test
    void testGetGameByCategoryFail(){
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameByCategory("Platformer"));
        assertEquals("There is no Game with category: Platformer" , e.getMessage());
    }

    @Test
    void testGetGameByPriceRange() {
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");
        Game g = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        g.setId(23);

        List<Game> gamesList = new ArrayList<>();
        gamesList.add(g);

        when(gameRepository.findGamesByPriceRange(70.0F, 80.0F)).thenReturn(Optional.of(gamesList));

        List<Game> result = gameService.getGameByPriceRange(70.0F, 80.0F);

        assertNotNull(result);
        Game game = result.getFirst();
        assertInstanceOf(Game.class, game);
        assertEquals(title, game.getTitle());
        assertEquals(23, game.getId());
        assertEquals(79.99F, game.getPrice());
        assertEquals(description, game.getDescription());
        assertEquals(4.5F, game.getRating());
        assertEquals(remainingQuantity, game.getRemainingQuantity());
        assertTrue(game.getIsOffered());
        assertEquals(publicOpinion, game.getPublicOpinion());
        assertEquals("Platformer", game.getCategory().getCategory());
    }

    @Test
    void testGetGameByPriceRangeInvalidRange() {
        // Test invalid range (min > max)
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameByPriceRange(80.0F, 70.0F));
        assertEquals("Invalid price range", e1.getMessage());

        // Test null values
        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameByPriceRange(null, 80.0F));
        assertEquals("Invalid price range", e2.getMessage());

        IllegalArgumentException e3 = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameByPriceRange(70.0F, null));
        assertEquals("Invalid price range", e3.getMessage());
    }

    @Test
    void testGetGameByPriceRangeNoGamesFound() {
        when(gameRepository.findGamesByPriceRange(70.0F, 80.0F)).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameByPriceRange(70.0F, 80.0F));
        assertEquals("There is no Game within price range: [70.0, 80.0]", e.getMessage());
    }

    @Test
    void testGetGameByRatingRange() {
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");
        Game g = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        g.setId(23);

        List<Game> gamesList = new ArrayList<>();
        gamesList.add(g);

        when(gameRepository.findGamesByRatingRange(4.0F, 5.0F)).thenReturn(Optional.of(gamesList));

        List<Game> result = gameService.getGameByRatingRange(4.0F, 5.0F);

        assertNotNull(result);
        Game game = result.getFirst();
        assertInstanceOf(Game.class, game);
        assertEquals(title, game.getTitle());
        assertEquals(23, game.getId());
        assertEquals(79.99F, game.getPrice());
        assertEquals(description, game.getDescription());
        assertEquals(4.5F, game.getRating());
        assertEquals(remainingQuantity, game.getRemainingQuantity());
        assertTrue(game.getIsOffered());
        assertEquals(publicOpinion, game.getPublicOpinion());
        assertEquals("Platformer", game.getCategory().getCategory());
    }

    @Test
    void testGetGameByRatingRangeInvalidRange() {
        // Test invalid range (min > max)
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameByRatingRange(4.0F, 2.0F));
        assertEquals("Invalid rating range. Ratings must be between 0 and 5, and minimum rating must not exceed maximum rating",
                e1.getMessage());

        // Test out-of-bounds ratings
        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameByRatingRange(-1.0F, 5.0F));
        assertEquals("Invalid rating range. Ratings must be between 0 and 5, and minimum rating must not exceed maximum rating",
                e2.getMessage());

        IllegalArgumentException e3 = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameByRatingRange(0.0F, 6.0F));
        assertEquals("Invalid rating range. Ratings must be between 0 and 5, and minimum rating must not exceed maximum rating",
                e3.getMessage());

        // Test null values
        IllegalArgumentException e4 = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameByRatingRange(null, 5.0F));
        assertEquals("Rating range cannot be null", e4.getMessage());
    }

    @Test
    void testGetGameByRatingRangeNoGamesFound() {
        when(gameRepository.findGamesByRatingRange(4.0F, 5.0F)).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameByRatingRange(4.0F, 5.0F));
        assertEquals("There is no Game within rating range: [4.0, 5.0]", e.getMessage());
    }

    @Test
    void testGetGameByDescription(){
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");
        Game g = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        g.setId(23);

        when(gameRepository.save(any(Game.class))).thenReturn(g);
        gameService.createGame(title, price, description, publicOpinion, category);

        when(gameRepository.findGameByDescription(g.getDescription())).thenReturn(Optional.of(g));
        Game gg = gameService.getGameByDescription(g.getDescription());

        assertInstanceOf(Game.class, gg);
        assertEquals(title, gg.getTitle());
        assertEquals(23, gg.getId());
        assertEquals(79.99F, gg.getPrice());
        assertEquals("A fun platformer!", gg.getDescription());
        assertEquals(4.5F, gg.getRating());
        assertEquals(20, gg.getRemainingQuantity());
        assertTrue(gg.getIsOffered());
        assertEquals(Game.GeneralFeeling.POSITIVE, gg.getPublicOpinion());
        assertEquals("Platformer", gg.getCategory().getCategory());
    }

    @Test
    void testGetGameByDescriptionFail(){
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.getGameByDescription("A fun platformer!"));
        assertEquals("There is no Game with description: A fun platformer!", e.getMessage());
    }

    @Test
    void testUpdateGameSuccess() {
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");
        Game originalGame = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        originalGame.setId(23);

        String newTitle = "Rayman Legends Definitive Edition";
        float newPrice = 89.99F;
        String newDescription = "The ultimate platformer!";
        float newRating = 4.8F;
        int newQuantity = 25;
        boolean newIsOffered = true;
        GameCategory newCategory = new GameCategory("Action Platformer");

        when(gameRepository.findGameById(23)).thenReturn(Optional.of(originalGame));
        when(gameRepository.findGameByTitle(newTitle)).thenReturn(Optional.empty());
        when(gameRepository.findGameByDescription(newDescription)).thenReturn(Optional.empty());
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Game updatedGame = gameService.updateGame(23, newTitle, newPrice, newDescription,
                newRating, newQuantity, newIsOffered, newCategory);

        assertNotNull(updatedGame);
        assertEquals(newTitle, updatedGame.getTitle());
        assertEquals(newPrice, updatedGame.getPrice());
        assertEquals(newDescription, updatedGame.getDescription());
        assertEquals(newRating, updatedGame.getRating());
        assertEquals(newQuantity, updatedGame.getRemainingQuantity());
        assertEquals(newIsOffered, updatedGame.getIsOffered());
        assertEquals(newCategory, updatedGame.getCategory());
    }

    @Test
    void testUpdateGameNonExistentId() {
        when(gameRepository.findGameById(999)).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.updateGame(999, "Rayman Legends", 79.99F, "A fun platformer!",
                        4.5F, 20, true, new GameCategory("Platformer")));
        assertEquals("There is no Game with id: 999", e.getMessage());
    }

    @Test
    void testUpdateGameDuplicateTitle() {
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");
        Game originalGame = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        originalGame.setId(23);

        Game existingGame = new Game("Mario Odyssey", 89.99F, "A 3D platformer!",
                4.9F, 15, true, Game.GeneralFeeling.POSITIVE, new GameCategory("Platformer"), "");
        existingGame.setId(24);

        when(gameRepository.findGameById(23)).thenReturn(Optional.of(originalGame));
        when(gameRepository.findGameByTitle("Mario Odyssey")).thenReturn(Optional.of(existingGame));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.updateGame(23, "Mario Odyssey", 89.99F, "New Description",
                        4.5F, 20, true, category));
        assertEquals("There already exists a Game with title: Mario Odyssey", e.getMessage());
    }

    @Test
    void testUpdateGameInvalidPrice() {
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");
        Game originalGame = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        originalGame.setId(23);

        when(gameRepository.findGameById(23)).thenReturn(Optional.of(originalGame));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.updateGame(23, title, -10.0F, description,
                        rating, remainingQuantity, isOffered, category));
        assertEquals("Price is not valid", e.getMessage());
    }

    @Test
    void testUpdateGameInvalidRating() {
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");
        Game originalGame = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        originalGame.setId(23);

        when(gameRepository.findGameById(23)).thenReturn(Optional.of(originalGame));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.updateGame(23, title, price, description,
                        5.5F, remainingQuantity, isOffered, category));
        assertEquals("Rating is not valid", e.getMessage());
    }

    @Test
    void testUpdateGameInvalidQuantity() {
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");
        Game originalGame = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        originalGame.setId(23);

        when(gameRepository.findGameById(23)).thenReturn(Optional.of(originalGame));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.updateGame(23, title, price, description,
                        rating, -1, isOffered, category));
        assertEquals("Remaining quantity is not valid", e.getMessage());
    }

    @Test
    void testMakeGameOfferedSuccess() {
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = false;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");

        Game game = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        game.setId(23);

        when(gameRepository.findGameById(23)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Game updatedGame = gameService.makeGameOffered(23);

        assertNotNull(updatedGame);
        assertTrue(updatedGame.getIsOffered());
    }

    @Test
    void testMakeGameOfferedAlreadyOffered() {
        Game game = new Game("Rayman Legends", 79.99F, "A fun platformer!", 4.5F, 20, true,
                Game.GeneralFeeling.POSITIVE, new GameCategory("Platformer"), "");
        game.setId(23);

        when(gameRepository.findGameById(23)).thenReturn(Optional.of(game));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.makeGameOffered(23));
        assertEquals("Game is already offered", e.getMessage());
    }

    @Test
    void testMakeGameOfferedNoQuantity() {
        Game game = new Game("Rayman Legends", 79.99F, "A fun platformer!", 4.5F, 0, false,
                Game.GeneralFeeling.POSITIVE, new GameCategory("Platformer"), "");
        game.setId(23);

        when(gameRepository.findGameById(23)).thenReturn(Optional.of(game));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.makeGameOffered(23));
        assertEquals("Cannot offer game with no remaining quantity", e.getMessage());
    }

    @Test
    void testMakeGameOfferedNonExistent() {
        when(gameRepository.findGameById(999)).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.makeGameOffered(999));
        assertEquals("There is no Game with id: 999", e.getMessage());
    }

    @Test
    void testMakeGameNotOfferedSuccess() {
        String title = "Rayman Legends";
        float price = 79.99F;
        String description = "A fun platformer!";
        float rating = 4.5F;
        int remainingQuantity = 20;
        boolean isOffered = true;
        Game.GeneralFeeling publicOpinion = Game.GeneralFeeling.POSITIVE;
        GameCategory category = new GameCategory("Platformer");

        Game game = new Game(title, price, description, rating, remainingQuantity, isOffered, publicOpinion, category, "");
        game.setId(23);

        when(gameRepository.findGameById(23)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Game updatedGame = gameService.makeGameNotOffered(23);

        assertNotNull(updatedGame);
        assertFalse(updatedGame.getIsOffered());
    }

    @Test
    void testMakeGameNotOfferedAlreadyNotOffered() {
        Game game = new Game("Rayman Legends", 79.99F, "A fun platformer!", 4.5F, 20, false,
                Game.GeneralFeeling.POSITIVE, new GameCategory("Platformer"), "");
        game.setId(23);

        when(gameRepository.findGameById(23)).thenReturn(Optional.of(game));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.makeGameNotOffered(23));
        assertEquals("Game is already not offered", e.getMessage());
    }

    @Test
    void testMakeGameNotOfferedNonExistent() {
        when(gameRepository.findGameById(999)).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.makeGameNotOffered(999));
        assertEquals("There is no Game with id: 999", e.getMessage());
    }

    @Test
    void testGetAllAvailableGamesSuccess() {
        List<Game> games = new ArrayList<>();
        games.add(new Game("Rayman Legends", 79.99F, "A fun platformer!", 4.5F, 20, true,
                Game.GeneralFeeling.POSITIVE, new GameCategory("Platformer"), ""));
        games.add(new Game("Super Mario Odyssey", 89.99F, "A 3D platformer!", 4.9F, 15, true,
                Game.GeneralFeeling.POSITIVE, new GameCategory("3D Platformer"), ""));

        when(gameRepository.findAllAvailableGames()).thenReturn(Optional.of(games));

        List<Game> foundGames = gameService.getAllAvailableGames();

        assertNotNull(foundGames);
        assertEquals(2, foundGames.size());
        assertTrue(foundGames.stream().allMatch(Game::getIsOffered));
    }

    @Test
    void testGetAllAvailableGamesEmpty() {
        when(gameRepository.findAllAvailableGames()).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.getAllAvailableGames());
        assertEquals("There are no available games", e.getMessage());
    }

    @Test
    void testGetAllAvailableGamesByCategorySuccess() {
        String category = "Platformer";
        List<Game> games = new ArrayList<>();
        games.add(new Game("Rayman Legends", 79.99F, "A fun platformer!", 4.5F, 20, true,
                Game.GeneralFeeling.POSITIVE, new GameCategory(category), ""));

        when(gameRepository.findAllAvailableGamesByGameCategory(category))
                .thenReturn(Optional.of(games));

        List<Game> foundGames = gameService.getAllAvailableGamesByCategory(category);

        assertNotNull(foundGames);
        assertEquals(1, foundGames.size());
        assertTrue(foundGames.stream().allMatch(g ->
                g.getCategory().getCategory().equals(category) && g.getIsOffered()));
    }

    @Test
    void testGetAllAvailableGamesByCategoryEmpty() {
        String category = "Platformer";
        when(gameRepository.findAllAvailableGamesByGameCategory(category))
                .thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                gameService.getAllAvailableGamesByCategory(category));
        assertEquals("There are no available games in category: Platformer", e.getMessage());
    }

    @Test
    void testGetAllAvailableGamesByPriceRangeSuccess() {
        float minPrice = 70.0F;
        float maxPrice = 90.0F;
        List<Game> games = new ArrayList<>();
        games.add(new Game("Rayman Legends", 79.99F, "A fun platformer!", 4.5F, 20, true,
                Game.GeneralFeeling.POSITIVE, new GameCategory("Platformer"), ""));

        when(gameRepository.findAllAvailableGamesByPriceRange(minPrice, maxPrice))
                .thenReturn(Optional.of(games));

        List<Game> foundGames = gameService.getAllAvailableGamesByPriceRange(minPrice, maxPrice);

        assertNotNull(foundGames);
        assertTrue(foundGames.stream().allMatch(g ->
                g.getPrice() >= minPrice && g.getPrice() <= maxPrice && g.getIsOffered()));
    }

    @Test
    void testGetAllAvailableGamesByRatingRangeSuccess() {
        float minRating = 4.0F;
        float maxRating = 5.0F;
        List<Game> games = new ArrayList<>();
        games.add(new Game("Rayman Legends", 79.99F, "A fun platformer!", 4.5F, 20, true,
                Game.GeneralFeeling.POSITIVE, new GameCategory("Platformer"), ""));

        when(gameRepository.findAllAvailableGamesByRatingRange(minRating, maxRating))
                .thenReturn(Optional.of(games));

        List<Game> foundGames = gameService.getAllAvailableGamesByRatingRange(minRating, maxRating);

        assertNotNull(foundGames);
        assertTrue(foundGames.stream().allMatch(g ->
                g.getRating() >= minRating && g.getRating() <= maxRating && g.getIsOffered()));
    }

    @Test
    void testGetAllAvailableGamesByRatingRangeInvalid() {
        // Test min > max
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () ->
                gameService.getAllAvailableGamesByRatingRange(5.0F, 4.0F));
        assertEquals("Minimum rating cannot be greater than maximum rating", e1.getMessage());

        // Test out of range values
        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () ->
                gameService.getAllAvailableGamesByRatingRange(-1.0F, 5.0F));
        assertEquals("Rating must be between 0 and 5", e2.getMessage());

        IllegalArgumentException e3 = assertThrows(IllegalArgumentException.class, () ->
                gameService.getAllAvailableGamesByRatingRange(0.0F, 6.0F));
        assertEquals("Rating must be between 0 and 5", e3.getMessage());
    }

}
