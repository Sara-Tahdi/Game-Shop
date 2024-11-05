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



    public UserRequestResponseDTO() {}

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
}
