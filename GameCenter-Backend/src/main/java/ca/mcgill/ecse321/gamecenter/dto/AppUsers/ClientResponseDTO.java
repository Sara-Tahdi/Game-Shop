package ca.mcgill.ecse321.gamecenter.dto.AppUsers;

import ca.mcgill.ecse321.gamecenter.model.Client;

public class ClientResponseDTO {
    private int id;
    private String username;
    private String email;
    private String phoneNumber;
    private String deliveryAddress;
    private boolean isActive;
    private String userType;


    public ClientResponseDTO() {}

    public ClientResponseDTO(Client client) {
        this.id = client.getId();
        this.username = client.getUsername();
        this.email = client.getEmail();
        this.phoneNumber = client.getPhoneNumber();
        this.deliveryAddress = client.getDeliveryAddress();
        this.isActive = client.getIsActive();
        this.userType = "Client";
    }

    public int getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public String getPhoneNumber() { return this.phoneNumber; }
    public String getDeliveryAddress() { return this.deliveryAddress; }

    public boolean getIsActive() { return this.isActive; }
}
