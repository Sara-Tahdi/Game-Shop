package main.java.ca.mcgill.ecse321.gamecenter.service;
import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.repository.GameCenterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class GameCenterService {
    @Autowired
    private GameCenterRepository gameCenterRepository;

    public GameCenter saveOrUpdateGameCenter(GameCenter gameCenter) {
        if (gameCenter == null||gameCenter.getGameCenterName()==null||gameCenter.getGameCenterName().isEmpty()||GameCentre.getOpen()==null|| gameCenter.getClose() == null) {
            throw new IllegalArgumentException("GameCenter name, opening time, and closing time are required.");
            
        }
        // Check if updating existing game center
        if(gameCenterRepository.existsById(gameCenter.getGameCenterName())){
            GameCenter existingGameCenter = gameCenterRepository.findById(gameCenter.getName())
            .orElseThrow(() -> new IllegalArgumentException("GameCenter not found"));
            existingGameCenter.setOpen(gameCenter.getOpen());
            existingGameCenter.setClose(gameCenter.getClose());
            existingGameCenter.setStorePolicy(gameCenter.getStorePolicy());
            return gameCenterRepository.save(existingGameCenter);
        
        }
        else{
            return gameCenterRepository.save(gameCenter);
        }
    }
        
    
}
