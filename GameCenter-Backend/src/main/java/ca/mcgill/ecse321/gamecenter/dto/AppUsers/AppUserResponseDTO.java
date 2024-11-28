package ca.mcgill.ecse321.gamecenter.dto.AppUsers;
import ca.mcgill.ecse321.gamecenter.model.AppUser;
import ca.mcgill.ecse321.gamecenter.model.Client;

public class AppUserResponseDTO {
    private int id;
    private String email;
    private String username;
    private String deliveryAddress;
    private String phoneNumber;
    private String userType;

    @SuppressWarnings("unused")
    private AppUserResponseDTO() {}

    public AppUserResponseDTO(AppUser user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.userType = user.getClass().getSimpleName();
        if (this.userType.equals("Client")) {
            this.deliveryAddress = ((Client) user).getDeliveryAddress();
            this.phoneNumber = ((Client) user).getPhoneNumber();
        }
    }

    public int getId() { return this.id; }
    public String getEmail() { return this.email; }
    public String getUsername() { return this.username; }
    public String getDeliveryAddress() { return this.deliveryAddress; }
    public String getPhoneNumber() { return this.phoneNumber; }
    public String getUserType() { return this.userType; }

    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setUserType(String userType) { this.userType = userType; }
}
