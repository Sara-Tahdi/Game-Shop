package ca.mcgill.ecse321.gamecenter.dto.Promotion;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Promotion;

import java.sql.Date;

public class PromotionResponseDTO {
    private int id;
    private float newPrice;
    private Date startDate;
    private Date endDate;
    private Game game;

    public PromotionResponseDTO(Promotion p) {
        this.id = p.getId();
        this.newPrice = p.getNewPrice();
        this.startDate = p.getStartDate();
        this.endDate = p.getEndDate();
        this.game = p.getGame();
    }

    public PromotionResponseDTO(){}

    public int getId() {return id;}
    public float getNewPrice() {return this.newPrice;}
    public Date getStartDate() {return this.startDate;}
    public Date getEndDate() {return this.endDate;}
    public Game getGame() {return this.game;}
}
