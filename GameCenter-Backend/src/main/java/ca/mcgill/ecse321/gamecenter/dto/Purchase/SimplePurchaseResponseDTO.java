package ca.mcgill.ecse321.gamecenter.dto.Purchase;

import ca.mcgill.ecse321.gamecenter.model.Purchase;
import ca.mcgill.ecse321.gamecenter.utilities.Round;


import java.util.List;
import java.util.Map;

public class SimplePurchaseResponseDTO {
    private String trackingCode;
    private Float totalPrice;

    public SimplePurchaseResponseDTO() {}

    public SimplePurchaseResponseDTO(String trackingCode, Float totalPrice) {
        this.totalPrice = totalPrice;
        this.trackingCode = trackingCode;
    }

    public Float getTotalPrice() { return totalPrice; }
    public String getTrackingCode() { return trackingCode; }
}
