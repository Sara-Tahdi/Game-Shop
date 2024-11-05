package ca.mcgill.ecse321.gamecenter.dto.Requests;
import ca.mcgill.ecse321.gamecenter.model.GameRequest;



public class GameRequestResponseDTO {
    private int id;
    private String status;
    private String reason;
    private int createdRequestId;
    private String createdRequestUsername;
    private String createdRequestEmail;
    private String requestType;
    private String gameTitle;
    private int gameId;

    public GameRequestResponseDTO() {}

    public GameRequestResponseDTO(GameRequest request) {
        this.id = request.getId();
        this.status = request.getStatus().toString();
        this.reason = request.getReason();
        this.createdRequestId = request.getCreatedRequest().getId();
        this.createdRequestUsername = request.getCreatedRequest().getUsername();
        this.createdRequestEmail = request.getCreatedRequest().getEmail();
        this.requestType = request.getType().toString();
        this.gameTitle = request.getGame().getTitle();
        this.gameId = request.getGame().getId();
    }

    public int getId() { return this.id; }
    public String getStatus() { return this.status; }
    public String getReason() { return this.reason; }
    public int getCreatedRequestId() { return this.createdRequestId; }
    public String getCreatedRequestUsername() { return this.createdRequestUsername; }
    public String getCreatedRequestEmail() { return this.createdRequestEmail; }
    public String getRequestType() { return this.requestType; }
    public String getGameTitle() { return this.gameTitle; }
    public int getGameId() { return this.gameId; }
}
