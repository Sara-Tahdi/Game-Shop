package ca.mcgill.ecse321.gamecenter.dto.Requests;
import ca.mcgill.ecse321.gamecenter.model.Request;

public class RequestResponseDTO {
    private int id;
    private String status;
    private String reason;
    private int createdRequestId;
    private String createdRequestUsername;
    private String createdRequestEmail;
    private String requestType;

    @SuppressWarnings("unused")
    private RequestResponseDTO() {}

    public RequestResponseDTO(Request request) {
        this.id = request.getId();
        this.status = request.getStatus().toString();
        this.reason = request.getReason();
        this.createdRequestId = request.getCreatedRequest().getId();
        this.createdRequestUsername = request.getCreatedRequest().getUsername();
        this.createdRequestEmail = request.getCreatedRequest().getEmail();
        this.requestType = request.getClass().getSimpleName();
    }

    public int getId() { return this.id; }
    public String getStatus() { return this.status; }
    public String getReason() { return this.reason; }
    public int getCreatedRequestId() { return this.createdRequestId; }
    public String getCreatedRequestUsername() { return this.createdRequestUsername; }
    public String getCreatedRequestEmail() { return this.createdRequestEmail; }
    public String getRequestType() { return this.requestType; }

    public void setId(int id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
    public void setReason(String reason) { this.reason = reason; }
    public void setCreatedRequestId(int createdRequestId) { this.createdRequestId = createdRequestId; }
    public void setCreatedRequestUsername(String createdRequestUsername) { this.createdRequestUsername = createdRequestUsername; }
    public void setCreatedRequestEmail(String createdRequestEmail) { this.createdRequestEmail = createdRequestEmail; }
    public void setRequestType(String requestType) { this.requestType = requestType; }
}
