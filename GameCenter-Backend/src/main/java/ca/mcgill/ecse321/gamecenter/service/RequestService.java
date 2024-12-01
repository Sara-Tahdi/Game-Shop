package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.*;

import ca.mcgill.ecse321.gamecenter.repository.RequestRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.StaffRepository;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private GameService gameService;
    
    public List<Request> getAllRequests() {
        List<Request> requests = new ArrayList<>();
        requestRepository.findAll().forEach(requests::add);
        if (requests.isEmpty()) {
            throw new IllegalArgumentException("There are no Requests");
        }
        return requests;
    }

    public Request getRequestById(int id) {
        Request a = requestRepository.findRequestById(id).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There is no Request with id: " + id);
        }
        return a;
    }

    public List<Request> getRequestsByCreatedRequestId(int createdRequestId) {
        List<Request> a = requestRepository.findRequestsByCreatedRequestId(createdRequestId).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There are no Requests with createdRequestId: " + createdRequestId);
        }
        return a;
    }

    public List<Request> getRequestsByCreatedRequestUsername(String createdRequestsUsername) {
        List<Request> a = requestRepository.findRequestsByCreatedRequestUsername(createdRequestsUsername).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There are no Requests with createdRequestsUsername: " + createdRequestsUsername);
        }
        return a;
    }

    public List<Request> getRequestsByCreatedRequestEmail(String createdRequestEmail) {
        List<Request> a = requestRepository.findRequestsByCreatedRequestEmail(createdRequestEmail).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There are no Requests with createdRequestEmail: " + createdRequestEmail);
        }
        return a;
    }

    public List<GameRequest> getRequestsByGameTitle(String gameTitle) {
        List<GameRequest> a = requestRepository.findRequestsByGameTitle(gameTitle).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There are no Requests with gameTitle: " + gameTitle);
        }
        return a;
    }

    public List<GameRequest> getRequestsByGameId(int gameId) {
        List<GameRequest> a = requestRepository.findRequestsByGameId(gameId).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There are no Requests with gameId: " + gameId);
        }
        return Arrays.asList(a.toArray(new GameRequest[a.size()]));
    }

    public List<UserRequest> getRequestsByUserFacingJudgementId(int userFacingJudgementId) {
        List<UserRequest> a = requestRepository.findRequestsByUserFacingJudgementId(userFacingJudgementId).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There are no Requests with userFacingJudgementId: " + userFacingJudgementId);
        }
        return a;
    }

    public List<UserRequest> getRequestsByUserFacingJudgementUsername(String userFacingJudgementUsername) {
        List<UserRequest> a = requestRepository.findRequestsByUserFacingJudgementUsername(userFacingJudgementUsername).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There are no Requests with userFacingJudgementUsername: " + userFacingJudgementUsername);
        }
        return a;
    }

    public List<UserRequest> getRequestsByUserFacingJudgementEmail(String userFacingJudgementEmail) {
        List<UserRequest> a = requestRepository.findRequestsByUserFacingJudgementEmail(userFacingJudgementEmail).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There are no Requests with userFacingJudgementEmail: " + userFacingJudgementEmail);
        }
        return a;
    }

    public List<GameRequest> getAllGameRequests() {
        List<Request> requests = requestRepository.findRequestsByRequestType(GameRequest.class).orElse(null);
        if (requests == null) {
            throw new IllegalArgumentException("There are no GameRequests");
        }
    
        // Sort the list by ID
        return requests.stream()
                       .map(request -> (GameRequest) request)
                       .sorted(Comparator.comparing(GameRequest::getId))
                       .collect(Collectors.toList());
    }

    public List<UserRequest> getAllUserRequests() {
    List<Request> requests = requestRepository.findRequestsByRequestType(UserRequest.class).orElse(null);

    if (requests == null) {
        throw new IllegalArgumentException("There are no UserRequests");
    }

    // Sort the list by ID
    return requests.stream()
                   .map(request -> (UserRequest) request)
                   .sorted(Comparator.comparing(UserRequest::getId)) // Assuming `getId` exists
                   .collect(Collectors.toList());
    }

    public List<Request> getRequestsByStatus(Request.Status status) {
        List<Request> a = requestRepository.findRequestsByStatus(status).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There are no Requests with status: " + status);
        }
        return a;
    }

    @Transactional
    public UserRequest flagUser(String aStaffUsername, String aClientUsername, String reason) {
        Staff staff = (Staff) staffRepository.findStaffByUsername(aStaffUsername).orElse(null);
        if (staff == null || !staff.isIsActive()) {
            throw new IllegalArgumentException("There is no Staff with username: " + aStaffUsername);
        }
        Client client = (Client) clientRepository.findClientByUsername(aClientUsername).orElse(null);
        if (client == null) {
            throw new IllegalArgumentException("There is no Client with username: " + aClientUsername);
        }
        List<Request> staffRequests = requestRepository.findRequestsByCreatedRequestUsername(aStaffUsername).orElse(null);
        if (staffRequests != null) {
            for (Request r : staffRequests) {
                if (r instanceof UserRequest && ((UserRequest) r).getUserFacingJudgement().getUsername().equals(aClientUsername)) {
                    throw new IllegalArgumentException("There is already a request from " + aStaffUsername + " regarding " + aClientUsername);
                }
            }
        }
        UserRequest userRequest = new UserRequest(Request.Status.PENDING, reason, staff, client);
        return requestRepository.save(userRequest);
    }

    @Transactional
    public GameRequest addGameRequest(String aStaffUsername, String aGameTitle, String reason) {
        return createGameRequest(aStaffUsername, aGameTitle, GameRequest.Type.ADD, reason);
    }

    @Transactional
    public GameRequest removeGameRequest(String aStaffUsername, String aGameTitle, String reason) {
        return createGameRequest(aStaffUsername, aGameTitle, GameRequest.Type.REMOVE, reason);
    }

    private GameRequest createGameRequest(String aStaffUsername, String aGameTitle, GameRequest.Type requestType, String reason) {
        Staff staff = (Staff) staffRepository.findStaffByUsername(aStaffUsername).orElse(null);
        if (staff == null  || !staff.isIsActive()) {
            throw new IllegalArgumentException("There is no Staff with username: " + aStaffUsername);
        }
    
        Game game = (Game) gameRepository.findGameByTitle(aGameTitle).orElse(null);
        if (game == null) {
            throw new IllegalArgumentException("There is no Game with title: " + aGameTitle);
        }
    
        List<Request> staffRequests = requestRepository.findRequestsByCreatedRequestUsername(aStaffUsername).orElse(null);
        if (staffRequests != null) {
            for (Request r : staffRequests) {
                if (r instanceof GameRequest && ((GameRequest) r).getGame().getTitle().equals(aGameTitle) && ((GameRequest) r).getType() == requestType) {
                    throw new IllegalArgumentException("There is already a request of type " + requestType.toString() + " from "  + aStaffUsername + " regarding " + aGameTitle);
                }
            }
        }
    
        GameRequest gameRequest = new GameRequest(Request.Status.PENDING, reason, staff, requestType, game);
        return requestRepository.save(gameRequest);
    }

    @Transactional
    public Request handleRequestApproval(int requestId, boolean approval) {
        Request request = requestRepository.findRequestById(requestId).orElse(null);
        if (request == null) {
            throw new IllegalArgumentException("There is no Request with id: " + requestId);
        }
        Request.Status newStatus = approval ? Request.Status.APPROVED : Request.Status.DENIED;
        request.setStatus(newStatus);

        if (approval && request instanceof UserRequest) {
            appUserService.deactivateClientAccountByUsername(((UserRequest) request).getUserFacingJudgement().getUsername());
        }

        if (approval && request instanceof GameRequest) {
            GameRequest gameRequest = (GameRequest) request;
            if (gameRequest.getType() == GameRequest.Type.ADD) {
                gameService.makeGameOffered(gameRequest.getGame().getId());
            } else {
                gameService.makeGameNotOffered(gameRequest.getGame().getId());
            }

        }

        return requestRepository.save(request);
    }
}
