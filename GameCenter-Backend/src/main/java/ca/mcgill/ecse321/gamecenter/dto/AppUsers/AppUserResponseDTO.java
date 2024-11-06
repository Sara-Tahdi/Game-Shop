package ca.mcgill.ecse321.gamecenter.dto.AppUsers;
import ca.mcgill.ecse321.gamecenter.model.AppUser;

public class AppUserResponseDTO {
    private int id;
    private String email;
    private String username;
    private String userType;

    @SuppressWarnings("unused")
    private AppUserResponseDTO() {}

    public AppUserResponseDTO(AppUser user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.userType = user.getClass().getSimpleName();
    }

    public int getId() { return this.id; }
    public String getEmail() { return this.email; }
    public String getUsername() { return this.username; }
    public String getUserType() { return this.userType; }

    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setUserType(String userType) { this.userType = userType; }
}
