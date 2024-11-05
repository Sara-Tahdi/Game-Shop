package ca.mcgill.ecse321.gamecenter.dto.AppUsers;

import ca.mcgill.ecse321.gamecenter.model.Employee;

public class OwnerResponseDTO {
    private int id;
    private String username;
    private String email;
    private String password;
    private boolean isActive;

    public OwnerResponseDTO() {
    }

    public OwnerResponseDTO(Employee employee) {
        this.id = employee.getId();
        this.username = employee.getUsername();
        this.email = employee.getEmail();
        this.password = employee.getPassword();
        this.isActive = employee.getIsActive();
    }

    public int getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
    public boolean getIsActive() { return this.isActive; }
}
