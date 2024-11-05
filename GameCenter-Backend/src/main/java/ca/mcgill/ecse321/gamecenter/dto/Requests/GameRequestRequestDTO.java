package ca.mcgill.ecse321.gamecenter.dto.Requests;

import jakarta.validation.constraints.NotBlank;


public class GameRequestRequestDTO {
    @NotBlank(message = "")
    private String reason;
    @NotBlank(message = "Creator's username is required")
    private String createdRequestUsername;
    @NotBlank(message = "Game title is required")
    private String gameTitle;

    public GameRequestRequestDTO(String reason, String createdRequestUsername, String gameTitle) {
        this.reason = reason;
        this.createdRequestUsername = createdRequestUsername;
        this.gameTitle = gameTitle;
    }

    public String getReason() { return this.reason; }
    public String getCreatedRequestUsername() { return this.createdRequestUsername; }
    public String getGameTitle() { return this.gameTitle; }    
}
