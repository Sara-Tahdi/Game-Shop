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
    
    public List<Request> getAllRequests() {
        List<Request> a = requestRepository.findRequestsByRequestType(Request.class).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There are no Requests");
        }
        return a;
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

    public List<Request> getRequestsByCreatedRequestsUsername(String createdRequestsUsername) {
        List<Request> a = requestRepository.findRequestsByCreatedRequestUsername(createdRequestsUsername).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There are no Requests with createdRequestsUsername: " + createdRequestsUsername);
        }
        return a;
    }

    public List<Request> getRequestsByStatus(Request.Status status) {
        List<Request> a = requestRepository.findRequestsByStatus(status).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There are no Requests with status: " + status);
        }
        return a;
    }

    @Transactional
    public UserRequest flagUser(String aStaffUsername, String aClientUsername) {
        Staff staff = (Staff) staffRepository.findStaffByUsername(aStaffUsername).orElse(null);
        if (staff == null) {
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
        UserRequest userRequest = new UserRequest(Request.Status.PENDING, staff, client);
        return requestRepository.save(userRequest);
    }

    @Transactional
    public GameRequest addGameRequest(String aStaffUsername, String aGameTitle) {
        return createGameRequest(aStaffUsername, aGameTitle, GameRequest.Type.ADD);
    }

    @Transactional
    public GameRequest removeGameRequest(String aStaffUsername, String aGameTitle) {
        return createGameRequest(aStaffUsername, aGameTitle, GameRequest.Type.REMOVE);
    }

    private GameRequest createGameRequest(String aStaffUsername, String aGameTitle, GameRequest.Type requestType) {
        Staff staff = (Staff) staffRepository.findStaffByUsername(aStaffUsername).orElse(null);
        if (staff == null) {
            throw new IllegalArgumentException("There is no Staff with username: " + aStaffUsername);
        }
    
        Game game = (Game) gameRepository.findGameByTitle(aGameTitle).orElse(null);
        if (game == null) {
            throw new IllegalArgumentException("There is no Game with name: " + aGameTitle);
        }
    
        List<Request> staffRequests = requestRepository.findRequestsByCreatedRequestUsername(aStaffUsername).orElse(null);
        if (staffRequests != null) {
            for (Request r : staffRequests) {
                if (r instanceof GameRequest && ((GameRequest) r).getGame().getTitle().equals(aGameTitle) && ((GameRequest) r).getType() == requestType) {
                    throw new IllegalArgumentException("There is already a request of type" + requestType.toString() + "from"  + aStaffUsername + " regarding " + aGameTitle);
                }
            }
        }
    
        GameRequest gameRequest = new GameRequest(Request.Status.PENDING, staff, requestType, game);
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
        return requestRepository.save(request);
    }

}
