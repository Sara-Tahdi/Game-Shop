package ca.mcgill.ecse321.gamecenter.dto.Purchase;

import jakarta.validation.constraints.NotBlank;

public class RefundRequestDTO {
    @NotBlank(message = "Refund reason is required")
    private String reason;

    public RefundRequestDTO() {}

    public RefundRequestDTO(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }
}
