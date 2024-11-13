package ca.mcgill.ecse321.gamecenter.dto.PaymentInfo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class PaymentInfoRequestDTO {
    @NotBlank(message = "Card number is required")
    @Length(min = 16, max = 16, message = "Card number must be 16 digits")
    private String cardNumber;
    @NotBlank(message = "CVV is required")
    @Length(min = 3, max = 3, message = "CVV must be 3 digits")
    private String cvv;
    @Min(value = 1, message = "Please enter a valid expiry month")
    @Max(value = 12, message = "Please enter a valid expiry month")
    private int expiryMonth;
    @Min(value = 2024, message = "Please enter a valid expiry year")
    private int expiryYear;

    public PaymentInfoRequestDTO(String cardNumber, String cvv, int expiryMonth, int expiryYear) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
    }

    public String getCardNumber() { return this.cardNumber; }
    public String getCvv() { return this.cvv; }
    public int getExpiryMonth() { return this.expiryMonth; }
    public int getExpiryYear() { return this.expiryYear; }

}
