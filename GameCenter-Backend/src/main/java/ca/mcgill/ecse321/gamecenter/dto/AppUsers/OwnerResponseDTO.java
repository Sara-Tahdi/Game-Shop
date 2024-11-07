package ca.mcgill.ecse321.gamecenter.dto.AppUsers;

import ca.mcgill.ecse321.gamecenter.model.Employee;
import ca.mcgill.ecse321.gamecenter.model.Owner;

public class OwnerResponseDTO {
    private int id;
    private String username;
    private String email;
    private boolean isActive;

    public OwnerResponseDTO() {
    }

    public OwnerResponseDTO(Owner owner) {
        this.id = owner.getId();
        this.username = owner.getUsername();
        this.email = owner.getEmail();
        this.isActive = owner.getIsActive();
    }

    public int getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public boolean getIsActive() { return this.isActive; }
}
