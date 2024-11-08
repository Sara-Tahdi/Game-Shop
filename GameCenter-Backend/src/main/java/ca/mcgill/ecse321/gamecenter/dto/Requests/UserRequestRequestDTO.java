package ca.mcgill.ecse321.gamecenter.dto.Requests;
import jakarta.validation.constraints.NotBlank;

public class UserRequestRequestDTO {
    @NotBlank(message = "")
    private String reason;
    @NotBlank(message = "Creator's username is required")
    private String createdRequestUsername;
    @NotBlank(message = "Flagged user's username is required")
    private String userFacingJudgementUsername;

    @SuppressWarnings("unused")
    private UserRequestRequestDTO() {}

    public UserRequestRequestDTO(String reason, String createdRequestUsername, String userFacingJudgementUsername) {
        this.reason = reason;
        this.createdRequestUsername = createdRequestUsername;
        this.userFacingJudgementUsername = userFacingJudgementUsername;
    }

    public String getReason() { return this.reason; }
    public String getCreatedRequestUsername() { return this.createdRequestUsername; }
    public String getUserFacingJudgementUsername() { return this.userFacingJudgementUsername; }

    public void setReason(String reason) { this.reason = reason; }
    public void setCreatedRequestUsername(String createdRequestUsername) { this.createdRequestUsername = createdRequestUsername; }
    public void setUserFacingJudgementUsername(String userFacingJudgementUsername) { this.userFacingJudgementUsername = userFacingJudgementUsername; }
}
