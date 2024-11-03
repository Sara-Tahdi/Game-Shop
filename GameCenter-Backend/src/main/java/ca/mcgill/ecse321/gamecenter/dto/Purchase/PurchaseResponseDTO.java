package ca.mcgill.ecse321.gamecenter.dto.Purchase;

import java.sql.Date;

public class PurchaseResponseDTO {
    private int id;
    private float totalPrice;
    private int copies;
    private int trackingCode;
    private Date purchaseDate;
    private String refundReason;

    public PurchaseResponseDTO() {}

    public PurchaseResponseDTO(int id, float totalPrice, int copies, int trackingCode, Date purchaseDate, String refundReason) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.copies = copies;
        this.trackingCode = trackingCode;
        this.purchaseDate = purchaseDate;
        this.refundReason = refundReason;
    }

    public int getId() { return this.id; }
    public float getTotalPrice() { return this.totalPrice; }
    public int getCopies() { return this.copies; }
    public int getTrackingCode() { return this.trackingCode; }
    public Date getPurchaseDate() { return this.purchaseDate; }
    public String getRefundReason() { return this.refundReason; }
}
