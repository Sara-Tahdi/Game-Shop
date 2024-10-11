package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.GameCenter;
import ca.mcgill.ecse321.gamecenter.repository.GameCenterRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class GameCenterTests {
    @Autowired
    private GameCenterRepository gameCenterRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        gameCenterRepository.deleteAll();
    }




    @Test
    public void testGetGameCenterByName() {
        String name = "GameCenter";
        Time open = Time.valueOf("09:00:00");
        Time close = Time.valueOf("21:00:00");
        String storePolicy = "Policy";

        GameCenter gameCenter_repo = new GameCenter(name, open, close, storePolicy);
        gameCenter_repo = gameCenterRepository.save(gameCenter_repo);

        GameCenter gameCenter_repo_db = gameCenterRepository.findGameCenterByName(name).orElse(null);

        assertNotNull(gameCenter_repo_db);
        assertEquals(gameCenter_repo_db.getName(), name);
        assertEquals(gameCenter_repo_db.getOpen(), open);
        assertEquals(gameCenter_repo_db.getClose(), close);
        assertEquals(gameCenter_repo_db.getStorePolicy(), storePolicy);
    }

}
