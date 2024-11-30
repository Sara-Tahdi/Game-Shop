package ca.mcgill.ecse321.gamecenter.dto.AppUsers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class OwnerCreateRequestDTO {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Validation Password is required")
    private String password;

    public OwnerCreateRequestDTO(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
}
