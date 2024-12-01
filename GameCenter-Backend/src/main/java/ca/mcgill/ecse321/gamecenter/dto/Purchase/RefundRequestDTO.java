package ca.mcgill.ecse321.gamecenter.dto.Purchase;

import jakarta.validation.constraints.NotBlank;

public class RefundRequestDTO {
    @NotBlank(message = "Refund reason is required")
    private String refundReason;

    public RefundRequestDTO() {}

    public RefundRequestDTO(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundReason() {
        return this.refundReason;
    }
}
