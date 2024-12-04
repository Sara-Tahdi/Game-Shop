package ca.mcgill.ecse321.gamecenter.dto.PaymentInfo;

import ca.mcgill.ecse321.gamecenter.dto.AppUsers.ClientResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.PaymentInfo;

public class PaymentInfoResponseDTO {
    private int id;
    private String lastFourDigits;
    private int expiryMonth;
    private int expiryYear;
    private int clientId;

    public PaymentInfoResponseDTO() {}

    public PaymentInfoResponseDTO(PaymentInfo p) {
        this.id = p.getId();
        this.lastFourDigits = p.getCardNumber().substring(p.getCardNumber().length() - 4);
        this.expiryMonth = p.getExpiryMonth();
        this.expiryYear = p.getExpiryYear();
        this.clientId = p.getClient().getId();
    }

    public int getId() { return id; }
    public String getLastFourDigits() { return this.lastFourDigits; }
    public int getExpiryMonth() { return expiryMonth; }
    public int getExpiryYear() { return expiryYear; }
    public int getClientId() { return clientId; }
}
