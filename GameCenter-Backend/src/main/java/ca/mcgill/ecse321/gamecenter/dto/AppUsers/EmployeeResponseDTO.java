package ca.mcgill.ecse321.gamecenter.dto.AppUsers;

import ca.mcgill.ecse321.gamecenter.model.Employee;

public class EmployeeResponseDTO {
    private int id;
    private String username;
    private String email;
    private boolean isActive;
    private String userType;

    public EmployeeResponseDTO() {
    }

    public EmployeeResponseDTO(Employee employee) {
        this.id = employee.getId();
        this.username = employee.getUsername();
        this.email = employee.getEmail();
        this.isActive = employee.getIsActive();
        this.userType = "Employee";
    }

    public int getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public boolean getIsActive() { return this.isActive; }
    public String getUserType() { return this.userType; }
}
