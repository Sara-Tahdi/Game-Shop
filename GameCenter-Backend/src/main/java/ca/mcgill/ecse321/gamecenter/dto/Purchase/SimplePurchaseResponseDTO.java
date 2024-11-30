package ca.mcgill.ecse321.gamecenter.dto.Purchase;

import ca.mcgill.ecse321.gamecenter.model.Purchase;
import ca.mcgill.ecse321.gamecenter.utilities.Round;


import java.util.List;
import java.util.Map;

public class SimplePurchaseResponseDTO {
    private String trackingCode;
    private Float totalPrice;
    private String refundReason;

    public SimplePurchaseResponseDTO() {}

    public SimplePurchaseResponseDTO(String trackingCode, Float totalPrice, String refundReason) {
        this.totalPrice = totalPrice;
        this.trackingCode = trackingCode;
        this.refundReason = refundReason;
    }

    public Float getTotalPrice() { return totalPrice; }
    public String getTrackingCode() { return trackingCode; }

    public String getRefundReason() { return refundReason; }
}
