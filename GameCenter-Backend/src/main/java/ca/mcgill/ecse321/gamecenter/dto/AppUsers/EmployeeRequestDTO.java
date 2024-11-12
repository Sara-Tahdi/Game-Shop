package ca.mcgill.ecse321.gamecenter.dto.AppUsers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmployeeRequestDTO {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;

    private String oldPassword;

    public EmployeeRequestDTO() {}

    public EmployeeRequestDTO(String email, String username, String password, String oldPassword) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.oldPassword = oldPassword;
    }

    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
    public String getOldPassword() { return this.oldPassword; }
}
