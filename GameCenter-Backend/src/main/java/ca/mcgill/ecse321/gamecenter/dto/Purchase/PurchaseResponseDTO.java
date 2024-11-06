package ca.mcgill.ecse321.gamecenter.dto.Purchase;

import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Purchase;

import java.sql.Date;

public class PurchaseResponseDTO {
    private int id;
    private float totalPrice;
    private int copies;
    private int trackingCode;
    private Date purchaseDate;
    private String refundReason;
    private Client client;
    private Game game;

    public PurchaseResponseDTO() {}

    public PurchaseResponseDTO(Purchase p) {
        this.id = p.getId();
        this.totalPrice = p.getTotalPrice();
        this.copies = p.getCopies();
        this.trackingCode = p.getTrackingCode();
        this.purchaseDate = p.getPurchaseDate();
        this.refundReason = p.getRefundReason();
        this.client = p.getClient();
        this.game = p.getGame();
    }

    public int getId() { return this.id; }
    public float getTotalPrice() { return this.totalPrice; }
    public int getCopies() { return this.copies; }
    public int getTrackingCode() { return this.trackingCode; }
    public Date getPurchaseDate() { return this.purchaseDate; }
    public String getRefundReason() { return this.refundReason; }
    public Client getClient() { return this.client; }
    public Game getGame() { return this.game; }
}
