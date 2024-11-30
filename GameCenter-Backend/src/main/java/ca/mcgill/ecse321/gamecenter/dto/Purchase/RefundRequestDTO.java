package ca.mcgill.ecse321.gamecenter.dto.Purchase;

import jakarta.validation.constraints.NotBlank;

public class RefundRequestDTO {
    @NotBlank(message = "Refund reason is required")
    private String refundReason;

    public RefundRequestDTO() {}

    public RefundRequestDTO(String reason) {
        this.refundReason = reason;
    }

    public String getReason() {
        return this.refundReason;
    }
}
