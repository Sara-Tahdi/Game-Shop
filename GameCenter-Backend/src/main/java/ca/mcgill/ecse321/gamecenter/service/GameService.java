package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public Game createGame(String title, float price, String description, Game.GeneralFeeling generalFeeling, GameCategory category) {
        Game g = new Game(title, price, description, 0, 0, false, generalFeeling, category);
        return gameRepository.save(g);
    }
}
