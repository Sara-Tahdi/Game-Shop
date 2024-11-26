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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class RequestRestController {
    @Autowired
    private RequestService requestService;

    @PostMapping(value = "/requests/game/add")
    public ResponseEntity<?> createAddGameRequest(@Validated @RequestBody GameRequestRequestDTO gameRequestToCreate) {
        try {
            GameRequest g = requestService.addGameRequest(
                    gameRequestToCreate.getCreatedRequestUsername(),
                    gameRequestToCreate.getGameTitle(),
                    gameRequestToCreate.getReason()
            );
            return ResponseEntity.ok().body(new GameRequestResponseDTO(g));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/requests/game/remove")
    public ResponseEntity<?> createRemoveGameRequest(@Validated @RequestBody GameRequestRequestDTO gameRequestToCreate) {
        try {
            GameRequest g = requestService.removeGameRequest(
                    gameRequestToCreate.getCreatedRequestUsername(),
                    gameRequestToCreate.getGameTitle(),
                    gameRequestToCreate.getReason()
            );
            return ResponseEntity.ok().body(new GameRequestResponseDTO(g));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/requests/user/flag")
    public ResponseEntity<?> createUserRequest(@Validated @RequestBody UserRequestRequestDTO userRequestToCreate) {
        try {
            UserRequest u = requestService.flagUser(
                    userRequestToCreate.getCreatedRequestUsername(),
                    userRequestToCreate.getUserFacingJudgementUsername(),
                    userRequestToCreate.getReason()
            );
            return ResponseEntity.ok().body(new UserRequestResponseDTO(u));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/requests")
    public ResponseEntity<?> getRequests() {
        try {
            List<Request> requests = requestService.getAllRequests();
            return ResponseEntity.ok().body(requests.stream()
                    .map(a -> new RequestResponseDTO(a))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/requests/status/{status}")
    public ResponseEntity<?> getRequestsByStatus(@PathVariable String status) {
        try {
            Request.Status statusEnum = Request.Status.valueOf(status.toUpperCase());
            List<Request> requests = requestService.getRequestsByStatus(statusEnum);
            return ResponseEntity.ok().body(requests.stream()
                    .map(a -> new RequestResponseDTO(a))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/requests/id/{id}")
    public ResponseEntity<?> getRequestById(@PathVariable int id) {
        try {
            Request r = requestService.getRequestById(id);
            return ResponseEntity.ok().body(new RequestResponseDTO(r));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/requests/creator/id/{createdRequestId}")
    public ResponseEntity<?> getRequestsByCreatedRequestId(@PathVariable int createdRequestId) {
        try {
            List<Request> requests = requestService.getRequestsByCreatedRequestId(createdRequestId);
            return ResponseEntity.ok().body(requests.stream()
                    .map(a -> new RequestResponseDTO(a))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/requests/creator/username/{createdRequestUsername}")
    public ResponseEntity<?> getRequestsByCreatedRequestUsername(@PathVariable String createdRequestUsername) {
        try {
            List<Request> requests = requestService.getRequestsByCreatedRequestUsername(createdRequestUsername);
            return ResponseEntity.ok().body(requests.stream()
                    .map(a -> new RequestResponseDTO(a))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/requests/creator/email/{createdRequestEmail}")
    public ResponseEntity<?> getRequestsByCreatedRequestEmail(@PathVariable String createdRequestEmail) {
        try {
            List<Request> requests = requestService.getRequestsByCreatedRequestEmail(createdRequestEmail);
            return ResponseEntity.ok().body(requests.stream()
                    .map(a -> new RequestResponseDTO(a))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/requests/game")
    public ResponseEntity<?> getGameRequests() {
        try {
            List<GameRequest> requests = requestService.getAllGameRequests();
            return ResponseEntity.ok().body(requests.stream()
                    .map(a -> new GameRequestResponseDTO(a))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/requests/game/title/{gameTitle}")
    public ResponseEntity<?> getRequestsByGameTitle(@PathVariable String gameTitle) {
        try {
            List<GameRequest> requests = requestService.getRequestsByGameTitle(gameTitle);
            return ResponseEntity.ok().body(requests.stream()
                    .map(a -> new GameRequestResponseDTO(a))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/requests/game/id/{gameId}")
    public ResponseEntity<?> getRequestsByGameId(@PathVariable int gameId) {
        try {
            List<GameRequest> requests = requestService.getRequestsByGameId(gameId);
            return ResponseEntity.ok().body(requests.stream()
                    .map(a -> new GameRequestResponseDTO(a))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/requests/user")
    public ResponseEntity<?> getUserRequests() {
        try {
            List<UserRequest> requests = requestService.getAllUserRequests();
            return ResponseEntity.ok().body(requests.stream()
                    .map(a -> new UserRequestResponseDTO(a))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/requests/user/username/{userFacingJudgementUsername}")
    public ResponseEntity<?> getRequestsByUserFacingJudgementUsername(@PathVariable String userFacingJudgementUsername) {
        try {
            List<UserRequest> requests = requestService.getRequestsByUserFacingJudgementUsername(userFacingJudgementUsername);
            return ResponseEntity.ok().body(requests.stream()
                    .map(a -> new UserRequestResponseDTO(a))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/requests/user/id/{userFacingJudgementId}")
    public ResponseEntity<?> getRequestsByUserFacingJudgementId(@PathVariable int userFacingJudgementId) {
        try {
            List<UserRequest> requests = requestService.getRequestsByUserFacingJudgementId(userFacingJudgementId);
            return ResponseEntity.ok().body(requests.stream()
                    .map(a -> new UserRequestResponseDTO(a))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/requests/user/email/{userFacingJudgementEmail}")
    public ResponseEntity<?> getRequestsByUserFacingJudgementEmail(@PathVariable String userFacingJudgementEmail) {
        try {
            List<UserRequest> requests = requestService.getRequestsByUserFacingJudgementEmail(userFacingJudgementEmail);
            return ResponseEntity.ok().body(requests.stream()
                    .map(a -> new UserRequestResponseDTO(a))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/requests/approve/{id}")
    public ResponseEntity<?> approveRequest(@PathVariable int id) {
        try {
            Request r = requestService.handleRequestApproval(id, true);
            return ResponseEntity.ok().body(new RequestResponseDTO(r));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/requests/deny/{id}")
    public ResponseEntity<?> denyRequest(@PathVariable int id) {
        try {
            Request r = requestService.handleRequestApproval(id, false);
            return ResponseEntity.ok().body(new RequestResponseDTO(r));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
