package ca.mcgill.ecse321.gamecenter.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import ca.mcgill.ecse321.gamecenter.service.GameCenterService;
import ca.mcgill.ecse321.gamecenter.dto.GameCenterDTO;
import ca.mcgill.ecse321.gamecenter.model.GameCenter;

@RestController
@RequestMapping("/gamecenter")
public class GameCenterController {
    @Autowired
    private GameCenterService gameCenterService;

    @PutMapping(value = {"/createGameCenter", "/createGameCenter/"})
    public GameCenterDTO createorUpdateGameCenter(@RequestBody GameCenterDTO gameCenterDTO) {
        GameCenter gameCenter=convertToModel(gameCenterDTO);
        return new GameCenterDTO(gameCenterService.saveOrUpdateGameCenter(gameCenter));
    }

    @GetMapping(value="")
    public GameCenterDTO getStoreInfo() {
        return new GameCenterDTO(gameCenterService.getGameCenter());
    }


    // Helper methods to convert between model and DTO
    private GameCenter convertToModel(GameCenterDTO dto) {
        return new GameCenter(dto.getName(), dto.getOpen(), dto.getClose(), dto.getStorePolicy());
    }



}
