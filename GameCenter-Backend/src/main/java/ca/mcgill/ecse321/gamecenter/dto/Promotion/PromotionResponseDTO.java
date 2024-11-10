package ca.mcgill.ecse321.gamecenter.dto.Promotion;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Promotion;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.time.LocalDate;

public class PromotionResponseDTO {
    private int id;
    private float newPrice;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Game game;

    public PromotionResponseDTO(Promotion p) {
        this.id = p.getId();
        this.newPrice = p.getNewPrice();
        this.startDate = p.getStartDate().toLocalDate();
        this.endDate = p.getEndDate().toLocalDate();
        this.game = p.getGame();
    }

    public PromotionResponseDTO(){}

    public int getId() {return id;}
    public float getNewPrice() {return this.newPrice;}
    public LocalDate getStartDate() {return this.startDate;}
    public LocalDate getEndDate() {return this.endDate;}
    public Game getGame() {return this.game;}
}
