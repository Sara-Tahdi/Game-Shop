package ca.mcgill.ecse321.gamecenter.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import ca.mcgill.ecse321.gamecenter.service.GameCenterService;
import ca.mcgill.ecse321.gamecenter.dto.GameCenterDTO;
import ca.mcgill.ecse321.gamecenter.model.GameCenter;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/gamecenter")
public class GameCenterController {
    @Autowired
    private GameCenterService gameCenterService;

    @PutMapping(value = {"/createGameCenter", "/createGameCenter/"})
    public ResponseEntity<?> createorUpdateGameCenter(@RequestBody GameCenterDTO gameCenterDTO) {
        try {
            GameCenter gameCenter = convertToModel(gameCenterDTO);
            return ResponseEntity.ok().body(new GameCenterDTO(gameCenterService.saveOrUpdateGameCenter(gameCenter)));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value="")
    public ResponseEntity<?> getStoreInfo() {
        try {
            return ResponseEntity.ok().body(new GameCenterDTO(gameCenterService.getGameCenter()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Helper methods to convert between model and DTO
    private GameCenter convertToModel(GameCenterDTO dto) {
        return new GameCenter(dto.getName(), dto.getOpen(), dto.getClose(), dto.getStorePolicy());
    }



}
