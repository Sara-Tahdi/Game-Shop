package ca.mcgill.ecse321.gamecenter.dto.Requests;
import ca.mcgill.ecse321.gamecenter.model.Request;
import ca.mcgill.ecse321.gamecenter.dto.AppUsers.AppUserResponseDTO;

public class RequestResponseDTO {
    private int id;
    private String status;
    private String reason;
    private String requestType;
    private AppUserResponseDTO createdRequest;

    @SuppressWarnings("unused")
    private RequestResponseDTO() {}

    public RequestResponseDTO(Request request) {
        this.id = request.getId();
        this.status = request.getStatus().toString();
        this.reason = request.getReason();
        this.createdRequest = new AppUserResponseDTO(request.getCreatedRequest());
        this.requestType = request.getClass().getSimpleName();
    }

    public int getId() { return this.id; }
    public String getStatus() { return this.status; }
    public String getReason() { return this.reason; }
    public AppUserResponseDTO getCreatedRequest() { return this.createdRequest; }
    public String getRequestType() { return this.requestType; }

    public void setId(int id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
    public void setReason(String reason) { this.reason = reason; }
    public void setCreatedRequest(AppUserResponseDTO createdRequest) { this.createdRequest = createdRequest; }
    public void setRequestType(String requestType) { this.requestType = requestType; }
}
