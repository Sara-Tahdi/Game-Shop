package ca.mcgill.ecse321.gamecenter.dto.Requests;
import ca.mcgill.ecse321.gamecenter.model.GameRequest;
import ca.mcgill.ecse321.gamecenter.dto.AppUsers.AppUserResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Game.GameResponseDTO;



public class GameRequestResponseDTO {
    private int id;
    private String status;
    private String reason;
    private String requestType;
    private AppUserResponseDTO createdRequest;
    private GameResponseDTO game;


    @SuppressWarnings("unused")
    private GameRequestResponseDTO() {}

    public GameRequestResponseDTO(GameRequest request) {
        this.id = request.getId();
        this.status = request.getStatus().toString();
        this.reason = request.getReason();
        this.requestType = request.getType().toString();
        this.createdRequest = new AppUserResponseDTO(request.getCreatedRequest());
        this.game = new GameResponseDTO(request.getGame());

    }

    public int getId() { return this.id; }
    public String getStatus() { return this.status; }
    public String getReason() { return this.reason; }
    public String getRequestType() { return this.requestType; }
    public AppUserResponseDTO getCreatedRequest() { return this.createdRequest; }
    public GameResponseDTO getGame() { return this.game; }

    public void setId(int id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
    public void setReason(String reason) { this.reason = reason; }
    public void setRequestType(String requestType) { this.requestType = requestType; }
    public void setCreatedRequest(AppUserResponseDTO createdRequest) { this.createdRequest = createdRequest; }
    public void setGame(GameResponseDTO game) { this.game = game; }
}
