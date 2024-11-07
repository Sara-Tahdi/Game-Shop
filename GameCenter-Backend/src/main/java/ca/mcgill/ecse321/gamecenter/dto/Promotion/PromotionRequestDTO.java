package ca.mcgill.ecse321.gamecenter.dto.Promotion;

import ca.mcgill.ecse321.gamecenter.model.Game;
import jakarta.validation.constraints.NotBlank;

import java.sql.Date;

public class PromotionRequestDTO {
    @NotBlank(message = "New Price is required")
    private float newPrice;
    @NotBlank(message = "Start Date is required")
    private Date startDate;
    @NotBlank(message = "End Date is required")
    private Date endDate;
    @NotBlank(message = "Game is required")
    private Game game;


    public PromotionRequestDTO(float newPrice, Date startDate, Date endDate, Game game) {
        this.newPrice = newPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.game = game;
    }

    public float getNewPrice() {return this.newPrice;}
    public Date getStartDate() {return this.startDate;}
    public Date getEndDate() {return this.endDate;}
    public Game getGame() {return this.game;}

}
