package ca.mcgill.ecse321.gamecenter.dto.Requests;
import ca.mcgill.ecse321.gamecenter.model.UserRequest;
import ca.mcgill.ecse321.gamecenter.dto.AppUsers.AppUserResponseDTO;



public class UserRequestResponseDTO {
    private int id;
    private String status;
    private String reason;
    private AppUserResponseDTO createdRequest;
    private AppUserResponseDTO userFacingJudgement;

    @SuppressWarnings("unused")
    private UserRequestResponseDTO() {}

    public UserRequestResponseDTO(UserRequest request) {
        this.id = request.getId();
        this.status = request.getStatus().toString();
        this.reason = request.getReason();
        this.createdRequest = new AppUserResponseDTO(request.getCreatedRequest());
        this.userFacingJudgement = new AppUserResponseDTO(request.getUserFacingJudgement());
    }

    public int getId() { return this.id; }
    public String getStatus() { return this.status; }
    public String getReason() { return this.reason; }
    public AppUserResponseDTO getCreatedRequest() { return this.createdRequest; }
    public AppUserResponseDTO getUserFacingJudgement() { return this.userFacingJudgement; }

    public void setId(int id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
    public void setReason(String reason) { this.reason = reason; }
    public void setCreatedRequest(AppUserResponseDTO createdRequest) { this.createdRequest = createdRequest; }
    public void setUserFacingJudgement(AppUserResponseDTO userFacingJudgement) { this.userFacingJudgement = userFacingJudgement; }
}
