package ca.mcgill.ecse321.gamecenter.dto.Requests;
import ca.mcgill.ecse321.gamecenter.model.UserRequest;


public class UserRequestResponseDTO {
    private int id;
    private String status;
    private String reason;
    private int createdRequestId;
    private String createdRequestUsername;
    private String createdRequestEmail;
    private int userFacingJudgementId;
    private String userFacingJudgementUsername;
    private String userFacingJudgementEmailString;

    @SuppressWarnings("unused")
    private UserRequestResponseDTO() {}

    public UserRequestResponseDTO(UserRequest request) {
        this.id = request.getId();
        this.status = request.getStatus().toString();
        this.reason = request.getReason();
        this.createdRequestId = request.getCreatedRequest().getId();
        this.createdRequestUsername = request.getCreatedRequest().getUsername();
        this.createdRequestEmail = request.getCreatedRequest().getEmail();
        this.userFacingJudgementId = request.getUserFacingJudgement().getId();
        this.userFacingJudgementUsername = request.getUserFacingJudgement().getUsername();
        this.userFacingJudgementEmailString = request.getUserFacingJudgement().getEmail();
    }

    public int getId() { return this.id; }
    public String getStatus() { return this.status; }
    public String getReason() { return this.reason; }
    public int getCreatedRequestId() { return this.createdRequestId; }
    public String getCreatedRequestUsername() { return this.createdRequestUsername; }
    public String getCreatedRequestEmail() { return this.createdRequestEmail; }
    public int getUserFacingJudgementId() { return this.userFacingJudgementId; }
    public String getUserFacingJudgementUsername() { return this.userFacingJudgementUsername; }
    public String getUserFacingJudgementEmailString() { return this.userFacingJudgementEmailString; }

    public void setId(int id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
    public void setReason(String reason) { this.reason = reason; }
    public void setCreatedRequestId(int createdRequestId) { this.createdRequestId = createdRequestId; }
    public void setCreatedRequestUsername(String createdRequestUsername) { this.createdRequestUsername = createdRequestUsername; }
    public void setCreatedRequestEmail(String createdRequestEmail) { this.createdRequestEmail = createdRequestEmail; }
    public void setUserFacingJudgementId(int userFacingJudgementId) { this.userFacingJudgementId = userFacingJudgementId; }
    public void setUserFacingJudgementUsername(String userFacingJudgementUsername) { this.userFacingJudgementUsername = userFacingJudgementUsername; }
    public void setUserFacingJudgementEmailString(String userFacingJudgementEmailString) { this.userFacingJudgementEmailString = userFacingJudgementEmailString; }
}
