package ca.mcgill.ecse321.gamecenter.dto.AppUsers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ClientRequestDTO {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    private String oldPassword;

    public ClientRequestDTO() {}

    public ClientRequestDTO(String email, String username, String password, String phoneNumber, String deliveryAddress, String oldPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.oldPassword = oldPassword;
    }

    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
    public String getPhoneNumber() { return this.phoneNumber; }
    public String getDeliveryAddress() { return this.deliveryAddress; }
    public String getOldPassword() { return this.oldPassword; }
}
