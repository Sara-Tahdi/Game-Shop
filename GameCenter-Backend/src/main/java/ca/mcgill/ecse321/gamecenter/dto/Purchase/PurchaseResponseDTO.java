package ca.mcgill.ecse321.gamecenter.dto.Purchase;

import ca.mcgill.ecse321.gamecenter.dto.AppUsers.ClientResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Game.GameResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Purchase;

import java.sql.Date;

public class PurchaseResponseDTO {
    private int id;
    private float totalPrice;
    private int copies;
    private String trackingCode;
    private Date purchaseDate;
    private String refundReason;
    private ClientResponseDTO client;
    private GameResponseDTO game;

    public PurchaseResponseDTO() {}

    public PurchaseResponseDTO(Purchase p) {
        this.id = p.getId();
        this.totalPrice = p.getTotalPrice();
        this.copies = p.getCopies();
        this.trackingCode = p.getTrackingCode();
        this.purchaseDate = p.getPurchaseDate();
        this.refundReason = p.getRefundReason();
        this.client = new ClientResponseDTO(p.getClient());
        this.game = new GameResponseDTO(p.getGame());
    }

    public int getId() { return this.id; }
    public float getTotalPrice() { return this.totalPrice; }
    public int getCopies() { return this.copies; }
    public String getTrackingCode() { return this.trackingCode; }
    public Date getPurchaseDate() { return this.purchaseDate; }
    public String getRefundReason() { return this.refundReason; }
    public ClientResponseDTO getClient() { return this.client; }
    public GameResponseDTO getGame() { return this.game; }
}
