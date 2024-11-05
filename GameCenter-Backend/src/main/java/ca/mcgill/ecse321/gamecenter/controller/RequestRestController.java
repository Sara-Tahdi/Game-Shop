package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.Requests.RequestResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Requests.GameRequestRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Requests.GameRequestResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Requests.UserRequestRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Requests.UserRequestResponseDTO;

import ca.mcgill.ecse321.gamecenter.model.Request;
import ca.mcgill.ecse321.gamecenter.model.GameRequest;
import ca.mcgill.ecse321.gamecenter.model.UserRequest;

import ca.mcgill.ecse321.gamecenter.service.RequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@RestController
public class RequestRestController {
    @Autowired
    private RequestService requestService;

    @PostMapping(value = "/requests/game/create")
    public GameRequestResponseDTO createGameRequest(@Validated @RequestBody GameRequestRequestDTO gameRequestToCreate) {
        GameRequest g = requestService.addGameRequest(
                gameRequestToCreate.getCreatedRequestUsername(),
                gameRequestToCreate.getGameTitle(),
                gameRequestToCreate.getReason()
        );
        return new GameRequestResponseDTO(g);
    }

    @PostMapping(value = "/requests/user/create")
    public UserRequestResponseDTO createUserRequest(@Validated @RequestBody UserRequestRequestDTO userRequestToCreate) {
        UserRequest u = requestService.flagUser(
                userRequestToCreate.getCreatedRequestUsername(),
                userRequestToCreate.getUserFacingJudgementUsername(),
                userRequestToCreate.getReason()
        );
        return new UserRequestResponseDTO(u);
    }

    @GetMapping(value = "/requests")
    public List<RequestResponseDTO> getRequests() {
        List<Request> requests = requestService.getAllRequests();
        return requests.stream()
                .map(a -> new RequestResponseDTO(a))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/requests/{status}")
    public List<RequestResponseDTO> getRequestsByStatus(@PathVariable String status) {
        Request.Status statusEnum = Request.Status.valueOf(status.toUpperCase());
        List<Request> requests = requestService.getRequestsByStatus(statusEnum);
        return requests.stream()
                .map(a -> new RequestResponseDTO(a))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/requests/{id}")
    public RequestResponseDTO getRequestById(@PathVariable int id) {
        Request r = requestService.getRequestById(id);
        return new RequestResponseDTO(r);
    }

    @GetMapping(value = "/requests/{createdRequestId}")
    public List<RequestResponseDTO> getRequestsByCreatedRequestId(@PathVariable int createdRequestId) {
        List<Request> requests = requestService.getRequestsByCreatedRequestId(createdRequestId);
        return requests.stream()
                .map(a -> new RequestResponseDTO(a))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/requests/{createdRequestUsername}")
    public List<RequestResponseDTO> getRequestsByCreatedRequestUsername(@PathVariable String createdRequestUsername) {
        List<Request> requests = requestService.getRequestsByCreatedRequestUsername(createdRequestUsername);
        return requests.stream()
                .map(a -> new RequestResponseDTO(a))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/requests/{createdRequestEmail}")
    public List<RequestResponseDTO> getRequestsByCreatedRequestEmail(@PathVariable String createdRequestEmail) {
        List<Request> requests = requestService.getRequestsByCreatedRequestEmail(createdRequestEmail);
        return requests.stream()
                .map(a -> new RequestResponseDTO(a))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/requests/game")
    public List<GameRequestResponseDTO> getGameRequests() {
        List<GameRequest> requests = requestService.getAllGameRequests();
        return requests.stream()
                .map(a -> new GameRequestResponseDTO(a))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/requests/game/{gameTitle}")
    public List<GameRequestResponseDTO> getRequestsByGameTitle(@PathVariable String gameTitle) {
        List<GameRequest> requests = requestService.getRequestsByGameTitle(gameTitle);
        return requests.stream()
                .map(a -> new GameRequestResponseDTO(a))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/requests/game/{gameId}")
    public List<GameRequestResponseDTO> getRequestsByGameId(@PathVariable int gameId) {
        List<GameRequest> requests = requestService.getRequestsByGameId(gameId);
        return requests.stream()
                .map(a -> new GameRequestResponseDTO(a))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/requests/user")
    public List<UserRequestResponseDTO> getUserRequests() {
        List<UserRequest> requests = requestService.getAllUserRequests();
        return requests.stream()
                .map(a -> new UserRequestResponseDTO(a))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/requests/user/{userFacingJudgementUsername}")
    public List<UserRequestResponseDTO> getRequestsByUserFacingJudgementUsername(@PathVariable String userFacingJudgementUsername) {
        List<UserRequest> requests = requestService.getRequestsByUserFacingJudgementUsername(userFacingJudgementUsername);
        return requests.stream()
                .map(a -> new UserRequestResponseDTO(a))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/requests/user/{userFacingJudgementId}")
    public List<UserRequestResponseDTO> getRequestsByUserFacingJudgementId(@PathVariable int userFacingJudgementId) {
        List<UserRequest> requests = requestService.getRequestsByUserFacingJudgementId(userFacingJudgementId);
        return requests.stream()
                .map(a -> new UserRequestResponseDTO(a))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/requests/user/{userFacingJudgementEmail}")
    public List<UserRequestResponseDTO> getRequestsByUserFacingJudgementEmail(@PathVariable String userFacingJudgementEmail) {
        List<UserRequest> requests = requestService.getRequestsByUserFacingJudgementEmail(userFacingJudgementEmail);
        return requests.stream()
                .map(a -> new UserRequestResponseDTO(a))
                .collect(Collectors.toList());
    }












    
    
}
