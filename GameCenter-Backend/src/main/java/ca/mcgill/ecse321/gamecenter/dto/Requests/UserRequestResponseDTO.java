package ca.mcgill.ecse321.gamecenter.dto.Requests;

public class UserRequestResponseDTO {
    private int id;
    private String status;
    private String reason;
    private int createdRequestId;
    private String createdRequestUsername;
    private String createdRequestEmail;
    private String requestType;
    private String username;
    
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
        
        this.username = request.getUser().getUsername();
    }

    public int getId() { return this.id; }
    public String getStatus() { return this.status; }
    public String getReason() { return this.reason; }
    public int getCreatedRequestId() { return this.createdRequestId; }
    public String getCreatedRequestUsername() { return this.createdRequestUsername; }
    public String getCreatedRequestEmail() { return this.createdRequestEmail; }
    public String getRequestType() { return this.requestType; }
    public String getUsername() { return this.username; }
}
